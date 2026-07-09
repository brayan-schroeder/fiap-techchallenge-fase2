package com.fiap.techchallenge.dto;

import com.fiap.techchallenge.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "CreateUserRequest",
        description = "Request payload to create a new user"
)
public record CreateUserRequest(

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
                description = "User password",
                example = "123456"
        )
        @NotBlank(message = "Password is required")
        String password,

        @Schema(
                description = "User address",
                example = "Rua Exemplo, 123"
        )
        String address,

        @Schema(
                description = "User role",
                example = "OWNER"
        )
        @NotNull(message = "Role is required")
        Role role
) {
}