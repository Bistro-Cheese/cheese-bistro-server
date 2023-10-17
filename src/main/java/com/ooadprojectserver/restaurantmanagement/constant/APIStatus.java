package com.ooadprojectserver.restaurantmanagement.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum APIStatus {
    ERR_UNAUTHORIZED(401, "Unauthorized or Access Token is expired"),
    ERR_DELETE_OWNER(403, "Owner cannot be deleted"),

    // Common status
    ERR_INTERNAL_SERVER(500, "Internal Error"),
    ERR_BAD_REQUEST(400, "Bad request"),

    //User
    USER_NOT_FOUND(404, "User Not Found"),

    //Food status
    CATEGORY_NOT_FOUND(404, "Category Not Found"),
    FOOD_NOT_FOUND(404, "Food Not Found"),
    CREATE_FOOD_ERROR(700, "Create product error"),
    DELETE_FOOD_ERROR(701, "Delete product error"),
    GET_FOOD_ERROR(702, "Can't get product detail"),
    UPDATE_FOOD_ERROR(703, "Update product error"),
    GET_LIST_PRODUCT_ERROR(704, "Get list product error"),

    //    Pagination
    ERR_PAGINATION(400, "Page size or page number must not be less than one"),

    //    Schedule
    SCHEDULE_NOT_FOUND(400, "Schedule not found"),
    SCHEDULE_ALREADY_EXISTED(400, "This staff already assigned to this schedule.");

    private final int status;
    private final String message;
}
