package com.ooadprojectserver.restaurantmanagement.repository.ingredient;

import com.ooadprojectserver.restaurantmanagement.model.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findBySupplierAndName(String supplier, String name);
}
