package com.spms.service.impl;

import com.spms.auth.entity.Otp;
import com.spms.auth.entity.OtpPurpose;
import com.spms.auth.entity.User;
import com.spms.auth.repository.OtpRepository;
import com.spms.auth.repository.UserRepository;
import com.spms.dto.response.PendingOtpResponseDTO;
import com.spms.exception.InvalidOtpException;
import com.spms.service.EmailService;
import com.spms.service.OtpService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.Executor;

@Service
public class OtpServiceImpl implements OtpService {

    // Safer random than Math.random()
    private static final SecureRandom RANDOM = new SecureRandom();

    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final Executor mailExecutor;

    // How long OTP stays valid (from application.properties)
    private final int otpTtlMinutes;

    // @Qualifier picks the "mailExecutor" bean from AsyncConfig
    public OtpServiceImpl(
            OtpRepository otpRepository,
            UserRepository userRepository,
            EmailService emailService,
            @Qualifier("mailExecutor") Executor mailExecutor,
            @org.springframework.beans.factory.annotation.Value("${spms.otp.ttl-minutes:30}") int otpTtlMinutes) {
        this.otpRepository = otpRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.mailExecutor = mailExecutor;
        this.otpTtlMinutes = otpTtlMinutes;
    }

    // ---------- LOGIN OTP ----------

    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public PendingOtpResponseDTO issueLoginOtp(Long userId, String email) {
        // Remove old OTPs for this user (only one active OTP)
        otpRepository.deleteByUserId(userId);

        return issueOtp(
                userId,
                email,
                OtpPurpose.LOGIN,
                // which email method to call
                (to, code) -> emailService.sendLoginOtp(to, code)
        );
    }

    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public User verifyAndConsumeForLogin(String otpToken, String code) {
        Otp otp = verifyOtpRecord(otpToken, code, OtpPurpose.LOGIN);

        // Read id while transaction is still open (lazy proxy is safe here)
        Long userId = otp.getUser().getId();

        // One-time use: delete after success
        otpRepository.delete(otp);

        // Load full user with role before transaction ends
        // (avoids: Could not initialize proxy - no session)
        return userRepository.findById(userId)
                .flatMap(u -> userRepository.findByUsername(u.getUsername()))
                .orElseThrow(() ->
                        new InvalidOtpException("User not found after OTP verification."));
    }

    // ---------- PASSWORD RESET OTP ----------

    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public PendingOtpResponseDTO issuePasswordResetOtp(Long userId, String email) {
        otpRepository.deleteByUserId(userId);

        return issueOtp(
                userId,
                email,
                OtpPurpose.PASSWORD_RESET,
                (to, code) -> emailService.sendPasswordResetOtp(to, code)
        );
    }

    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public User verifyAndConsumeForPasswordReset(String otpToken, String code) {
        Otp otp = verifyOtpRecord(otpToken, code, OtpPurpose.PASSWORD_RESET);

        // Read id while transaction is still open
        Long userId = otp.getUser().getId();
        otpRepository.delete(otp);

        // Return a fully loaded user (not a lazy proxy)
        return userRepository.findById(userId)
                .orElseThrow(() ->
                        new InvalidOtpException("User not found after OTP verification."));
    }

    // ---------- SHARED PRIVATE HELPERS ----------

    // Creates OTP, saves it, emails code in background, returns response
    private PendingOtpResponseDTO issueOtp(
            Long userId,
            String email,
            OtpPurpose purpose,
            java.util.function.BiConsumer<String, String> mailSender) {

        // Clean expired OTPs (safe cleanup when issuing a new one)
        otpRepository.deleteByExpiresAtBefore(LocalDateTime.now());

        // 6-digit code, e.g. 004821
        String code = String.format("%06d", RANDOM.nextInt(1_000_000));

        // Random token for frontend (not the code itself)
        String otpToken = UUID.randomUUID().toString().replace("-", "");

        Otp otp = new Otp();
        otp.setPurpose(purpose);
        // Link to user without full load
        otp.setUser(userRepository.getReferenceById(userId));
        otp.setOtpToken(otpToken);
        otp.setCode(code);
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(otpTtlMinutes));

        otpRepository.save(otp);

        // Send email in another thread (API returns fast)
        String recipient = email;
        mailExecutor.execute(() -> mailSender.accept(recipient, code));

        return new PendingOtpResponseDTO(
                true,
                otpToken,
                maskEmail(email),
                "Check your email for the 6-digit verification code."
        );
    }

    // Validates token + code + purpose + expiry
    private Otp verifyOtpRecord(String otpToken, String code, OtpPurpose expectedPurpose) {

        if (otpToken == null || otpToken.isBlank()) {
            throw new InvalidOtpException("Verification session is invalid.");
        }

        String normalizedCode = code == null ? "" : code.trim();
        if (!normalizedCode.matches("\\d{6}")) {
            throw new InvalidOtpException("Enter the 6-digit verification code.");
        }

        // IMPORTANT: find first — do NOT delete expired rows before lookup
        // (timezone issues were deleting valid OTPs and causing "token not found")
        Otp otp = otpRepository.findByOtpToken(otpToken.trim())
                .orElseThrow(() ->
                        new InvalidOtpException(
                                "OTP token not found. Login again and copy otpToken from the login response (must match otp_token in DB)."));

        // LOGIN code must not work for password reset (and opposite)
        if (otp.getPurpose() != expectedPurpose) {
            throw new InvalidOtpException("This OTP is for a different purpose. Please try again.");
        }

        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            otpRepository.delete(otp);
            throw new InvalidOtpException("Verification code has expired. Please login again.");
        }

        if (!otp.getCode().equals(normalizedCode)) {
            throw new InvalidOtpException(
                    "Incorrect verification code. Use the code from the SAME otp row as your otpToken.");
        }

        return otp;
    }

    // aaloka@gmail.com → a***@gmail.com
    static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "your email";
        }
        int at = email.indexOf('@');
        String local = email.substring(0, at);
        String domain = email.substring(at);

        if (local.length() <= 1) {
            return "*" + domain;
        }
        return local.charAt(0) + "***" + domain;
    }
}