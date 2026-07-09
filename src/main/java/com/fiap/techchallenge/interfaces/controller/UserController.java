package com.fiap.techchallenge.interfaces.controller;

import com.fiap.techchallenge.application.dto.*;
import com.fiap.techchallenge.domain.model.User;
import com.fiap.techchallenge.application.service.UserService;
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
        name = "Users",
        description = "User management endpoints"
)
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @Operation(
            summary = "Create user",
            description = "Creates a new user in the system"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),

            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "type": "https://api.techchallenge/errors/validation",
                                      "title": "Validation error",
                                      "status": 400,
                                      "detail": "One or more fields are invalid",
                                      "timestamp": "2026-04-30T16:00:00",
                                      "path": "/api/v1/users",
                                      "fields": {
                                        "email": "Invalid email"
                                      }
                                    }
                                    """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "Email or login already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "type": "https://api.techchallenge/errors/duplicate-email",
                                      "title": "Duplicate email",
                                      "status": 409,
                                      "detail": "Email already registered",
                                      "timestamp": "2026-04-30T16:00:00",
                                      "path": "/api/v1/users"
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping
    public ResponseEntity<UserResponse> create(
            @Valid @RequestBody CreateUserRequest request
    ) {

        UserResponse createdUser = service.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdUser);
    }

    @Operation(
            summary = "List users",
            description = "Returns all users or filters by name"
    )
    @ApiResponse(responseCode = "200", description = "Users found")
    @GetMapping
    public ResponseEntity<List<UserResponse>> list(
            @RequestParam(required = false) String name
    ) {
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(service.search(name));
        }

        return ResponseEntity.ok(service.findAll());
    }

    @Operation(
            summary = "Find user by id",
            description = "Returns a single user by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),

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
                                      "detail": "User with id 99 not found",
                                      "timestamp": "2026-04-30T16:00:00",
                                      "path": "/api/v1/users/99"
                                    }
                                    """
                            )
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> get(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(service.findResponseById(id));
    }

    @Operation(
            summary = "Update user",
            description = "Updates user data except password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User updated"),

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
                                  "detail": "User with id 99 not found",
                                  "timestamp": "2026-04-30T16:00:00",
                                  "path": "/api/v1/users/99"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "409",
                    description = "Email or login already exists",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Duplicate Email",
                                            value = """
                                        {
                                          "type": "https://api.techchallenge/errors/duplicate-email",
                                          "title": "Duplicate email",
                                          "status": 409,
                                          "detail": "Email already registered",
                                          "timestamp": "2026-04-30T16:00:00",
                                          "path": "/api/v1/users/99"
                                        }
                                        """
                                    ),
                                    @ExampleObject(
                                            name = "Duplicate Login",
                                            value = """
                                        {
                                          "type": "https://api.techchallenge/errors/duplicate-login",
                                          "title": "Duplicate login",
                                          "status": 409,
                                          "detail": "Login already registered",
                                          "timestamp": "2026-04-30T16:00:00",
                                          "path": "/api/v1/users/99"
                                        }
                                        """
                                    )
                            }
                    )
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request
    ) {

        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(
            summary = "Change password",
            description = "Updates only user password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password updated"),

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
                                      "detail": "User with id 99 not found",
                                      "timestamp": "2026-04-30T16:00:00",
                                      "path": "/api/v1/users/99/password"
                                    }
                                    """
                            )
                    )
            )
    })
    @PatchMapping("/{id}/password")
    public ResponseEntity<Void> password(
            @PathVariable Long id,
            @Valid @RequestBody PasswordRequest request
    ) {
        service.updatePassword(id, request.password());

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete user",
            description = "Deletes a user by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),

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
                                  "detail": "User with id 99 not found",
                                  "timestamp": "2026-04-30T16:00:00",
                                  "path": "/api/v1/users/99"
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

    @Operation(
            summary = "Login",
            description = "Validates user login and password"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Authenticated"),

            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid credentials",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                    {
                                      "type": "https://api.techchallenge/errors/unauthorized",
                                      "title": "Invalid credentials",
                                      "status": 401,
                                      "detail": "Invalid login or password",
                                      "timestamp": "2026-04-30T16:00:00",
                                      "path": "/api/v1/users/login"
                                    }
                                    """
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<Boolean> login(
            @Valid @RequestBody LoginRequest request
    ) {
        service.login(request);

        return ResponseEntity.ok(true);
    }
}