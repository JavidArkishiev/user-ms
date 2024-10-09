package com.example.userms.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangePasswordRequest {
    @NotBlank(message = "Password can`t be null")
    @Size(min = 8, message = "Password must the least 8 symbols")
    private String currentPassword;

    @Schema(example = "string")
    @NotBlank(message = "Password can`t be null")
    @Size(min = 8, message = "Password must the least 8 symbols")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$",
            message = "Password must contain at least one uppercase Latin letter," +
                    " one lowercase Latin letter, and one digit.")
    private String newPassword;

    @NotBlank(message = "Password can`t be null")
    private String confirmNewPassword;
}
