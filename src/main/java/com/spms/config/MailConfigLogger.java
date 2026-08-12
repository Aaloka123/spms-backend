package com.spms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Logs (masked) mail settings after Spring starts so you can confirm .env loaded.
 */
@Component
@Order(50)
public class MailConfigLogger implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(MailConfigLogger.class);

    @Value("${spring.mail.username:}")
    private String mailUsername;

    @Value("${spms.mail.from:}")
    private String mailFrom;

    @Value("${spms.mail.from-name:MedNexus}")
    private String mailFromName;

    @Override
    public void run(ApplicationArguments args) {
        log.info("Mail SMTP user: {}", mask(mailUsername));
        log.info("Mail From: {} <{}>", mailFromName, mask(mailFrom));
    }

    private static String mask(String email) {
        if (email == null || email.isBlank()) {
            return "(not set)";
        }
        int at = email.indexOf('@');
        if (at <= 0) {
            return "(set)";
        }
        return email.charAt(0) + "***" + email.substring(at);
    }
}
