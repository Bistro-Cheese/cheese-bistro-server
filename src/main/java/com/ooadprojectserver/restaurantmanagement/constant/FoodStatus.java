package com.ooadprojectserver.restaurantmanagement.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FoodStatus {
    AVAILABLE(1, "AVAILABLE"),
    OUT_OF_STOCK(2, "Out of stock"),
    UNAVAILABLE(3, "UNAVAILABLE");


    private final Integer value;
    private final String status;
}
