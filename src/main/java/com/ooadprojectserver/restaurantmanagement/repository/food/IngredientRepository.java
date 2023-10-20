package com.ooadprojectserver.restaurantmanagement.repository.food;

import com.ooadprojectserver.restaurantmanagement.model.composition.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    @Transactional
    @Modifying
    @Query("update Ingredient i set i.name = ?1, i.ingredientType = ?2 where i.id = ?3")
    void updateIngredient(String name, Integer ingredientType, Long id);
    Optional<Ingredient> findByName(String name);
}
