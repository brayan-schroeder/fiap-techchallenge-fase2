package com.fiap.techchallenge.application.service;

import com.fiap.techchallenge.application.dto.CreateRestaurantRequest;
import com.fiap.techchallenge.application.dto.RestaurantResponse;
import com.fiap.techchallenge.application.dto.UpdateRestaurantRequest;
import com.fiap.techchallenge.application.dto.UserResponse;
import com.fiap.techchallenge.domain.exception.ResourceNotFoundException;
import com.fiap.techchallenge.domain.exception.RestaurantOwnerException;
import com.fiap.techchallenge.domain.model.Restaurant;
import com.fiap.techchallenge.domain.model.User;
import com.fiap.techchallenge.infrastructure.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository repository;
    private final UserService userService;
    private final UserTypeService userTypeService;

    public RestaurantResponse create(CreateRestaurantRequest request) {

        Restaurant restaurant = new Restaurant();

        restaurant.setName(request.name());
        restaurant.setAddress(request.address());
        restaurant.setCuisineType(request.cuisineType());
        restaurant.setOpeningHours(request.openingHours());

        User owner = userService.findById(request.ownerId());

        if (!userTypeService.isOwner(owner.getUserType())) {
            throw new RestaurantOwnerException(
                    "Owner user type is not 'OWNER'"
            );
        }

        restaurant.setOwner(owner);

        return toResponse(repository.save(restaurant));
    }

    public List<RestaurantResponse> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RestaurantResponse> search(
            String name
    ) {

        return repository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Restaurant findById(Long id) {

        return repository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Restaurant not found"
                        )
                );
    }

    public RestaurantResponse findResponseById(Long id) {
        return toResponse(findById(id));
    }

    public RestaurantResponse update(
            Long id,
            UpdateRestaurantRequest request
    ) {

        Restaurant restaurant = findById(id);

        restaurant.setName(request.name());
        restaurant.setAddress(request.address());
        restaurant.setCuisineType(request.cuisineType());
        restaurant.setOpeningHours(request.openingHours());

        User owner = userService.findById(request.ownerId());

        if (!userTypeService.isOwner(owner.getUserType())) {
            throw new RestaurantOwnerException(
                    "Owner user type is not 'OWNER'"
            );
        }

        restaurant.setOwner(owner);

        return toResponse(repository.save(restaurant));
    }


    public void delete(Long id) {

        Restaurant restaurant = findById(id);

        repository.delete(restaurant);
    }

    private RestaurantResponse toResponse(
            Restaurant restaurant
    ) {

        return new RestaurantResponse(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getAddress(),
                restaurant.getCuisineType(),
                restaurant.getOpeningHours(),
                restaurant.getOwner().getId(),
                restaurant.getOwner().getName()
        );
    }
}