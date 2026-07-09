package com.fiap.techchallenge.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "UpdateRestaurantRequest",
        description = "Request payload to update restaurant data"
)
public record UpdateRestaurantRequest(

        @Schema(
                description = "Restaurant name",
                example = "Sushi do Bairro"
        )
        @NotBlank(message = "Name is required")
        String name,

        @Schema(
                description = "Restaurant address",
                example = "Rua Exemplo, 456"
        )
        @NotBlank(message = "Address is required")
        String address,

        @Schema(
                description = "Cuisine type",
                example = "Comida japonesa"
        )
        @NotBlank(message = "Cuisine type is required")
        String cuisineType,

        @Schema(
                description = "Opening hours",
                example = "De terça a sábado, 18:30h até 23:00h"
        )
        @NotBlank(message = "Opening hours is required")
        String openingHours,

        @Schema(
                description = "Owner",
                example = "2"
        )
        @NotNull(message = "Owner is required")
        Long ownerId
) {
}