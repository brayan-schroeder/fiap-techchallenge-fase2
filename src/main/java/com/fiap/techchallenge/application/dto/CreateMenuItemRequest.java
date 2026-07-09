package com.fiap.techchallenge.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

@Schema(
        name = "CreateMenuItemRequest",
        description = "Request payload to create a new menu item"
)
public record CreateMenuItemRequest(

        @Schema(
                description = "Item name",
                example = "Salmão com molho de maracujá"
        )
        @NotBlank(message = "Name is required")
        String name,

        @Schema(
                description = "Item description",
                example = "Salmão ao molho de maracujá com acompanhamento de arroz, pirão e salada"
        )
        @NotBlank(message = "Description is required")
        String description,

        @Schema(
                description = "Item price",
                example = "139,90"
        )
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than zero")
        BigDecimal price,

        @Schema(
                description = "Item is local consumption?",
                example = "true"
        )
        @NotNull(message = "Consumption availability is required")
        Boolean onlyLocalConsumption,

        @Schema(
                description = "Item image path",
                example = "mysite.com/imagem-aleatoria"
        )
        String imagePath,

        @Schema(
                description = "Restaurant",
                example = "1"
        )
        @NotNull(message = "Restaurant is required")
        Long restaurantId
) {
}