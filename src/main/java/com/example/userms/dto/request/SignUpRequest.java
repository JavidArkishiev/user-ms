package com.example.userms.dto.request;

import com.example.userms.validations.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {

    @Schema(example = "user@example.com")
    @NotBlank(message = "E-mail can`t be null")
    @Email(message = "E-mail is not correct format")
    private String email;

    @NotBlank(message = "FirstName can`t be null")
    @Size(max = 15, message = "FirstName must maximum 15 symbols")
    private String firstName;

    @NotBlank(message = "LastName can`t be null")
    @Size(max = 20, message = "LastName must maximum 20 symbols")
    private String lastName;

    @NotBlank(message = "Password can`t be null")
    @Size(min = 8, message = "Password must the least 8 symbols")
    @ValidPassword
    private String password;

    @NotBlank(message = "Password can`t be null")
    private String confirmPassword;
}
