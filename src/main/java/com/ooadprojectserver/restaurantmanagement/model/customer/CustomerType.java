package com.ooadprojectserver.restaurantmanagement.model.customer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CustomerType {
    STANDARD(1, 0),
    MEMBERSHIP(5, 2000000),
    VIP(10, 8500000);

    private final int visitCount;
    private final int totalSpent;
}
