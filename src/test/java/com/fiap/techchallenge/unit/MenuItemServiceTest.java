package com.fiap.techchallenge.unit;

import com.fiap.techchallenge.application.dto.CreateMenuItemRequest;
import com.fiap.techchallenge.application.dto.UpdateMenuItemRequest;
import com.fiap.techchallenge.application.service.MenuItemService;
import com.fiap.techchallenge.application.service.RestaurantService;
import com.fiap.techchallenge.domain.exception.ResourceNotFoundException;
import com.fiap.techchallenge.domain.model.MenuItem;
import com.fiap.techchallenge.domain.model.Restaurant;
import com.fiap.techchallenge.infrastructure.repository.MenuItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {


    @Mock
    private MenuItemRepository repository;


    @Mock
    private RestaurantService restaurantService;


    @InjectMocks
    private MenuItemService service;



    @Test
    void shouldCreateMenuItem() {

        CreateMenuItemRequest request =
                new CreateMenuItemRequest(
                        "Pizza",
                        "Pizza italiana",
                        BigDecimal.valueOf(59.90),
                        false,
                        "/images/pizza.jpg",
                        1L
                );


        Restaurant restaurant =
                createRestaurant();


        when(restaurantService.findById(1L))
                .thenReturn(restaurant);


        when(repository.save(any(MenuItem.class)))
                .thenAnswer(
                        invocation ->
                                invocation.getArgument(0)
                );


        var response =
                service.create(request);


        assertEquals(
                "Pizza",
                response.name()
        );


        assertEquals(
                "Restaurant",
                response.restaurantName()
        );


        verify(repository)
                .save(any(MenuItem.class));
    }



    @Test
    void shouldFindMenuItemsByRestaurant() {

        MenuItem item =
                createMenuItem();


        when(repository.findByRestaurantId(1L))
                .thenReturn(
                        List.of(item)
                );


        var response =
                service.findByRestaurant(1L);


        assertEquals(
                1,
                response.size()
        );


        assertEquals(
                "Pizza",
                response.get(0).name()
        );
    }



    @Test
    void shouldSearchMenuItemsByRestaurantAndName() {

        MenuItem item =
                createMenuItem();


        when(repository.findByRestaurantIdAndNameContainingIgnoreCase(
                1L,
                "piz"
        ))
                .thenReturn(
                        List.of(item)
                );


        var response =
                service.search(
                        1L,
                        "piz"
                );


        assertEquals(
                1,
                response.size()
        );


        assertEquals(
                "Pizza",
                response.get(0).name()
        );
    }



    @Test
    void shouldFindMenuItemById() {

        MenuItem item =
                createMenuItem();


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(item)
                );


        MenuItem response =
                service.findById(1L);


        assertEquals(
                1L,
                response.getId()
        );
    }



    @Test
    void shouldFindMenuItemResponseById() {

        MenuItem item =
                createMenuItem();


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(item)
                );


        var response =
                service.findResponseById(1L);


        assertEquals(
                "Pizza",
                response.name()
        );


        assertEquals(
                "Restaurant",
                response.restaurantName()
        );
    }



    @Test
    void shouldThrowWhenMenuItemNotFound() {

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
    void shouldUpdateMenuItem() {

        MenuItem item =
                createMenuItem();


        UpdateMenuItemRequest request =
                new UpdateMenuItemRequest(
                        "Burger",
                        "Burger artesanal",
                        BigDecimal.valueOf(39.90),
                        true,
                        "/images/burger.jpg"
                );


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(item)
                );


        when(repository.save(any(MenuItem.class)))
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
                "Burger",
                response.name()
        );


        assertEquals(
                true,
                response.onlyLocalConsumption()
        );


        assertEquals(
                BigDecimal.valueOf(39.90),
                response.price()
        );
    }



    @Test
    void shouldDeleteMenuItem() {

        MenuItem item =
                createMenuItem();


        when(repository.findById(1L))
                .thenReturn(
                        Optional.of(item)
                );


        service.delete(1L);


        verify(repository)
                .delete(item);
    }



    private MenuItem createMenuItem() {

        MenuItem item =
                new MenuItem();


        item.setId(1L);
        item.setName("Pizza");
        item.setDescription("Pizza italiana");
        item.setPrice(BigDecimal.valueOf(59.90));
        item.setOnlyLocalConsumption(false);
        item.setImagePath("/images/pizza.jpg");

        item.setRestaurant(
                createRestaurant()
        );


        return item;
    }



    private Restaurant createRestaurant() {

        Restaurant restaurant =
                new Restaurant();


        restaurant.setId(1L);
        restaurant.setName("Restaurant");


        return restaurant;
    }
}