package com.ooadprojectserver.restaurantmanagement.repository.inventory;

import com.ooadprojectserver.restaurantmanagement.model.inventory.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
