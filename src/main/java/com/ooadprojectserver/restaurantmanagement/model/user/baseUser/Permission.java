package com.ooadprojectserver.restaurantmanagement.model.user.baseUser;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    OWNER_READ("owner:read"),
    OWNER_WRITE("owner:write"),
    OWNER_UPDATE("owner:update"),
    OWNER_DELETE("owner:delete"),
    MANAGER_READ("manager:read"),
    MANAGER_WRITE("manager:write"),
    MANAGER_UPDATE("manager:update"),
    MANAGER_DELETE("manager:delete"),
    STAFF_READ("staff:read"),
    STAFF_WRITE("staff:write"),
    STAFF_UPDATE("staff:update"),
    STAFF_DELETE("staff:delete");

    private final String permission;
}
