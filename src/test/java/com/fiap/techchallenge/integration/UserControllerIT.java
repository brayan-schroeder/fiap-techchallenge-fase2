package com.fiap.techchallenge.integration;

import com.fiap.techchallenge.application.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(
        webEnvironment =
                SpringBootTest.WebEnvironment.RANDOM_PORT
)
class UserControllerIT
        extends IntegrationTestBase {


    @Test
    void shouldCreateUser() {

        CreateUserRequest request =
                new CreateUserRequest(
                        "Brayan",
                        "brayan@test.com",
                        "brayan",
                        "123456",
                        "Street",
                        1L
                );


        ResponseEntity<UserResponse> response =
                restTemplate.postForEntity(
                        "/api/v1/users",
                        request,
                        UserResponse.class
                );


        assertEquals(
                HttpStatus.CREATED,
                response.getStatusCode()
        );


        assertEquals(
                "Brayan",
                response.getBody().name()
        );
    }



    @Test
    void shouldFindUsers() {

        ResponseEntity<UserResponse[]> response =
                restTemplate.getForEntity(
                        "/api/v1/users",
                        UserResponse[].class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );
    }



    @Test
    void shouldFindUserById() {

        UserResponse user =
                createUser();


        ResponseEntity<UserResponse> response =
                restTemplate.getForEntity(
                        "/api/v1/users/"
                        + user.id(),
                        UserResponse.class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );


        assertEquals(
                user.id(),
                response.getBody().id()
        );
    }



    @Test
    void shouldUpdateUser() {

        UserResponse user =
                createUser();


        UpdateUserRequest request =
                new UpdateUserRequest(
                        "Updated",
                        "updated@test.com",
                        "updated",
                        "New Address",
                        2L
                );


        ResponseEntity<UserResponse> response =
                restTemplate.exchange(
                        "/api/v1/users/"
                        + user.id(),
                        HttpMethod.PUT,
                        new HttpEntity<>(request),
                        UserResponse.class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );


        assertEquals(
                "Updated",
                response.getBody().name()
        );
    }



    @Test
    void shouldUpdatePassword() {

        UserResponse user =
                createUser();


        PasswordRequest request =
                new PasswordRequest(
                        "newPassword"
                );


        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/api/v1/users/"
                        + user.id()
                        + "/password",
                        HttpMethod.PATCH,
                        new HttpEntity<>(request),
                        Void.class
                );


        assertEquals(
                HttpStatus.NO_CONTENT,
                response.getStatusCode()
        );
    }



    @Test
    void shouldLogin() {

        createUser();


        LoginRequest request =
                new LoginRequest(
                        "brayan",
                        "123456"
                );


        ResponseEntity<Boolean> response =
                restTemplate.postForEntity(
                        "/api/v1/users/login",
                        request,
                        Boolean.class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );


        assertTrue(
                response.getBody()
        );
    }



    @Test
    void shouldDeleteUser() {

        UserResponse user =
                createUser();


        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/api/v1/users/"
                        + user.id(),
                        HttpMethod.DELETE,
                        null,
                        Void.class
                );


        assertEquals(
                HttpStatus.NO_CONTENT,
                response.getStatusCode()
        );
    }



    private UserResponse createUser() {

        return restTemplate.postForObject(
                "/api/v1/users",
                new CreateUserRequest(
                        "User",
                        System.nanoTime()
                        + "@test.com",
                        "login"
                        + System.nanoTime(),
                        "123456",
                        "Address",
                        1L
                ),
                UserResponse.class
        );
    }
}