package com.goose.clearsolutionstest.mapper;

import com.goose.clearsolutionstest.dto.UpdateUserDTO;
import com.goose.clearsolutionstest.dto.UserDTO;
import com.goose.clearsolutionstest.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User dtoToModel(UserDTO userDTO);

    UserDTO modelToDTO(User user);

    @Mapping(target = "id", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    void updateUser(@MappingTarget User user, UpdateUserDTO updateUserDTO);
}
