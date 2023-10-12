package com.ooadprojectserver.restaurantmanagement.repository.food;

import com.ooadprojectserver.restaurantmanagement.model.food.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    public Optional<Category> findByName(String name);
}
