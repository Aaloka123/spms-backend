package com.spms.controller;

import com.spms.constants.ApiPath;
import com.spms.dto.request.RegisterRequestDTO;
import com.spms.dto.request.UserRequestDTO;
import com.spms.dto.response.UserResponseDTO;
import com.spms.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPath.USERS)
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    //Public registration
    // Create a new user
    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(
            @Valid @RequestBody RegisterRequestDTO requestDTO) {

        UserResponseDTO response = userService.createUser(requestDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Admin only
    // Get all users
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {

        return ResponseEntity.ok(userService.getAllUsers());
    }

    //ADMIN only
    // Get user by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id) {

        return ResponseEntity.ok(userService.getUserById(id));
    }

    //Only Admin
    // Update user
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO requestDTO) {

        return ResponseEntity.ok(userService.updateUser(id, requestDTO));
    }

    //Only Admin
    // Delete user
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        userService.deleteUser(id);

        return ResponseEntity.ok("User deleted successfully.");
    }

}