package com.fiap.techchallenge.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "PasswordRequest",
        description = "Request payload to update user password"
)
public record PasswordRequest(

        @Schema(
                description = "New user password",
                example = "newPassword123"
        )
        @NotBlank(message = "Password is required")
        String password
) {
}