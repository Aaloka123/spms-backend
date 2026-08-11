package com.spms.auth.repository;

import com.spms.auth.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Check if username already exists
    boolean existsByUsername(String username);

    // Check if email already exists
    boolean existsByEmail(String email);

    // Check if phone number already exists
    boolean existsByPhoneNumber(String phoneNumber);

    // Check username excluding current user
    boolean existsByUsernameAndIdNot(String username, Long id);

    // Check email excluding current user
    boolean existsByEmailAndIdNot(String email, Long id);

    // Check phone number excluding current user
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Long id);

    //Forgot-password looks up user by email
    @EntityGraph(attributePaths = "role")
    Optional<User> findByEmail(String email);

    // Finds a user by username for login authentication
   @EntityGraph(attributePaths = "role")
   Optional<User> findByUsername(String username);
    // Check if any user is assigned to a role
    boolean existsByRole_RoleId(Long roleId);

    // Check if any user has a given role name (e.g. ADMIN)
    boolean existsByRole_RoleName(String roleName);

    // Find users with a given role name
    @EntityGraph(attributePaths = "role")
    java.util.List<User> findByRole_RoleName(String roleName);
}