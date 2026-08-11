package com.spms.mapper;

import com.spms.dto.request.RegisterRequestDTO;
import com.spms.dto.request.UserRequestDTO;
import com.spms.dto.response.UserResponseDTO;
import com.spms.auth.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

//Mapper for converting between User Entity and DTOs.
@Mapper(componentModel = "spring")
public interface UserMapper {

    // Convert UserRequestDTO to User Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(UserRequestDTO requestDTO);

    // Convert public RegisterRequestDTO to User Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequestDTO requestDTO);

    // Convert User Entity to UserResponseDTO
    @Mapping(target = "roleName", source = "role.roleName")
    UserResponseDTO toResponseDTO(User user);

    // Convert list of User entities to list of UserResponseDTO
    List<UserResponseDTO> toResponseDTOList(List<User> users);

    // Update an existing User Entity using UserRequestDTO
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "enabled", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDTO(UserRequestDTO requestDTO,
                             @MappingTarget User user);

}