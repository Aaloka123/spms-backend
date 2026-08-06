package com.spms.service;

import com.spms.dto.request.ForgotPasswordRequestDTO;
import com.spms.dto.request.LoginRequestDTO;
import com.spms.dto.request.ResetPasswordRequestDTO;
import com.spms.dto.request.VerifyOtpRequestDTO;
import com.spms.dto.response.LoginResponseDTO;
import com.spms.dto.response.PendingOtpResponseDTO;

import java.util.Map;

// Auth service contract (methods only, no logic)
public interface AuthService {

    // Step 1: username + password OK → send OTP (NO JWT yet)
    PendingOtpResponseDTO login(LoginRequestDTO loginRequestDTO);

    // Step 2: otpToken + code OK → return JWT
    LoginResponseDTO verifyOtp(VerifyOtpRequestDTO request);

    // Forgot password: send PASSWORD_RESET OTP to email
    PendingOtpResponseDTO forgotPassword(ForgotPasswordRequestDTO request);

    // Reset password using OTP, then save new password
    Map<String, String> resetPassword(ResetPasswordRequestDTO request);
}