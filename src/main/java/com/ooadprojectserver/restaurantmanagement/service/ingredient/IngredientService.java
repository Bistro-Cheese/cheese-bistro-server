package com.ooadprojectserver.restaurantmanagement.service.ingredient;

import com.ooadprojectserver.restaurantmanagement.dto.request.ingredient.IngredientRequest;
import com.ooadprojectserver.restaurantmanagement.model.ingredient.Ingredient;

import java.util.List;

public interface IngredientService {
    void create(IngredientRequest req);
    void update(Long id, IngredientRequest req);
    void delete(Long id);
    List<Ingredient> getAll();
    Ingredient self(Long id);
}
