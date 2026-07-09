package com.fiap.techchallenge.interfaces.controller;

import com.fiap.techchallenge.application.dto.*;
import com.fiap.techchallenge.application.service.UserTypeService;
import com.fiap.techchallenge.domain.model.UserType;
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
        name = "User Types",
        description = "User type management endpoints"
)
@RestController
@RequestMapping("/api/v1/user-types")
@RequiredArgsConstructor
public class UserTypeController {

    private final UserTypeService service;

    @Operation(
            summary = "Create user type",
            description = "Creates a new user type in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User type created successfully")
    })
    @PostMapping
    public ResponseEntity<UserType> create(
            @Valid @RequestBody CreateUserTypeRequest request
    ) {

        UserType createdUserType = service.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUserType);
    }

    @Operation(
            summary = "List user types",
            description = "Returns all user types"
    )
    @ApiResponse(responseCode = "200", description = "User types found")
    @GetMapping
    public ResponseEntity<List<UserType>> list() {

        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Find user type by id",
            description = "Returns a single user type by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User type found"),

            @ApiResponse(
                    responseCode = "404",
                    description = "User type not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "type": "https://api.techchallenge/errors/not-found",
                                      "title": "Resource not found",
                                      "status": 404,
                                      "detail": "User type with id 99 not found",
                                      "timestamp": "2026-07-09T16:00:00",
                                      "path": "/api/v1/user-types/99"
                                    }
                                    """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserType> get(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(
            summary = "Update user type",
            description = "Updates user type data"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User type updated"),

            @ApiResponse(
                    responseCode = "404",
                    description = "User type not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "type": "https://api.techchallenge/errors/not-found",
                                  "title": "Resource not found",
                                  "status": 404,
                                  "detail": "User with id 99 not found",
                                  "timestamp": "2026-07-09T16:00:00",
                                  "path": "/api/v1/user-types/99"
                                }
                                """
                            )
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserType> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserTypeRequest request
    ) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(
            summary = "Delete user type",
            description = "Deletes a user type by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User type deleted"),

            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "type": "https://api.techchallenge/errors/not-found",
                                  "title": "Resource not found",
                                  "status": 404,
                                  "detail": "User type with id 99 not found",
                                  "timestamp": "2026-07-09T16:00:00",
                                  "path": "/api/v1/user-types/99"
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