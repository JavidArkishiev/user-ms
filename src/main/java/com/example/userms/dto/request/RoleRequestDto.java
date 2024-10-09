package com.example.userms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RoleRequestDto {
    @NotBlank(message = "Role name can`t be null")
    @Size(max = 12, message = "The role must be the maximum of 12 characters")
    private String name;
}
