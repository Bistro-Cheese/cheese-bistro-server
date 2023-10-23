package com.ooadprojectserver.restaurantmanagement.model.composition.food;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FoodStatus {
    AVAILABLE(1, "Available"),
    UNAVAILABLE(2, "Unavailable"),
    DRAFT(3, "Draft");

    private final Integer value;
    private final String status;
}
