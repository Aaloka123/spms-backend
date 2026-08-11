package com.spms.service.impl;

import com.spms.dto.request.RegisterRequestDTO;
import com.spms.dto.request.UserRequestDTO;
import com.spms.dto.response.UserResponseDTO;
import com.spms.auth.entity.Role;
import com.spms.auth.entity.User;
import com.spms.exception.EmailAlreadyExistsException;
import com.spms.exception.PhoneNumberAlreadyExistsException;
import com.spms.exception.RoleNotFoundException;
import com.spms.exception.UserNotFoundException;
import com.spms.exception.UsernameAlreadyExistsException;
import com.spms.mapper.UserMapper;
import com.spms.auth.repository.RoleRepository;
import com.spms.auth.repository.UserRepository;
import com.spms.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Service implementation for User operations.
@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "authTransactionManager")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // Create a new user.
    @Override
    public UserResponseDTO createUser(RegisterRequestDTO requestDTO) {

        // Check username
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new UsernameAlreadyExistsException(requestDTO.getUsername());
        }

        // Check email
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new EmailAlreadyExistsException(requestDTO.getEmail());
        }

        // Check phone number
        if (requestDTO.getPhoneNumber() != null
                && userRepository.existsByPhoneNumber(requestDTO.getPhoneNumber())) {

            throw new PhoneNumberAlreadyExistsException(requestDTO.getPhoneNumber());
        }

        // Public signup always gets USER role
        Role role = roleRepository.findByRoleName("USER")
                .orElseThrow(() ->
                        new RoleNotFoundException(
                                "USER role not found. Please seed roles first."));

        // Convert DTO to Entity
        User user = userMapper.toEntity(requestDTO);

        // Assign role
        user.setRole(role);

        // New accounts are enabled by default
        user.setEnabled(true);

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        // Save user
        User savedUser = userRepository.save(user);

        // Return response
        return userMapper.toResponseDTO(savedUser);
    }

    // Retrieve all users.
    @Override
    public List<UserResponseDTO> getAllUsers() {

        return userMapper.toResponseDTOList(userRepository.findAll());
    }

    // Retrieve a user by ID.
    @Override
    public UserResponseDTO getUserById(Long id) {

        // Find user by ID
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id));

        // Return response DTO
        return userMapper.toResponseDTO(user);
    }

    // Update an existing user.
    @Override
    public UserResponseDTO updateUser(Long id, UserRequestDTO requestDTO) {

        // Find user by ID
        User existingUser = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id));

        // Check username
        if (userRepository.existsByUsernameAndIdNot(requestDTO.getUsername(), id)) {
            throw new UsernameAlreadyExistsException(requestDTO.getUsername());
        }

        // Check email
        if (userRepository.existsByEmailAndIdNot(requestDTO.getEmail(), id)) {
            throw new EmailAlreadyExistsException(requestDTO.getEmail());
        }

        // Check phone number
        if (requestDTO.getPhoneNumber() != null
                && userRepository.existsByPhoneNumberAndIdNot(requestDTO.getPhoneNumber(), id)) {

            throw new PhoneNumberAlreadyExistsException(requestDTO.getPhoneNumber());
        }

        // Find role
        Role role = roleRepository.findById(requestDTO.getRoleId())
                .orElseThrow(() ->
                        new RoleNotFoundException(
                                "Role not found with id: " + requestDTO.getRoleId()));

        // Update entity using mapper
        userMapper.updateEntityFromDTO(requestDTO, existingUser);

        // Assign role
        existingUser.setRole(role);

        // Encode password before saving
        existingUser.setPassword(passwordEncoder.encode(requestDTO.getPassword()));

        // Save updated user
        User updatedUser = userRepository.save(existingUser);

        // Return response
        return userMapper.toResponseDTO(updatedUser);
    }

    // Delete a user.
    @Override
    public void deleteUser(Long id) {

        // Find user by ID
        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                "User not found with id: " + id));

        // Delete user
        userRepository.delete(user);
    }
}