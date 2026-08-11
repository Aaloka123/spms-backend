package com.spms.config;

import com.spms.auth.entity.Role;
import com.spms.auth.entity.User;
import com.spms.auth.repository.RoleRepository;
import com.spms.auth.repository.UserRepository;
import com.spms.constants.Roles;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Ensures exactly one usable bootstrap ADMIN from .env.
 * - If ADMIN_USERNAME exists → sync password/email/role from .env
 * - Else if some other ADMIN exists → reclaim that account to .env credentials
 * - Else create a new ADMIN
 */
@Component
@Order(2)
@RequiredArgsConstructor
@Transactional(transactionManager = "authTransactionManager")
public class AdminDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_EMAIL:mednexusnepal@gmail.com}")
    private String adminEmail;

    @Value("${ADMIN_USERNAME:admin}")
    private String adminUsername;

    @Value("${ADMIN_PASSWORD:}")
    private String adminPassword;

    @Override
    public void run(String @NonNull ... args) {
        if (adminPassword == null || adminPassword.isBlank()) {
            System.out.println("ADMIN_PASSWORD not set — skipping admin user seed.");
            return;
        }

        String username = adminUsername.trim();
        String email = adminEmail.trim().toLowerCase();

        Role adminRole = roleRepository.findByRoleName(Roles.ADMIN)
                .orElseThrow(() -> new IllegalStateException(
                        "ADMIN role not found. RoleDataInitializer must run first."));

        // 1) Preferred: user already has ADMIN_USERNAME
        var byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            syncAdmin(byUsername.get(), username, email, adminRole);
            System.out.println("Updated ADMIN from .env — login with username: " + username);
            return;
        }

        // 2) Reclaim an existing ADMIN under a different username
        List<User> existingAdmins = userRepository.findByRole_RoleName(Roles.ADMIN);
        if (!existingAdmins.isEmpty()) {
            User admin = existingAdmins.getFirst();
            String oldUsername = admin.getUsername();
            syncAdmin(admin, username, email, adminRole);
            System.out.println(
                    "Reclaimed ADMIN '" + oldUsername + "' → username '" + username
                            + "'. Login with that username and ADMIN_PASSWORD from .env");
            return;
        }

        // 3) Create new ADMIN (email must be free)
        if (userRepository.existsByEmail(email)) {
            System.out.println(
                    "ADMIN_EMAIL already used by another user. Skipping admin seed: " + email);
            return;
        }

        User admin = new User();
        admin.setFirstName("System");
        admin.setLastName("Admin");
        syncAdmin(admin, username, email, adminRole);
        userRepository.save(admin);
        System.out.println("Seeded default ADMIN user: " + username);
    }

    private void syncAdmin(User admin, String username, String email, Role adminRole) {
        admin.setUsername(username);

        // Only change email when free or already owned by this user
        if (admin.getEmail() == null
                || !admin.getEmail().equalsIgnoreCase(email)) {
            var emailOwner = userRepository.findByEmail(email);
            if (emailOwner.isEmpty() || emailOwner.get().getId().equals(admin.getId())) {
                admin.setEmail(email);
            } else {
                System.out.println(
                        "Keeping existing email for ADMIN (ADMIN_EMAIL already taken): "
                                + admin.getEmail());
            }
        }

        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(adminRole);
        admin.setEnabled(true);
        if (admin.getFirstName() == null || admin.getFirstName().isBlank()) {
            admin.setFirstName("System");
        }
        admin.setLastName(
                admin.getLastName() == null || admin.getLastName().isBlank()
                        ? "Admin"
                        : admin.getLastName());
        userRepository.save(admin);
    }
}
