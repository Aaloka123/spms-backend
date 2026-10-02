package com.spms.service.impl;

import com.spms.constants.Roles;
import com.spms.dto.request.RoleRequestDTO;
import com.spms.dto.response.RoleResponseDTO;
import com.spms.auth.entity.Role;
import com.spms.exception.InvalidRoleNameException;
import com.spms.exception.RoleAlreadyExistsException;
import com.spms.exception.RoleInUseException;
import com.spms.exception.RoleNotFoundException;
import com.spms.mapper.RoleMapper;
import com.spms.auth.repository.RoleRepository;
import com.spms.auth.repository.UserRepository;
import com.spms.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Service implementation for Role operations.
@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "authTransactionManager", readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;
    private final UserRepository userRepository;

    // Create a new role.
    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public RoleResponseDTO saveRole(RoleRequestDTO roleRequestDTO) {

        String trimmedRoleName = roleRequestDTO.getRoleName() != null ? roleRequestDTO.getRoleName().trim() : "";
        validateAllowedRoleName(trimmedRoleName);

        // Check role name
        if (roleRepository.existsByRoleName(trimmedRoleName)) {
            throw new RoleAlreadyExistsException(trimmedRoleName);
        }

        // Convert DTO to Entity using MapStruct
        Role role = roleMapper.toEntity(roleRequestDTO);
        role.setRoleName(trimmedRoleName);

        // Save Entity
        Role savedRole = roleRepository.save(role);

        // Convert Entity to Response DTO
        return roleMapper.toResponseDTO(savedRole);
    }

    // Retrieve all roles.
    @Override
    public List<RoleResponseDTO> getAllRoles() {

        return roleMapper.toResponseDTOList(roleRepository.findAll());
    }

    // Retrieve a role by ID.
    @Override
    public RoleResponseDTO getRoleById(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new RoleNotFoundException(
                                "Role not found with id: " + id));

        return roleMapper.toResponseDTO(role);
    }

    // Update an existing role.
    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public RoleResponseDTO updateRole(Long id, RoleRequestDTO roleRequestDTO) {

        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() ->
                        new RoleNotFoundException(
                                "Role not found with id: " + id));

        String trimmedRoleName = roleRequestDTO.getRoleName() != null ? roleRequestDTO.getRoleName().trim() : "";
        validateAllowedRoleName(trimmedRoleName);

        // Check role name
        if (roleRepository.existsByRoleNameAndRoleIdNot(trimmedRoleName, id)) {
            throw new RoleAlreadyExistsException(trimmedRoleName);
        }

        // Update entity using mapper
        roleMapper.updateEntityFromDTO(roleRequestDTO, existingRole);
        existingRole.setRoleName(trimmedRoleName);

        // Save updated entity
        Role updatedRole = roleRepository.save(existingRole);

        // Convert Entity to Response DTO
        return roleMapper.toResponseDTO(updatedRole);
    }

    // Delete a role.
    @Override
    @Transactional(transactionManager = "authTransactionManager")
    public void deleteRole(Long id) {

        Role role = roleRepository.findById(id)
                .orElseThrow(() ->
                        new RoleNotFoundException(
                                "Role not found with id: " + id));

        // Check if role is assigned to users
        if (userRepository.existsByRole_RoleId(id)) {
            throw new RoleInUseException(id);
        }

        roleRepository.delete(role);
    }

    private void validateAllowedRoleName(String roleName) {
        String trimmed = (roleName != null) ? roleName.trim() : "";
        if (!Roles.ADMIN.equals(trimmed)
                && !Roles.PHARMACIST.equals(trimmed)
                && !Roles.USER.equals(trimmed)) {
            throw new InvalidRoleNameException(roleName);
        }
    }
}
