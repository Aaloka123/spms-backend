package com.spms.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyOtpRequestDTO {

    @NotBlank
    private String otpToken;

    /** 6-digit code; spaces allowed (stripped server-side), e.g. "1 2 3 4 5 6" or "123456" */
    @NotBlank
    private String code;
}