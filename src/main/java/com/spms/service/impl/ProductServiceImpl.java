package com.spms.service.impl;

import com.spms.dto.request.ProductRequestDTO;
import com.spms.dto.response.ProductResponseDTO;
import com.spms.app.entity.Product;
import com.spms.exception.ProductAlreadyExistsException;
import com.spms.exception.ProductNotFoundException;
import com.spms.mapper.ProductMapper;
import com.spms.app.repository.ProductRepository;
import com.spms.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Service implementation for Product operations.
@Service
@RequiredArgsConstructor
@Transactional(transactionManager = "appTransactionManager", readOnly = true)
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Transactional(transactionManager = "appTransactionManager")
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {

        String trimmedName = requestDTO.getProductName() != null ? requestDTO.getProductName().trim() : "";

        // Check product name
        if (productRepository.existsByProductName(trimmedName)) {
            throw new ProductAlreadyExistsException(trimmedName);
        }

        // Convert DTO to Entity
        Product product = productMapper.toEntity(requestDTO);
        product.setProductName(trimmedName);
        product.setIsActive(true);

        // Save product
        Product savedProduct = productRepository.save(product);
        log.info("Successfully created product with id: {} and name: '{}'", savedProduct.getId(), savedProduct.getProductName());

        // Return response
        return productMapper.toResponseDTO(savedProduct);
    }

    @Override
    public List<ProductResponseDTO> getAllProducts() {

        // Only return active products for the public Home page
        return productMapper.toResponseDTOList(
                productRepository.findByIsActiveTrue());
    }

    @Override
    public List<ProductResponseDTO> getAllProductsForAdmin() {
        // Admin sees all products (active + inactive)
        return productMapper.toResponseDTOList(productRepository.findAll());
    }

    @Override
    public List<ProductResponseDTO> getNewArrivals(int limit) {

        // PageRequest requires page size >= 1
        int safeLimit = (limit <= 0) ? 4 : Math.min(limit, 100);

        return productMapper.toResponseDTOList(
                productRepository.findByIsActiveTrueOrderByCreatedAtDesc(
                        PageRequest.of(0, safeLimit)));
    }

    @Override
    public ProductResponseDTO getProductById(Long id) {

        // Find product by ID
        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id));

        // Return response DTO
        return productMapper.toResponseDTO(product);
    }

    @Override
    @Transactional(transactionManager = "appTransactionManager")
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {

        // Find product by ID
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id));

        String trimmedName = requestDTO.getProductName() != null ? requestDTO.getProductName().trim() : "";

        // Check product name
        if (productRepository.existsByProductNameAndIdNot(trimmedName, id)) {
            throw new ProductAlreadyExistsException(trimmedName);
        }

        // Update entity using mapper
        productMapper.updateEntityFromDTO(requestDTO, existingProduct);
        existingProduct.setProductName(trimmedName);

        // Save updated product
        Product updatedProduct = productRepository.save(existingProduct);
        log.info("Successfully updated product with id: {}", id);

        // Return response DTO
        return productMapper.toResponseDTO(updatedProduct);
    }

    @Override
    @Transactional(transactionManager = "appTransactionManager")
    public void deleteProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id));

        // Soft delete: keep row, hide from public list
        product.setIsActive(false);
        productRepository.save(product);
        log.info("Successfully soft-deleted product with id: {}", id);
    }
}
