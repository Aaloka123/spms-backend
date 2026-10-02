package com.spms.service.impl;

import com.spms.app.entity.Vendor;
import com.spms.app.repository.VendorRepository;
import com.spms.dto.request.VendorRequestDTO;
import com.spms.dto.response.VendorResponseDTO;
import com.spms.exception.VendorAlreadyExistsException;
import com.spms.exception.VendorNotFoundException;
import com.spms.mapper.VendorMapper;
import com.spms.security.custom.CustomUserDetails;
import com.spms.service.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Implements vendor business logic
@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "appTransactionManager", readOnly = true)
public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;

    @Override
    @Transactional(transactionManager = "appTransactionManager")
    public VendorResponseDTO createVendor(VendorRequestDTO requestDTO) {

        String vendorName = requestDTO.getVendorName() != null ? requestDTO.getVendorName().trim() : "";
        String email = requestDTO.getEmail() != null ? requestDTO.getEmail().trim() : "";
        String phone = requestDTO.getPhoneNumber() != null ? requestDTO.getPhoneNumber().trim() : "";

        // Check duplicates
        if (vendorRepository.existsByVendorName(vendorName)) {
            throw new VendorAlreadyExistsException(
                    "Vendor already exists with name: " + vendorName);
        }
        if (vendorRepository.existsByEmail(email)) {
            throw new VendorAlreadyExistsException(
                    "Vendor already exists with email: " + email);
        }
        if (vendorRepository.existsByPhoneNumber(phone)) {
            throw new VendorAlreadyExistsException(
                    "Vendor already exists with phone: " + phone);
        }

        // Convert DTO to entity
        Vendor vendor = vendorMapper.toEntity(requestDTO);
        vendor.setVendorName(vendorName);
        vendor.setEmail(email);
        vendor.setPhoneNumber(phone);
        vendor.setIsActive(true);
        vendor.setCreatedBy(getCurrentUserId()); // save auth user id as Long

        // Save and return
        Vendor saved = vendorRepository.save(vendor);
        return vendorMapper.toResponseDTO(saved);
    }

    @Override
    public List<VendorResponseDTO> getAllVendors() {
        return vendorMapper.toResponseDTOList(vendorRepository.findAll());
    }

    @Override
    public VendorResponseDTO getVendorById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException(
                        "Vendor not found with id: " + id));
        return vendorMapper.toResponseDTO(vendor);
    }

    @Override
    @Transactional(transactionManager = "appTransactionManager")
    public VendorResponseDTO updateVendor(Long id, VendorRequestDTO requestDTO) {
        Vendor existing = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException(
                        "Vendor not found with id: " + id));

        String vendorName = requestDTO.getVendorName() != null ? requestDTO.getVendorName().trim() : "";
        String email = requestDTO.getEmail() != null ? requestDTO.getEmail().trim() : "";
        String phone = requestDTO.getPhoneNumber() != null ? requestDTO.getPhoneNumber().trim() : "";

        // Check duplicates for other vendors
        if (vendorRepository.existsByVendorNameAndIdNot(vendorName, id)) {
            throw new VendorAlreadyExistsException(
                    "Vendor already exists with name: " + vendorName);
        }
        if (vendorRepository.existsByEmailAndIdNot(email, id)) {
            throw new VendorAlreadyExistsException(
                    "Vendor already exists with email: " + email);
        }
        if (vendorRepository.existsByPhoneNumberAndIdNot(phone, id)) {
            throw new VendorAlreadyExistsException(
                    "Vendor already exists with phone: " + phone);
        }

        // Update fields
        vendorMapper.updateEntityFromDTO(requestDTO, existing);
        existing.setVendorName(vendorName);
        existing.setEmail(email);
        existing.setPhoneNumber(phone);

        Vendor updated = vendorRepository.save(existing);
        return vendorMapper.toResponseDTO(updated);
    }

    @Override
    @Transactional(transactionManager = "appTransactionManager")
    public void deleteVendor(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new VendorNotFoundException(
                        "Vendor not found with id: " + id));
        vendorRepository.delete(vendor);
    }

    // Get logged-in user id from JWT/security context
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getUserId();
        }
        return null;
    }
}