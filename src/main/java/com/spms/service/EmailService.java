package com.spms.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service // Marks this class as a Spring service
@RequiredArgsConstructor // Generates constructor for final fields
public class EmailService {

    // Logger for recording errors and application events
    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    // Spring's email sender
    private final JavaMailSender mailSender;

    // Reads sender email from application.properties
    @Value("${spms.mail.from:${spring.mail.username}}")
    private String fromAddress;

    // Sends OTP for login verification
    public void sendLoginOtp(String toEmail, String code) {
        send(toEmail,
                "Your SPMS login verification code",
                "Your login code is: " + code + "\nIt expires in 30 minutes.");
    }

    // Sends OTP for password reset
    public void sendPasswordResetOtp(String toEmail, String code) {
        send(toEmail,
                "Your SPMS password reset code",
                "Your password reset code is: " + code + "\nIt expires in 30 minutes.");
    }

    // Common method that creates and sends an email
    private void send(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            // Helps build the email message
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(fromAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body);

            // Sends the email
            mailSender.send(message);

        } catch (Exception ex) {

            // Logs an error if sending fails
            log.error("Failed to send email to {}", to, ex);
        }
    }
}