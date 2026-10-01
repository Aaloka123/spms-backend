package com.spms.controller;

import com.spms.constants.ApiPath;
import com.spms.dto.request.RoleRequestDTO;
import com.spms.dto.response.RoleResponseDTO;
import com.spms.service.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPath.ROLES)
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    // Create a new role (ADMIN only)
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleResponseDTO> createRole(
            @Valid @RequestBody RoleRequestDTO roleRequestDTO) {

        RoleResponseDTO savedRole = roleService.saveRole(roleRequestDTO);

        return new ResponseEntity<>(savedRole, HttpStatus.CREATED);
    }

    // Retrieve all roles
    @GetMapping
    public ResponseEntity<List<RoleResponseDTO>> getAllRoles() {

        List<RoleResponseDTO> roles = roleService.getAllRoles();

        return ResponseEntity.ok(roles);
    }

    // Retrieve a role by its ID
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponseDTO> getRoleById(@PathVariable Long id) {

        RoleResponseDTO role = roleService.getRoleById(id);

        return ResponseEntity.ok(role);
    }

    // Update an existing role (ADMIN only)
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<RoleResponseDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleRequestDTO roleRequestDTO) {

        RoleResponseDTO updatedRole = roleService.updateRole(id, roleRequestDTO);

        return ResponseEntity.ok(updatedRole);
    }

    // Delete a role by its ID (ADMIN only)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteRole(@PathVariable Long id) {

        roleService.deleteRole(id);

        return ResponseEntity.ok("Role deleted successfully.");
    }
}