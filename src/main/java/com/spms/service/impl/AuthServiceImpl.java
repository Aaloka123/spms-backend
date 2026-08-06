package com.spms.service.impl;

import com.spms.auth.entity.User;
import com.spms.auth.repository.UserRepository;
import com.spms.dto.request.ForgotPasswordRequestDTO;
import com.spms.dto.request.LoginRequestDTO;
import com.spms.dto.request.ResetPasswordRequestDTO;
import com.spms.dto.request.VerifyOtpRequestDTO;
import com.spms.dto.response.LoginResponseDTO;
import com.spms.dto.response.PendingOtpResponseDTO;
import com.spms.exception.InvalidCredentialsException;
import com.spms.exception.UserNotFoundException;
import com.spms.security.custom.CustomUserDetails;
import com.spms.security.jwt.JwtService;
import com.spms.service.AuthService;
import com.spms.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service // Marks this class as the authentication service
@RequiredArgsConstructor // Generates constructor for dependency injection
public class AuthServiceImpl implements AuthService {

    // Authenticates username and password
    private final AuthenticationManager authenticationManager;

    // Generates JWT tokens
    private final JwtService jwtService;

    // Handles OTP generation, verification, and email sending
    private final OtpService otpService;

    // Performs user database operations
    private final UserRepository userRepository;

    // Encrypts passwords using BCrypt
    private final PasswordEncoder passwordEncoder;

    // Validates user credentials and sends a login OTP
    @Override
    public PendingOtpResponseDTO login(LoginRequestDTO loginRequestDTO) {

        try {
            Authentication authentication = authenticationManager.authenticate(
                    UsernamePasswordAuthenticationToken.unauthenticated(
                            loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()
                    )
            );

            CustomUserDetails userDetails =
                    (CustomUserDetails) authentication.getPrincipal();

            User user = userDetails.getUser();

            // Generate and email the login OTP
            return otpService.issueLoginOtp(user.getId(), user.getEmail());

        } catch (BadCredentialsException ex) {
            throw new InvalidCredentialsException();
        }
    }

    // Verifies OTP and generates a JWT token
    @Override
    public LoginResponseDTO verifyOtp(VerifyOtpRequestDTO request) {

        // OtpService already returns a fully loaded user (with role)
        User fullUser = otpService.verifyAndConsumeForLogin(
                request.getOtpToken(),
                request.getCode()
        );

        CustomUserDetails details = new CustomUserDetails(fullUser);
        String accessToken = jwtService.generateToken(details);

        return new LoginResponseDTO(
                "Login successful",
                fullUser.getId(),
                fullUser.getUsername(),
                fullUser.getRole().getRoleName(),
                accessToken
        );
    }

    // Sends a password reset OTP to the user's email
    @Override
    public PendingOtpResponseDTO forgotPassword(ForgotPasswordRequestDTO request) {

        User user = userRepository.findByEmail(request.getEmail().trim())
                .orElseThrow(() ->
                        new UserNotFoundException("No account found with this email."));

        return otpService.issuePasswordResetOtp(user.getId(), user.getEmail());
    }

    // Verifies OTP and updates the user's password
    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public Map<String, String> resetPassword(ResetPasswordRequestDTO request) {

        User user = otpService.verifyAndConsumeForPasswordReset(
                request.getOtpToken(),
                request.getCode()
        );

        // Encrypt and save the new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return Map.of("message", "Password updated successfully. You can now log in.");
    }
}