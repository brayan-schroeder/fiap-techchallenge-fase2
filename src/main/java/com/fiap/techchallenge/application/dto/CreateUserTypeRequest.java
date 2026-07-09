package com.fiap.techchallenge.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "CreateUserTypeRequest",
        description = "Request payload to create a new user type"
)
public record CreateUserTypeRequest(

        @Schema(
                description = "User type name",
                example = "OWNER"
        )
        @NotBlank(message = "Name is required")
        String name
) {
}