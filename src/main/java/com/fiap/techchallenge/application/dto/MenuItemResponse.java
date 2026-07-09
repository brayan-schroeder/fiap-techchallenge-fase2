package com.fiap.techchallenge.application.dto;

import java.math.BigDecimal;

public record MenuItemResponse(

        Long id,
        String name,
        String description,
        BigDecimal price,
        Boolean onlyLocalConsumption,
        String imagePath,
        Long restaurantId,
        String restaurantName

) {
}