package com.example.userms.model;

import com.example.userms.dto.request.RoleRequestDto;
import com.example.userms.entity.Role;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role mapToRoleEntity(RoleRequestDto role);
}
