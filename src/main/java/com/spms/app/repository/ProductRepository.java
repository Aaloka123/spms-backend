package com.spms.app.repository;

import com.spms.app.entity.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

// Spring Data JPA creates the SQL automatically from method names.
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Returns every product where isActive = true
    // Used by Home page: GET /api/products
    List<Product> findByIsActiveTrue();

    // Returns active products, newest first (ORDER BY created_at DESC)
    // Used by Home page: GET /api/products/new-arrivals?limit=4
    List<Product> findByIsActiveTrueOrderByCreatedAtDesc(Pageable pageable);

    // Check if a product exists by product name
    boolean existsByProductName(String productName);

    // Check product name excluding current product (used during update)
    boolean existsByProductNameAndIdNot(String productName, Long id);

    // Find product by product name
    Optional<Product> findByProductName(String productName);

}