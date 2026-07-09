package com.fiap.techchallenge.unit;

import com.fiap.techchallenge.application.dto.CreateRestaurantRequest;
import com.fiap.techchallenge.application.dto.UpdateRestaurantRequest;
import com.fiap.techchallenge.application.service.RestaurantService;
import com.fiap.techchallenge.application.service.UserService;
import com.fiap.techchallenge.application.service.UserTypeService;
import com.fiap.techchallenge.domain.exception.ResourceNotFoundException;
import com.fiap.techchallenge.domain.exception.RestaurantOwnerException;
import com.fiap.techchallenge.domain.model.Restaurant;
import com.fiap.techchallenge.domain.model.User;
import com.fiap.techchallenge.domain.model.UserType;
import com.fiap.techchallenge.infrastructure.repository.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {


    @Mock
    private RestaurantRepository repository;


    @Mock
    private UserService userService;

    @Mock
    private UserTypeService userTypeService;


    @InjectMocks
    private RestaurantService service;



    @Test
    void shouldCreateRestaurant() {

        CreateRestaurantRequest request =
                new CreateRestaurantRequest(
                        "Pizza House",
                        "Street 1",
                        "Italian",
                        "18:00 - 23:00",
                        1L
                );


        User owner = new User();

        owner.setId(1L);
        owner.setName("Brayan");


        when(userService.findById(1L))
                .thenReturn(owner);

        when(userTypeService.isOwner(
                owner.getUserType()
        ))
                .thenReturn(true);

        when(repository.save(any(Restaurant.class)))
                .thenAnswer(
                        invocation ->
                                invocation.getArgument(0)
                );


        var response =
                service.create(request);


        assertEquals(
                "Pizza House",
                response.name()
        );


        assertEquals(
                "Brayan",
                response.ownerName()
        );


        verify(repository)
                .save(any());
    }



    @Test
    void shouldFindAllRestaurants() {

        Restaurant restaurant =
                createRestaurant();


        when(repository.findAll())
                .thenReturn(
                        List.of(restaurant)
                );


        var result =
                service.findAll();


        assertEquals(
                1,
                result.size()
        );


        assertEquals(
                "Restaurant",
                result.get(0).name()
        );
    }



    @Test
    void shouldSearchRestaurantsByName() {

        Restaurant restaurant =
                createRestaurant();


        when(repository.findByNameContainingIgnoreCase(
                "res"
        ))
                .thenReturn(
                        List.of(restaurant)
                );


        var result =
                service.search("res");


        assertEquals(
                1,
                result.size()
        );
    }



    @Test
    void shouldFindRestaurantById() {

        Restaurant restaurant =
                createRestaurant();


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(restaurant)
                );


        Restaurant result =
                service.findById(1L);


        assertEquals(
                1L,
                result.getId()
        );
    }



    @Test
    void shouldThrowWhenRestaurantNotFound() {


        when(repository.findById(99L))
                .thenReturn(
                        Optional.empty()
                );


        assertThrows(
                ResourceNotFoundException.class,
                () -> service.findById(99L)
        );
    }

    @Test
    void shouldNotCreateRestaurantWhenUserIsNotOwner() {

        CreateRestaurantRequest request =
                new CreateRestaurantRequest(
                        "Pizza",
                        "Street",
                        "Italian",
                        "18:00",
                        1L
                );


        UserType type = new UserType();
        type.setName("CUSTOMER");


        User user = new User();

        user.setUserType(type);


        when(userService.findById(1L))
                .thenReturn(user);


        when(userTypeService.isOwner(type))
                .thenReturn(false);


        assertThrows(
                RestaurantOwnerException.class,
                () -> service.create(request)
        );


        verify(repository, never())
                .save(any());
    }

    @Test
    void shouldUpdateRestaurant() {

        Restaurant restaurant =
                createRestaurant();


        UpdateRestaurantRequest request =
                new UpdateRestaurantRequest(
                        "New Name",
                        "New Address",
                        "Japanese",
                        "10:00 - 22:00",
                        2L
                );


        UserType userType = new UserType();

        userType.setId(1L);
        userType.setName("OWNER");


        User newOwner = new User();

        newOwner.setId(2L);
        newOwner.setName("New Owner");
        newOwner.setUserType(userType);



        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(restaurant)
                );


        when(userService.findById(2L))
                .thenReturn(newOwner);


        when(userTypeService.isOwner(userType))
                .thenReturn(true);


        when(repository.save(any(Restaurant.class)))
                .thenAnswer(
                        invocation ->
                                invocation.getArgument(0)
                );


        var response =
                service.update(
                        1L,
                        request
                );


        assertEquals(
                "New Name",
                response.name()
        );


        assertEquals(
                "Japanese",
                response.cuisineType()
        );


        assertEquals(
                "New Owner",
                response.ownerName()
        );
    }



    @Test
    void shouldDeleteRestaurant() {

        Restaurant restaurant =
                createRestaurant();


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(restaurant)
                );


        service.delete(1L);


        verify(repository)
                .delete(restaurant);
    }



    private Restaurant createRestaurant() {

        UserType userType = new UserType();

        userType.setId(1L);
        userType.setName("OWNER");


        User owner = new User();

        owner.setId(1L);
        owner.setName("Brayan");
        owner.setEmail("email@test.com");
        owner.setLogin("brayan");
        owner.setUserType(userType);


        Restaurant restaurant =
                new Restaurant();


        restaurant.setId(1L);
        restaurant.setName("Restaurant");
        restaurant.setAddress("Address");
        restaurant.setCuisineType("Italian");
        restaurant.setOpeningHours("18:00");
        restaurant.setOwner(owner);


        return restaurant;
    }
}