package com.fiap.techchallenge.interfaces.controller;

import com.fiap.techchallenge.application.dto.*;
import com.fiap.techchallenge.application.service.RestaurantService;
import com.fiap.techchallenge.domain.model.Restaurant;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Restaurants",
        description = "Restaurant management endpoints"
)
@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
public class RestaurantController {

    private final RestaurantService service;

    @Operation(
            summary = "Create restaurant",
            description = "Creates a new restaurant in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurant created successfully"),

            @ApiResponse(
                    responseCode = "412",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "type": "https://api.techchallenge/errors/validation",
                                      "title": "Validation error",
                                      "status": 412,
                                      "detail": "Owner user type wrong",
                                      "timestamp": "2026-04-30T16:00:00",
                                      "path": "/api/v1/restaurants",
                                      "fields": {
                                        "email": "Owner user type is not 'OWNER'"
                                      }
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<RestaurantResponse> create(
            @Valid @RequestBody CreateRestaurantRequest request
    ) {

        RestaurantResponse createdRestaurant = service.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdRestaurant);
    }

    @Operation(
            summary = "List restaurants",
            description = "Returns all restaurants or filters by name"
    )
    @ApiResponse(responseCode = "200", description = "Restaurants found")
    @GetMapping
    public ResponseEntity<List<RestaurantResponse>> list(
            @RequestParam(required = false) String name
    ) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(service.search(name));
        }

        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Find restaurant by id",
            description = "Returns a single restaurant by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant found"),

            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "type": "https://api.techchallenge/errors/not-found",
                                      "title": "Resource not found",
                                      "status": 404,
                                      "detail": "Restaurant with id 99 not found",
                                      "timestamp": "2026-04-30T16:00:00",
                                      "path": "/api/v1/restaurants/99"
                                    }
                                    """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponse> get(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.findResponseById(id));
    }

    @Operation(
            summary = "Update restaurant",
            description = "Updates restaurant data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurant updated"),

            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "type": "https://api.techchallenge/errors/not-found",
                                  "title": "Resource not found",
                                  "status": 404,
                                  "detail": "Restaurant with id 99 not found",
                                  "timestamp": "2026-04-30T16:00:00",
                                  "path": "/api/v1/restaurants/99"
                                }
                                """
                            )
                    )
            ),
    })
    @PutMapping("/{id}")
    public ResponseEntity<RestaurantResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateRestaurantRequest request
    ) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(
            summary = "Delete restaurant",
            description = "Deletes a restaurant by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Restaurant deleted"),

            @ApiResponse(
                    responseCode = "404",
                    description = "Restaurant not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "type": "https://api.techchallenge/errors/not-found",
                                  "title": "Resource not found",
                                  "status": 404,
                                  "detail": "Restaurant with id 99 not found",
                                  "timestamp": "2026-04-30T16:00:00",
                                  "path": "/api/v1/restaurants/99"
                                }
                                """
                            )
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {
        service.delete(id);

        return ResponseEntity.noContent().build();
    }
}