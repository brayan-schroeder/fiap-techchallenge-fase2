package com.fiap.techchallenge.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(
        name = "CreateRestaurantRequest",
        description = "Request payload to create a new restaurant"
)
public record CreateRestaurantRequest(

        @Schema(
                description = "Restaurant name",
                example = "Tempero de Vó"
        )
        @NotBlank(message = "Name is required")
        String name,

        @Schema(
                description = "Restaurant address",
                example = "Rua Exemplo, 123"
        )
        @NotBlank(message = "Address is required")
        String address,

        @Schema(
                description = "Cuisine type",
                example = "Comida mineira"
        )
        @NotBlank(message = "Cuisine type is required")
        String cuisineType,

        @Schema(
                description = "Opening hours",
                example = "De segunda a sexta, 11:30h até 15:00h"
        )
        @NotBlank(message = "Opening hours is required")
        String openingHours,

        @Schema(
                description = "Owner",
                example = "1"
        )
        @NotNull(message = "Owner is required")
        Long ownerId
) {
}