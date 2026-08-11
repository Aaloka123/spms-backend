package com.spms.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequestDTO {

    @NotBlank(message = "First name is required.")
    @Size(max = 100, message = "First name cannot exceed 100 characters.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    @Size(max = 100, message = "Last name cannot exceed 100 characters.")
    private String lastName;

    @NotBlank(message = "Username is required.")
    @Size(max = 50, message = "Username cannot exceed 50 characters.")
    private String username;

    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Password is required.")
    @Size(min = 8, message = "Password must be at least 8 characters.")
    private String password;

    @Size(max = 20, message = "Phone number cannot exceed 20 characters.")
    private String phoneNumber;

    @Size(max = 255, message = "Address cannot exceed 255 characters.")
    private String address;
}
