package com.spms.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private Long id;

    private String productName;

    private String genericName;

    private String brand;

    private String dosageForm;

    private String strength;

    private BigDecimal purchasePrice;

    private BigDecimal sellingPrice;

    private Integer stockQuantity;

    private Integer reorderLevel;

    private LocalDate expiryDate;

    private String description;

    private Boolean isActive;

    // MapStruct maps this automatically from Product.createdAt
    private LocalDateTime createdAt;

}