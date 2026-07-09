package com.fiap.techchallenge.integration;

import com.fiap.techchallenge.application.dto.*;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(
        webEnvironment =
                SpringBootTest.WebEnvironment.RANDOM_PORT
)
class MenuItemControllerIT
        extends IntegrationTestBase {


    @Test
    void shouldCreateMenuItem() {

        RestaurantResponse restaurant =
                createRestaurant();


        CreateMenuItemRequest request =
                new CreateMenuItemRequest(
                        "Pizza",
                        "Pizza italiana",
                        BigDecimal.valueOf(59.90),
                        false,
                        "/images/pizza.jpg",
                        restaurant.id()
                );


        ResponseEntity<MenuItemResponse> response =
                restTemplate.postForEntity(
                        "/api/v1/menu-items",
                        request,
                        MenuItemResponse.class
                );


        assertEquals(
                HttpStatus.CREATED,
                response.getStatusCode()
        );


        assertEquals(
                "Pizza",
                response.getBody().name()
        );


        assertEquals(
                restaurant.id(),
                response.getBody().restaurantId()
        );
    }



    @Test
    void shouldFindMenuItemsByRestaurant() {

        MenuItemResponse item =
                createMenuItem();


        ResponseEntity<MenuItemResponse[]> response =
                restTemplate.getForEntity(
                        "/api/v1/menu-items?restaurantId="
                        + item.restaurantId(),
                        MenuItemResponse[].class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );


        assertTrue(
                response.getBody().length > 0
        );
    }



    @Test
    void shouldSearchMenuItems() {

        MenuItemResponse item =
                createMenuItem();


        ResponseEntity<MenuItemResponse[]> response =
                restTemplate.getForEntity(
                        "/api/v1/menu-items?restaurantId="
                        + item.restaurantId()
                        + "&name=Pizza",
                        MenuItemResponse[].class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );


        assertTrue(
                response.getBody().length > 0
        );
    }



    @Test
    void shouldFindMenuItemById() {

        MenuItemResponse item =
                createMenuItem();


        ResponseEntity<MenuItemResponse> response =
                restTemplate.getForEntity(
                        "/api/v1/menu-items/"
                        + item.id(),
                        MenuItemResponse.class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );


        assertEquals(
                item.id(),
                response.getBody().id()
        );
    }



    @Test
    void shouldUpdateMenuItem() {

        MenuItemResponse item =
                createMenuItem();


        UpdateMenuItemRequest request =
                new UpdateMenuItemRequest(
                        "Burger",
                        "Burger artesanal",
                        BigDecimal.valueOf(39.90),
                        true,
                        "/images/burger.jpg"
                );


        ResponseEntity<MenuItemResponse> response =
                restTemplate.exchange(
                        "/api/v1/menu-items/"
                        + item.id(),
                        HttpMethod.PUT,
                        new HttpEntity<>(request),
                        MenuItemResponse.class
                );


        assertEquals(
                HttpStatus.OK,
                response.getStatusCode()
        );


        assertEquals(
                "Burger",
                response.getBody().name()
        );


        assertTrue(
                response.getBody()
                        .onlyLocalConsumption()
        );
    }



    @Test
    void shouldDeleteMenuItem() {

        MenuItemResponse item =
                createMenuItem();


        ResponseEntity<Void> response =
                restTemplate.exchange(
                        "/api/v1/menu-items/"
                        + item.id(),
                        HttpMethod.DELETE,
                        null,
                        Void.class
                );


        assertEquals(
                HttpStatus.NO_CONTENT,
                response.getStatusCode()
        );
    }




    private MenuItemResponse createMenuItem() {

        RestaurantResponse restaurant =
                createRestaurant();


        return restTemplate.postForObject(
                "/api/v1/menu-items",
                new CreateMenuItemRequest(
                        "Pizza",
                        "Pizza italiana",
                        BigDecimal.valueOf(59.90),
                        false,
                        "/images/pizza.jpg",
                        restaurant.id()
                ),
                MenuItemResponse.class
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