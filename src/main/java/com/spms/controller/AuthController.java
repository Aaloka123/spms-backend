package com.spms.controller;

import com.spms.constants.ApiPath;
import com.spms.dto.request.ForgotPasswordRequestDTO;
import com.spms.dto.request.LoginRequestDTO;
import com.spms.dto.request.ResetPasswordRequestDTO;
import com.spms.dto.request.VerifyOtpRequestDTO;
import com.spms.dto.response.LoginResponseDTO;
import com.spms.dto.response.PendingOtpResponseDTO;
import com.spms.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Handles auth APIs under /api/auth
@RestController
@RequestMapping(ApiPath.AUTH)
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // Step 1 login:
    // username + password → send OTP email
    // returns otpToken (NO JWT yet)
    @PostMapping("/login")
    public ResponseEntity<PendingOtpResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {

        return ResponseEntity.ok(authService.login(dto));
    }

    // Step 2 verify OTP:
    // otpToken + 6-digit code → return JWT
    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponseDTO> verifyOtp(
            @Valid @RequestBody VerifyOtpRequestDTO dto) {

        return ResponseEntity.ok(authService.verifyOtp(dto));
    }

    // Forgot password:
    // email → send PASSWORD_RESET OTP
    @PostMapping("/forgot-password")
    public ResponseEntity<PendingOtpResponseDTO> forgotPassword(
            @Valid @RequestBody ForgotPasswordRequestDTO dto) {

        return ResponseEntity.ok(authService.forgotPassword(dto));
    }

    // Reset password:
    // otpToken + code + newPassword → update password
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(
            @Valid @RequestBody ResetPasswordRequestDTO dto) {

        return ResponseEntity.ok(authService.resetPassword(dto));
    }
}