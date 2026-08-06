package com.spms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PendingOtpResponseDTO {
    private boolean otpRequired;
    private String otpToken;
    private String maskedEmail;
    private String message;
}