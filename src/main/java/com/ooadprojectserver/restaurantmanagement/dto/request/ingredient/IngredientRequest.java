package com.ooadprojectserver.restaurantmanagement.dto.request.ingredient;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ooadprojectserver.restaurantmanagement.model.ingredient.IngredientType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class IngredientRequest {
    @JsonProperty("name")
    private String name;

    @JsonProperty("ingredient_type")
    private IngredientType ingredientType;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("supplier")
    private String supplier;
}
