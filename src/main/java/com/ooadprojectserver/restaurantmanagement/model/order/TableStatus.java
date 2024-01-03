package com.ooadprojectserver.restaurantmanagement.model.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TableStatus {
    EMPTY,
    OCCUPIED,
    REVERSED
}
