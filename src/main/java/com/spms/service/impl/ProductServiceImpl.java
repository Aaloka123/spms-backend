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
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

// Service implementation for Product operations.
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO requestDTO) {

        // Check product name
        if (productRepository.existsByProductName(requestDTO.getProductName())) {
            throw new ProductAlreadyExistsException(requestDTO.getProductName());
        }

        // Convert DTO to Entity
        Product product = productMapper.toEntity(requestDTO);

        // Save product
        Product savedProduct = productRepository.save(product);

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

        // PageRequest.of(0, limit):
        // 0 = first page
        // limit = how many products to return (e.g. 4)
        return productMapper.toResponseDTOList(
                productRepository.findByIsActiveTrueOrderByCreatedAtDesc(
                        PageRequest.of(0, limit)));
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
    public ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO) {

        // Find product by ID
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() ->
                        new ProductNotFoundException(
                                "Product not found with id: " + id));

        // Check product name
        if (productRepository.existsByProductNameAndIdNot(requestDTO.getProductName(), id)) {
            throw new ProductAlreadyExistsException(requestDTO.getProductName());
        }

        // Update entity using mapper
        productMapper.updateEntityFromDTO(requestDTO, existingProduct);

        // Save updated product
        Product updatedProduct = productRepository.save(existingProduct);

        // Return response DTO
        return productMapper.toResponseDTO(updatedProduct);
    }

    @Override
public void deleteProduct(Long id) {

    Product product = productRepository.findById(id)
            .orElseThrow(() ->
                    new ProductNotFoundException(
                            "Product not found with id: " + id));

    // Soft delete: keep row, hide from public list
    product.setIsActive(false);
    productRepository.save(product);
}

}
