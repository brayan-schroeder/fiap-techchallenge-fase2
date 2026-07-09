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
class RestaurantControllerIT
        extends IntegrationTestBase {


    @Test
    void shouldCreateRestaurant() {

        UserResponse owner =
                createOwner();


        CreateRestaurantRequest request =
                new CreateRestaurantRequest(
                        "Pizza House",
                        "Street 1",
                        "Italian",
                        "18:00 - 23:00",
                        owner.id()
                );


        ResponseEntity<RestaurantResponse> response =
                restTemplate.postForEntity(
                        "/api/v1/restaurants",
                        request,
                        RestaurantResponse.class
                );


        assertEquals(
                HttpStatus.CREATED,
                response.getStatusCode()
        );


        assertEquals(
                "Pizza House",
                response.getBody().name()
        );


        assertEquals(
                owner.id(),
                response.getBody().ownerId()
        );
    }



    @Test
    void shouldFindRestaurants() {

        ResponseEntity<RestaurantResponse[]> response =
                restTemplate.getForEntity(
                        "/api/v1/restaurants",
                        RestaurantResponse[].class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );
    }



    @Test
    void shouldFindRestaurantById() {

        RestaurantResponse restaurant =
                createRestaurant();


        ResponseEntity<RestaurantResponse> response =
                restTemplate.getForEntity(
                        "/api/v1/restaurants/"
                        + restaurant.id(),
                        RestaurantResponse.class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );


        assertEquals(
                restaurant.id(),
                response.getBody().id()
        );
    }



    @Test
    void shouldUpdateRestaurant() {

        RestaurantResponse restaurant =
                createRestaurant();


        UserResponse newOwner =
                createOwner();


        UpdateRestaurantRequest request =
                new UpdateRestaurantRequest(
                        "Updated Restaurant",
                        "New Street",
                        "Japanese",
                        "10:00 - 22:00",
                        newOwner.id()
                );


        ResponseEntity<RestaurantResponse> response =
                restTemplate.exchange(
                        "/api/v1/restaurants/"
                        + restaurant.id(),
                        HttpMethod.PUT,
                        new HttpEntity<>(request),
                        RestaurantResponse.class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );


        assertEquals(
                "Updated Restaurant",
                response.getBody().name()
        );


        assertEquals(
                newOwner.id(),
                response.getBody().ownerId()
        );
    }



    @Test
    void shouldDeleteRestaurant() {

        RestaurantResponse restaurant =
                createRestaurant();


        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/api/v1/restaurants/"
                        + restaurant.id(),
                        HttpMethod.DELETE,
                        null,
                        Void.class
                );


        assertEquals(
                HttpStatus.NO_CONTENT,
                response.getStatusCode()
        );
    }



    private RestaurantResponse createRestaurant() {

        UserResponse owner =
                createOwner();


        return restTemplate.postForObject(
                "/api/v1/restaurants",
                new CreateRestaurantRequest(
                        "Restaurant",
                        "Address",
                        "Italian",
                        "18:00",
                        owner.id()
                ),
                RestaurantResponse.class
        );
    }



    private UserResponse createOwner() {

        long value =
                System.nanoTime();


        return restTemplate.postForObject(
                "/api/v1/users",
                new CreateUserRequest(
                        "Owner",
                        value + "@test.com",
                        "owner" + value,
                        "123456",
                        "Address",
                        1L
                ),
                UserResponse.class
        );
    }
}