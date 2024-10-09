package com.example.userms.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OtpDto {

    @NotBlank(message = "Otp can`t be null")
    @Size(min = 6, max = 6, message = "Otp code must 6 symbols")
    private String otp;
}
