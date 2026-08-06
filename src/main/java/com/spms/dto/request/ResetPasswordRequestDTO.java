package com.spms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestDTO {

    @NotBlank
    private String otpToken;

    @NotBlank
    @Pattern(regexp = "\\d{6}")
    private String code;

    @NotBlank
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String newPassword;
}