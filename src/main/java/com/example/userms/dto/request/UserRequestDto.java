package com.example.userms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRequestDto {

    @NotBlank(message = "FirstName can not be null")
    @Size(max = 15, message = "FirstName must maximum 15 symbols")
    private String firstName;

    @NotBlank(message = "LastName can`t be null")
    @Size(max = 20, message = "LastName must maximum 20 symbols")
    private String lastName;

}
