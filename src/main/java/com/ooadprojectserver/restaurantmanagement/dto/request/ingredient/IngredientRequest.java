package com.ooadprojectserver.restaurantmanagement.dto.request.ingredient;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
public class IngredientRequest {
    @JsonProperty("name")
    private String name;

    @JsonProperty("ingredient_type")
    private Integer ingredientType;

    @JsonProperty("unit")
    private String unit;

    @JsonProperty("supplier")
    private String supplier;
}
