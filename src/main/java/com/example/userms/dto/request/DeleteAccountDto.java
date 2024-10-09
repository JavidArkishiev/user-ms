package com.example.userms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DeleteAccountDto {
    @NotBlank(message = "Password can`t be null")
    @Size(min = 8, message = "Password must the least 8 symbols")
    private String password;
}
