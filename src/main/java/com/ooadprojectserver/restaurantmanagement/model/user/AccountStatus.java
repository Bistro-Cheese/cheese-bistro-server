package com.ooadprojectserver.restaurantmanagement.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AccountStatus {
    ACTIVE(1, "Active"),
    INACTIVE(2, "Inactive");

    private final int value;
    private final String status;
}
