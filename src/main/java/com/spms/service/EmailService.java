package com.spms.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    /** Content-ID for the inline logo attached to OTP emails */
    private static final String LOGO_CID = "mednexus-logo";
    private static final String LOGO_CLASSPATH = "email/mednexus-logo.png";

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
     * Optional public HTTPS logo URL.
     * Prefer classpath inline logo (email/mednexus-logo.png) — Gmail cannot load localhost images.
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
        ClassPathResource inlineLogo = new ClassPathResource(LOGO_CLASSPATH);
        boolean useInlineLogo = inlineLogo.exists();

        // Prefer CID attachment (works in Gmail). Fall back to external URL, then text brand.
        String headerLogo =
                useInlineLogo
                        ? "cid:" + LOGO_CID
                        : (StringUtils.hasText(logoUrl) ? logoUrl.trim() : "");

        String html = EmailHtmlBuilder.otpVerification(
                headline,
                intro,
                formatCodeForDisplay(code),
                otpTtlMinutes,
                headerLogo,
                frontendUrl);

        String plainText = """
                %s

                Your verification code is: %s

                This code expires in %d minutes. Do not share it with anyone.

                — The MedNexus Team
                """.formatted(intro, code, otpTtlMinutes);

        sendHtmlEmail(toEmail, subject, html, plainText, useInlineLogo ? inlineLogo : null);
    }

    private void sendHtmlEmail(
            String to,
            String subject,
            String html,
            String plainText,
            ClassPathResource inlineLogo) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(new InternetAddress(fromAddress, fromName, "UTF-8"));
            helper.setTo(to);
            helper.setSubject(subject);
            // plain + html multipart (Gmail shows the HTML part)
            helper.setText(plainText, html);

            if (inlineLogo != null) {
                helper.addInline(LOGO_CID, inlineLogo);
            }

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
        String digits = code.replaceAll("\\D", "");
        StringBuilder spaced = new StringBuilder();
        for (int i = 0; i < digits.length(); i++) {
            if (i > 0) {
                spaced.append(' ');
            }
            spaced.append(digits.charAt(i));
        }
        return spaced.toString();
    }
}
