package com.fiap.techchallenge.application.dto;

import java.time.LocalDateTime;

public record UserResponse(

        Long id,
        String name,
        String email,
        String login,
        String address,
        String userType,
        LocalDateTime lastUpdate

) {
}