package com.fiap.techchallenge.application.dto;

public record RestaurantResponse(

        Long id,
        String name,
        String address,
        String cuisineType,
        String openingHours,
        Long ownerId,
        String ownerName

) {
}