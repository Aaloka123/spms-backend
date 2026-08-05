package com.spms.service;

import com.spms.dto.request.ProductRequestDTO;
import com.spms.dto.response.ProductResponseDTO;

import java.util.List;

// Service interface for Product CRUD operations.
public interface ProductService {

    // Create a new product.
    ProductResponseDTO createProduct(ProductRequestDTO requestDTO);

    // Retrieve all active products (Home page list).
    List<ProductResponseDTO> getAllProducts();

    // Retrieve the newest active products (Home New Arrivals).
    // limit = how many products to return (e.g. 4)
    List<ProductResponseDTO> getNewArrivals(int limit);

    // Retrieve a product by its ID.
    ProductResponseDTO getProductById(Long id);

    // Update an existing product.
    ProductResponseDTO updateProduct(Long id, ProductRequestDTO requestDTO);

    // Delete a product by its ID.
    void deleteProduct(Long id);

}