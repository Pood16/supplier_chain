package org.tricol.supplierchain.mapper;

import org.mapstruct.Mapper;
import org.tricol.supplierchain.dto.response.UserResponseDto;
import org.tricol.supplierchain.entity.UserApp;
@Mapper(componentModel = "spring")

public interface UserMapper {
    UserResponseDto toResponse(UserApp user);
}
