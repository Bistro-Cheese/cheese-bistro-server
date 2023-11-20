package com.ooadprojectserver.restaurantmanagement.model.discount;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiscountType {
    PERCENTAGE,
    FIXED
}
