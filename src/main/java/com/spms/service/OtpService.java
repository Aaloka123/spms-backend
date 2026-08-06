package com.spms.service;

import com.spms.auth.entity.User;
import com.spms.dto.response.PendingOtpResponseDTO;

// Interface = list of OTP methods (no logic here)
public interface OtpService {

    // After password is correct: create LOGIN OTP + send email
    PendingOtpResponseDTO issueLoginOtp(Long userId, String email);

    // Check LOGIN OTP, delete it, return the user
    User verifyAndConsumeForLogin(String otpToken, String code);

    // Forgot password: create PASSWORD_RESET OTP + send email
    PendingOtpResponseDTO issuePasswordResetOtp(Long userId, String email);

    // Check PASSWORD_RESET OTP, delete it, return the user
    User verifyAndConsumeForPasswordReset(String otpToken, String code);
}