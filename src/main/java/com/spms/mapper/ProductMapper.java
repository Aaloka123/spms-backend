package com.spms.mapper;

import com.spms.dto.request.ProductRequestDTO;
import com.spms.dto.response.ProductResponseDTO;
import com.spms.app.entity.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

// Mapper for converting between Product Entity and DTOs.
@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Convert ProductRequestDTO to Product Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Product toEntity(ProductRequestDTO requestDTO);

    // Convert Product Entity to ProductResponseDTO
    ProductResponseDTO toResponseDTO(Product product);

    // Convert list of Product entities to list of ProductResponseDTO
    List<ProductResponseDTO> toResponseDTOList(List<Product> products);

    // Update existing Product Entity from RequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(ProductRequestDTO requestDTO,
                             @MappingTarget Product product);

}
