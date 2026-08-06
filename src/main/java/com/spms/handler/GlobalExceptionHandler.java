package com.spms.handler;

import com.spms.exception.*;
import org.springframework.security.access.AccessDeniedException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice // Handles exceptions globally for all controllers
public class GlobalExceptionHandler {

    // Handles UserNotFoundException
    @ExceptionHandler(UserNotFoundException.class)
    public ProblemDetail handleUserNotFound(UserNotFoundException ex,
                                            HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 404 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        // Set short title of the error
        problemDetail.setTitle("User Not Found");

        // Set detailed error message
        problemDetail.setDetail(ex.getMessage());

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add custom properties
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }

    // Handles RoleNotFoundException
    @ExceptionHandler(RoleNotFoundException.class)
    public ProblemDetail handleRoleNotFound(RoleNotFoundException ex,
                                            HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 404 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        // Set short title of the error
        problemDetail.setTitle("Role Not Found");

        // Set detailed error message
        problemDetail.setDetail(ex.getMessage());

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add custom properties
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }
    // Handles ProductNotFoundException
    @ExceptionHandler(ProductNotFoundException.class)
    public ProblemDetail handleProductNotFound(ProductNotFoundException ex,
                                               HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 404 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);

        // Set short title of the error
        problemDetail.setTitle("Product Not Found");

        // Set detailed error message
        problemDetail.setDetail(ex.getMessage());

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add custom properties
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }

    // Handles UsernameAlreadyExistsException
    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ProblemDetail handleUsernameAlreadyExists(UsernameAlreadyExistsException ex,
                                                     HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 409 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        // Set short title of the error
        problemDetail.setTitle("Username Already Exists");

        // Set detailed error message
        problemDetail.setDetail(ex.getMessage());

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add custom properties
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }

    // Handles EmailAlreadyExistsException
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ProblemDetail handleEmailAlreadyExists(EmailAlreadyExistsException ex,
                                                  HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 409 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        // Set short title of the error
        problemDetail.setTitle("Email Already Exists");

        // Set detailed error message
        problemDetail.setDetail(ex.getMessage());

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add custom properties
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }

    // Handles PhoneNumberAlreadyExistsException
    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ProblemDetail handlePhoneNumberAlreadyExists(PhoneNumberAlreadyExistsException ex,
                                                        HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 409 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        // Set short title of the error
        problemDetail.setTitle("Phone Number Already Exists");

        // Set detailed error message
        problemDetail.setDetail(ex.getMessage());

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add custom properties
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }
    // Handles ProductAlreadyExistsException
    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ProblemDetail handleProductAlreadyExists(ProductAlreadyExistsException ex,
                                                    HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 409 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        // Set short title of the error
        problemDetail.setTitle("Product Already Exists");

        // Set detailed error message
        problemDetail.setDetail(ex.getMessage());

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add custom properties
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }

    // Handles InvalidRoleNameException
    @ExceptionHandler(InvalidRoleNameException.class)
    public ProblemDetail handleInvalidRoleName(InvalidRoleNameException ex,
                                               HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 400 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        // Set short title of the error
        problemDetail.setTitle("Invalid Role Name");

        // Set detailed error message
        problemDetail.setDetail(ex.getMessage());

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add custom properties
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }

    // Handles RoleAlreadyExistsException
    @ExceptionHandler(RoleAlreadyExistsException.class)
    public ProblemDetail handleRoleAlreadyExists(RoleAlreadyExistsException ex,
                                                 HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 409 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        // Set short title of the error
        problemDetail.setTitle("Role Already Exists");

        // Set detailed error message
        problemDetail.setDetail(ex.getMessage());

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add custom properties
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }

    // Handles RoleInUseException
    @ExceptionHandler(RoleInUseException.class)
    public ProblemDetail handleRoleInUse(RoleInUseException ex,
                                         HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 409 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);

        // Set short title of the error
        problemDetail.setTitle("Role In Use");

        // Set detailed error message
        problemDetail.setDetail(ex.getMessage());

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add custom properties
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }

    // Handles validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex,
                                                HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 400 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);

        // Set short title of the error
        problemDetail.setTitle("Validation Failed");

        // Set general error message
        problemDetail.setDetail("One or more fields contain invalid values.");

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Store validation errors
        Map<String, String> errors = new HashMap<>();

        // Extract field name + error message
        ex.getBindingResult().getAllErrors().forEach(error -> {

            String fieldName;
            if (error instanceof FieldError fieldError) {
                fieldName = fieldError.getField();
            } else {
                fieldName = error.getObjectName();
            }
            String message = error.getDefaultMessage();

            errors.put(fieldName, message);
        });

        // Add validation errors as custom property
        problemDetail.setProperty("errors", errors);

        // Add timestamp
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }
    // Handles AccessDeniedException (403 Forbidden)
    @ExceptionHandler(AccessDeniedException.class)
    public ProblemDetail handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 403 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.FORBIDDEN);

        // Set short title of the error
        problemDetail.setTitle("Access Denied");

        // Set detailed error message
        problemDetail.setDetail("You do not have permission to access this resource.");

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add timestamp
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }

    // Handles all unexpected exceptions (Fallback)
    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGenericException(Exception ex,
                                                HttpServletRequest request) {

        // Create ProblemDetail object with HTTP 500 status
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);

        // Set short title of the error
        problemDetail.setTitle("Internal Server Error");

        // Set detailed error message
        problemDetail.setDetail(ex.getMessage());

        // Set request URI where the error occurred
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));

        // Add timestamp
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        // Return ProblemDetail
        return problemDetail;
    }
    // Wrong username or password at login
    @ExceptionHandler(InvalidCredentialsException.class)
    public ProblemDetail handleInvalidCredentials(
            InvalidCredentialsException ex,
            HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Authentication Failed");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }

    // JWT token expired
    @ExceptionHandler(TokenExpiredException.class)
    public ProblemDetail handleTokenExpired(
            TokenExpiredException ex,
            HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Token Expired");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }

    // Invalid or malformed JWT token
    @ExceptionHandler(InvalidTokenException.class)
    public ProblemDetail handleInvalidToken(
            InvalidTokenException ex,
            HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Invalid Token");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }

    // No token on protected endpoint
    @ExceptionHandler(InsufficientAuthenticationException.class)
    public ProblemDetail handleInsufficientAuthentication(
            InsufficientAuthenticationException ex,
            HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.UNAUTHORIZED);
        problemDetail.setTitle("Unauthorized");
        problemDetail.setDetail("Authentication token is missing or invalid.");
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }
    // Handles VendorNotFoundException (404)
    @ExceptionHandler(VendorNotFoundException.class)
    public ProblemDetail handleVendorNotFound(VendorNotFoundException ex,
                                              HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setTitle("Vendor Not Found");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }

    // Handles VendorAlreadyExistsException (409)
    @ExceptionHandler(VendorAlreadyExistsException.class)
    public ProblemDetail handleVendorAlreadyExists(VendorAlreadyExistsException ex,
                                                   HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
        problemDetail.setTitle("Vendor Already Exists");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    }
    // Wrong / expired / invalid OTP code
    @ExceptionHandler(InvalidOtpException.class)
    public ProblemDetail handleInvalidOtp(
            InvalidOtpException ex,
            HttpServletRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Invalid OTP");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(java.net.URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", LocalDateTime.now());

        return problemDetail;
    }

}