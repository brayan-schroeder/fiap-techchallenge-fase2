package com.fiap.techchallenge.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "UpdateUserRequest",
        description = "Request payload to update user data"
)
public record UpdateUserRequest(

        @Schema(
                description = "User full name",
                example = "Brayan Schroeder"
        )
        @NotBlank(message = "Name is required")
        String name,

        @Schema(
                description = "User email",
                example = "alunobrayan@fiap.com.br"
        )
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email")
        String email,

        @Schema(
                description = "User login",
                example = "alunobrayan"
        )
        @NotBlank(message = "Login is required")
        String login,

        @Schema(
                description = "User address",
                example = "Rua Exemplo, 303"
        )
        String address,

        @Schema(
                description = "User type",
                example = "1"
        )
        @NotNull(message = "User type is required")
        Long userTypeId
) {
}