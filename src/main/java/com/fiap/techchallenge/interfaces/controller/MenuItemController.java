package com.fiap.techchallenge.interfaces.controller;

import com.fiap.techchallenge.application.dto.*;
import com.fiap.techchallenge.application.service.MenuItemService;
import com.fiap.techchallenge.application.service.RestaurantService;
import com.fiap.techchallenge.domain.model.MenuItem;
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
        name = "Menu Items",
        description = "Menu item management endpoints"
)
@RestController
@RequestMapping("/api/v1/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService service;

    @Operation(
            summary = "Create menu item",
            description = "Creates a new menu item in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Menu item created successfully")
    })
    @PostMapping
    public ResponseEntity<MenuItemResponse> create(
            @Valid @RequestBody CreateMenuItemRequest request
    ) {

        MenuItemResponse createdMenuItem = service.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdMenuItem);
    }

    @Operation(
            summary = "List menu items",
            description = "Returns all menu items or filters by name"
    )
    @ApiResponse(responseCode = "200", description = "Menu items found")
    @GetMapping
    public ResponseEntity<List<MenuItemResponse>> list(
            @RequestParam Long restaurantId,
            @RequestParam(required = false) String name
    ) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(service.search(restaurantId, name));
        }

        return ResponseEntity.ok(service.findByRestaurant(restaurantId));
    }

    @Operation(
            summary = "Find menu item by id",
            description = "Returns a single menu item by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item found"),

            @ApiResponse(
                    responseCode = "404",
                    description = "Menu item not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "type": "https://api.techchallenge/errors/not-found",
                                      "title": "Resource not found",
                                      "status": 404,
                                      "detail": "Menu item with id 99 not found",
                                      "timestamp": "2026-04-30T16:00:00",
                                      "path": "/api/v1/menu-items/99"
                                    }
                                    """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<MenuItemResponse> get(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.findResponseById(id));
    }

    @Operation(
            summary = "Update menu item",
            description = "Updates menu item data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Menu item updated"),

            @ApiResponse(
                    responseCode = "404",
                    description = "Menu item not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "type": "https://api.techchallenge/errors/not-found",
                                  "title": "Resource not found",
                                  "status": 404,
                                  "detail": "Menu item with id 99 not found",
                                  "timestamp": "2026-04-30T16:00:00",
                                  "path": "/api/v1/menu-items/99"
                                }
                                """
                            )
                    )
            ),
    })
    @PutMapping("/{id}")
    public ResponseEntity<MenuItemResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateMenuItemRequest request
    ) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(
            summary = "Delete menu item",
            description = "Deletes a menu item by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Menu item deleted"),

            @ApiResponse(
                    responseCode = "404",
                    description = "Menu item not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "type": "https://api.techchallenge/errors/not-found",
                                  "title": "Menu item not found",
                                  "status": 404,
                                  "detail": "Menu item with id 99 not found",
                                  "timestamp": "2026-04-30T16:00:00",
                                  "path": "/api/v1/menu-items/99"
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