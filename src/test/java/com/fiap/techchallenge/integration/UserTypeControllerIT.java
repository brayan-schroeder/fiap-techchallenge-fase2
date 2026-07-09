package com.fiap.techchallenge.integration;

import com.fiap.techchallenge.application.dto.CreateUserTypeRequest;
import com.fiap.techchallenge.application.dto.UpdateUserTypeRequest;
import com.fiap.techchallenge.domain.model.UserType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(
        webEnvironment =
                SpringBootTest.WebEnvironment.RANDOM_PORT
)
class UserTypeControllerIT
        extends IntegrationTestBase {


    @Test
    void shouldCreateUserType() {

        var request =
                new CreateUserTypeRequest(
                        "ADMIN"
                );


        ResponseEntity<UserType> response =
                restTemplate.postForEntity(
                        "/api/v1/user-types",
                        request,
                        UserType.class
                );


        assertEquals(
                HttpStatus.CREATED,
                response.getStatusCode()
        );


        assertEquals(
                "ADMIN",
                response.getBody().getName()
        );
    }



    @Test
    void shouldFindUserTypes() {

        ResponseEntity<UserType[]> response =
                restTemplate.getForEntity(
                        "/api/v1/user-types",
                        UserType[].class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );
    }



    @Test
    void shouldUpdateUserType() {

        UserType created =
                restTemplate.postForObject(
                        "/api/v1/user-types",
                        new CreateUserTypeRequest(
                                "TEMP"
                        ),
                        UserType.class
                );


        HttpEntity<UpdateUserTypeRequest> request =
                new HttpEntity<>(
                        new UpdateUserTypeRequest(
                                "UPDATED"
                        )
                );


        ResponseEntity<UserType> response =
                restTemplate.exchange(
                        "/api/v1/user-types/"
                        + created.getId(),
                        HttpMethod.PUT,
                        request,
                        UserType.class
                );


        assertEquals(
                "UPDATED",
                response.getBody().getName()
        );
    }



    @Test
    void shouldDeleteUserType() {

        UserType created =
                restTemplate.postForObject(
                        "/api/v1/user-types",
                        new CreateUserTypeRequest(
                                "DELETE"
                        ),
                        UserType.class
                );


        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/api/v1/user-types/"
                        + created.getId(),
                        HttpMethod.DELETE,
                        null,
                        Void.class
                );


        assertEquals(
                HttpStatus.NO_CONTENT,
                response.getStatusCode()
        );
    }
}