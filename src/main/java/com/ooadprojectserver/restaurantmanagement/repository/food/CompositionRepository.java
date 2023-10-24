package com.ooadprojectserver.restaurantmanagement.repository.food;

import com.ooadprojectserver.restaurantmanagement.model.composition.Composition;
import com.ooadprojectserver.restaurantmanagement.model.composition.food.Food;
import com.ooadprojectserver.restaurantmanagement.model.composition.ingredient.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface CompositionRepository extends JpaRepository<Composition, UUID> {
    @Transactional
    @Modifying
    @Query("update Composition c set c.ingredient = ?1, c.portion = ?2 where c.id = ?3")
    void updateIngredientAndPortionById(Ingredient ingredient, Integer portion, UUID id);

    @Query("select c from Composition c where c.food.id = ?1 and c.ingredient.id = ?2")
    Composition findComposition(UUID food_id, Long ingredient_id);

    @Transactional
    @Modifying
    @Query("delete from Composition c where c.food = ?1")
    void deleteByFood(Food food);

    @Transactional
    @Modifying
    @Query("delete from Composition c where c.ingredient = ?1")
    void deleteByIngredient(Ingredient ingredient);

    @Query("select c from Composition c where c.food.id = ?1")
    List<Composition> findByFood(UUID id);
}
