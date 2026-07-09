package com.fiap.techchallenge.infrastructure.repository;

import com.fiap.techchallenge.domain.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {

    List<MenuItem> findByRestaurantId(Long restaurantId);

    List<MenuItem> findByRestaurantIdAndNameContainingIgnoreCase(Long restaurantId, String name);
}