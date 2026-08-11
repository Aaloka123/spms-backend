package com.spms.controller;

import com.spms.constants.ApiPath;
import com.spms.dto.request.ProductRequestDTO;
import com.spms.dto.response.ProductResponseDTO;
import com.spms.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Handles all product API requests under /api/products
@RestController
@RequestMapping(ApiPath.PRODUCTS)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Create a new product
    // Only ADMIN and PHARMACIST can create (login required)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','PHARMACIST')")
    public ResponseEntity<ProductResponseDTO> createProduct(
            @Valid @RequestBody ProductRequestDTO requestDTO) {

        ProductResponseDTO response = productService.createProduct(requestDTO);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Get all active products
    // Public endpoint 
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {

        return ResponseEntity.ok(productService.getAllProducts());
    }

    // Get newest active products
    // Public endpoint 
    // IMPORTANT: Keep this ABOVE /{id} so "new-arrivals" is not treated as an id
    @GetMapping("/new-arrivals")
    public ResponseEntity<List<ProductResponseDTO>> getNewArrivals(
            @RequestParam(defaultValue = "4") int limit) {

        return ResponseEntity.ok(productService.getNewArrivals(limit));
    }

    // Admin/Pharmacist: all products (active + inactive)
    // Keep ABOVE /{id} so "admin" is not treated as an id
    @GetMapping("/admin/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','PHARMACIST')")
    public ResponseEntity<List<ProductResponseDTO>> getAllProductsForAdmin() {
        return ResponseEntity.ok(productService.getAllProductsForAdmin());
    }

    // Get one product by id
    // Public endpoint - no login needed (Home product details)
    // {id:\\d+} means id must be a number only
    // so "/new-arrivals" is never treated as an id
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Long id) {

        return ResponseEntity.ok(productService.getProductById(id));
    }

    // Update an existing product
    // Only ADMIN and PHARMACIST can update (login required)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','PHARMACIST')")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequestDTO requestDTO) {

        return ResponseEntity.ok(productService.updateProduct(id, requestDTO));
    }

    // Delete a product
    // Only ADMIN and PHARMACIST can delete (login required)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','PHARMACIST')")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {

        productService.deleteProduct(id);

        return ResponseEntity.ok("Product deleted successfully.");
    }


}