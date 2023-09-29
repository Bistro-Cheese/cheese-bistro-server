package com.ooadprojectserver.restaurantmanagement.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
    PENDING(0),
    SHIPPING(1),
    COMPLETED(2),
    CANCEL(3);

    private final int status;
}

