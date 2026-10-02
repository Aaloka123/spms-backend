package com.spms.service.impl;

import com.spms.app.entity.Vendor;
import com.spms.app.repository.VendorRepository;
import com.spms.auth.entity.User;
import com.spms.auth.repository.UserRepository;
import com.spms.dto.response.UserReportDTO;
import com.spms.dto.response.VendorReportDTO;
import com.spms.exception.UserNotFoundException;
import com.spms.exception.VendorNotFoundException;
import com.spms.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Builds Admin reports for users and vendors
@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;

    // Users are in auth DB
    @Override
    @Transactional(transactionManager = "authTransactionManager", readOnly = true)
    public List<UserReportDTO> getUserReports() {
        return userRepository.findAll().stream()
                .map(this::toUserReport)
                .toList();
    }

    @Override
    @Transactional(transactionManager = "authTransactionManager", readOnly = true)
    public UserReportDTO getUserReportById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(
                        "User not found with id: " + userId));
        return toUserReport(user);
    }

    // Vendors are in app DB
    @Override
    @Transactional(transactionManager = "appTransactionManager", readOnly = true)
    public List<VendorReportDTO> getVendorReports() {
        return vendorRepository.findAll().stream()
                .map(this::toVendorReport)
                .toList();
    }

    @Override
    @Transactional(transactionManager = "appTransactionManager", readOnly = true)
    public VendorReportDTO getVendorReportById(Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new VendorNotFoundException(
                        "Vendor not found with id: " + vendorId));
        return toVendorReport(vendor);
    }

    // Convert User entity -> UserReportDTO
    private UserReportDTO toUserReport(User user) {
        String firstName = user.getFirstName() != null ? user.getFirstName().trim() : "";
        String lastName = user.getLastName() != null ? user.getLastName().trim() : "";
        String fullName = (firstName + " " + lastName).trim();
        if (fullName.isEmpty()) {
            fullName = user.getUsername() != null ? user.getUsername() : "N/A";
        }

        String roleName = (user.getRole() != null && user.getRole().getRoleName() != null)
                ? user.getRole().getRoleName()
                : "N/A";

        return new UserReportDTO(
                user.getId(),
                user.getUsername(),
                fullName,
                user.getEmail(),
                roleName,
                user.getEnabled()
        );
    }

    // Convert Vendor entity -> VendorReportDTO
    private VendorReportDTO toVendorReport(Vendor vendor) {
        return new VendorReportDTO(
                vendor.getId(),
                vendor.getVendorName() != null ? vendor.getVendorName() : "N/A",
                vendor.getContactPerson() != null ? vendor.getContactPerson() : "N/A",
                vendor.getEmail(),
                vendor.getPhoneNumber(),
                vendor.getIsActive(),
                vendor.getCreatedBy()
        );
    }
}