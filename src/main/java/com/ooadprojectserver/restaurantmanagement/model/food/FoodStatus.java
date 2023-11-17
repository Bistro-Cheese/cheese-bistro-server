package com.ooadprojectserver.restaurantmanagement.model.food;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FoodStatus {
    AVAILABLE(1, "Available"),
    OUT_OF_STOCK(2, "Out of stock"),
    DRAFT(3, "Draft");

    private final Integer value;
    private final String status;
}
