package com.ooadprojectserver.restaurantmanagement.model.ingredient;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum IngredientType {
    DRY(1, "Dry"),
    BEVERAGE(2, "Beverage");

    private final int value;
    private final String type;
}
