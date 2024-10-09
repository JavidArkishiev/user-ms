package com.example.userms.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Schema(example = "user@example.com")
    @NotBlank(message = "E-mail can`t be null")
    @Email(message = "E-mail is not correct format")
    private String email;

    @NotBlank(message = "Password can`t be null")
    private String password;
}
