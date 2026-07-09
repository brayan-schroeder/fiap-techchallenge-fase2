package com.fiap.techchallenge.application.service;

import com.fiap.techchallenge.application.dto.CreateMenuItemRequest;
import com.fiap.techchallenge.application.dto.MenuItemResponse;
import com.fiap.techchallenge.application.dto.RestaurantResponse;
import com.fiap.techchallenge.application.dto.UpdateMenuItemRequest;
import com.fiap.techchallenge.domain.exception.ResourceNotFoundException;
import com.fiap.techchallenge.domain.model.MenuItem;
import com.fiap.techchallenge.infrastructure.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuItemService {

    private final MenuItemRepository repository;
    private final RestaurantService restaurantService;

    public MenuItemResponse create(CreateMenuItemRequest request) {

        MenuItem menuItem = new MenuItem();

        menuItem.setName(request.name());
        menuItem.setDescription(request.description());
        menuItem.setPrice(request.price());
        menuItem.setOnlyLocalConsumption(request.onlyLocalConsumption());
        menuItem.setImagePath(request.imagePath());

        menuItem.setRestaurant(
                restaurantService.findById(
                        request.restaurantId()
                )
        );

        return toResponse(repository.save(menuItem));
    }

    public List<MenuItemResponse> findByRestaurant(Long restaurantId) {
        return repository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MenuItemResponse> search(
            Long restaurantId,
            String name
    ) {

        return repository
                .findByRestaurantIdAndNameContainingIgnoreCase(restaurantId, name)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public MenuItem findById(Long id) {

        return repository
                .findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Menu item not found"
                        )
                );
    }

    public MenuItemResponse findResponseById(Long id) {
        return toResponse(findById(id));
    }

    public MenuItemResponse update(
            Long id,
            UpdateMenuItemRequest request
    ) {

        MenuItem menuItem = findById(id);

        menuItem.setName(request.name());
        menuItem.setDescription(request.description());
        menuItem.setPrice(request.price());
        menuItem.setOnlyLocalConsumption(request.onlyLocalConsumption());
        menuItem.setImagePath(request.imagePath());

        return toResponse(repository.save(menuItem));
    }


    public void delete(Long id) {

        MenuItem menuItem = findById(id);

        repository.delete(menuItem);
    }

    private MenuItemResponse toResponse(
            MenuItem item
    ) {

        return new MenuItemResponse(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getOnlyLocalConsumption(),
                item.getImagePath(),
                item.getRestaurant().getId(),
                item.getRestaurant().getName()
        );
    }
}