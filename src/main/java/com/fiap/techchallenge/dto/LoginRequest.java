package com.fiap.techchallenge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "LoginRequest",
        description = "Request payload to authenticate a user"
)
public record LoginRequest(

        @Schema(
                description = "User login",
                example = "alunobrayan"
        )
        @NotBlank(message = "Login is required")
        String login,

        @Schema(
                description = "User password",
                example = "123456"
        )
        @NotBlank(message = "Password is required")
        String password
) {
}