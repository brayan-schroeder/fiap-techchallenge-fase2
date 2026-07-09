package com.fiap.techchallenge.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(
        name = "UpdateUserRequest",
        description = "Request payload to update user type data"
)
public record UpdateUserTypeRequest(

        @Schema(
                description = "User type name",
                example = "COSTUMER"
        )
        @NotBlank(message = "Name is required")
        String name
) {
}