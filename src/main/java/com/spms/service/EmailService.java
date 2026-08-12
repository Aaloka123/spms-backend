package com.spms.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${spms.mail.from:${spring.mail.username}}")
    private String fromAddress;

    /** Shown in inbox as the sender name, e.g. "MedNexus" */
    @Value("${spms.mail.from-name:MedNexus}")
    private String fromName;

    @Value("${spms.otp.ttl-minutes:30}")
    private int otpTtlMinutes;

    @Value("${spms.frontend-url:http://localhost:4200}")
    private String frontendUrl;

    /**
     * Public logo image URL (Cloudinary). Same MedNexus logo used in Pharmacy Management emails.
     * Leave blank to fall back to text branding.
     */
    @Value("${spms.mail.logo-url:}")
    private String logoUrl;

    public void sendLoginOtp(String toEmail, String code) {
        sendOtpEmail(
                toEmail,
                code,
                "Your MedNexus login verification code",
                "Verify your login",
                "Use the verification code below to sign in to your MedNexus account.");
    }

    public void sendPasswordResetOtp(String toEmail, String code) {
        sendOtpEmail(
                toEmail,
                code,
                "Your MedNexus password reset code",
                "Reset your password",
                "Use the verification code below to reset your MedNexus account password.");
    }

    private void sendOtpEmail(
            String toEmail,
            String code,
            String subject,
            String headline,
            String intro) {
        String html = EmailHtmlBuilder.otpVerification(
                headline,
                intro,
                formatCodeForDisplay(code),
                otpTtlMinutes,
                logoUrl,
                frontendUrl);

        String plainText = """
                %s

                Your verification code is: %s

                This code expires in %d minutes. Do not share it with anyone.

                — The MedNexus Team
                """.formatted(intro, code, otpTtlMinutes);

        sendHtmlEmail(toEmail, subject, html, plainText);
    }

    private void sendHtmlEmail(String to, String subject, String html, String plainText) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress(fromAddress, fromName, "UTF-8"));
            helper.setTo(to);
            helper.setSubject(subject);
            // plain + html multipart (Gmail shows the HTML part)
            helper.setText(plainText, html);

            mailSender.send(message);
            log.info("Sent HTML OTP email to {} from {}", to, fromAddress);
        } catch (Exception ex) {
            log.error("Failed to send email to {}", to, ex);
        }
    }

    /** 943601 → "9 4 3 6 0 1" for MedNexus-style HTML emails */
    private static String formatCodeForDisplay(String code) {
        if (code == null || code.isBlank()) {
            return "";
        }
        return String.join(" ", code.trim().split(""));
    }
}
