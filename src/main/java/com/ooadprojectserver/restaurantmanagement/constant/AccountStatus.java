package com.ooadprojectserver.restaurantmanagement.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AccountStatus {
    ACTIVE_STATUS(1,"Active"),
    DELETED_STATUS(2, "Deleted"),
    REVOKE_STATUS(3,"Revoke"),
    DISABLED_STATUS(4,"Disable"),
    DELETED_FOREVER_STATUS(5,"Deleted forever"),
    PENDING(6,"Pending"),
    TRIAL_ACCOUNT_STATUS(7,"Trial");

    private final Integer value;
    private final String type;
}
