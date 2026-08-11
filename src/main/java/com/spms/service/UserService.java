package com.spms.service;

import com.spms.dto.request.RegisterRequestDTO;
import com.spms.dto.request.UserRequestDTO;
import com.spms.dto.response.UserResponseDTO;

import java.util.List;

public interface UserService {

    // Create a new user (public signup)
    UserResponseDTO createUser(RegisterRequestDTO requestDTO);

    // Get all users
    List<UserResponseDTO> getAllUsers();

    // Get user by ID
    UserResponseDTO getUserById(Long id);

    // Update user
    UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO);

    // Delete user
    void deleteUser(Long id);
}