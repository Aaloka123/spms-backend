package com.spms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequestDTO {

    @NotBlank
    private String otpToken;

    @NotBlank
    @Pattern(regexp = "\\d{6}", message = "Code must be 6 digits")
    private String code;
}