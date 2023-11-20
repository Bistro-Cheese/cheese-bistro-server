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
    INVALID_ROLE_ID(400, "Invalid role id"),
    INVALID_DATE_OF_BIRTH(400, "Invalid date of birth"),
    EMAIL_ALREADY_EXISTED(400, "Email already existed"),
    USERNAME_ALREADY_EXISTED(400, "Username already existed"),
    PHONE_NUMBER_ALREADY_EXISTED(400, "Phone number already existed"),
    ROLE_CANNOT_UPDATE(400, "Role cannot update"),

    //Food status
    CATEGORY_NOT_FOUND(404, "Category Not Found"),
    FOOD_NOT_FOUND(404, "Food Not Found"),
    FOOD_ALREADY_EXISTED(400, "This food already created"),

    //    Ingredient status
    INGREDIENT_NOT_FOUND(404, "Ingredient Not Found"),
    INGREDIENT_ALREADY_EXISTED(400, "This ingredient already created"),

    //    Pagination
    ERR_PAGINATION(400, "Page size or page number must not be less than one"),

    //    Schedule
    SCHEDULE_NOT_FOUND(400, "Schedule not found"),
    SCHEDULE_ALREADY_EXISTED(400, "This staff already assigned to this schedule."),

    //    Composition
    COMPOSITION_NOT_FOUND(404, "Composition Not Found"),

    //    Mail
    EMAIL_SEND_FAILED(500, "Email send failed"),
    EMAIL_SEND_SUCCESS(201, "Email send success"),
    EMAIL_NOT_FOUND(404, "Email not found"),

    //    Order
    ORDER_NOT_FOUND(404, "Order not found"),
    ORDER_TABLE_NOT_FOUND(404, "Order table not found"),
    INGREDIENT_NOT_ENOUGH(400, "Ingredient not enough"),
    ORDER_LINE_NOT_FOUND(404, "Order line not found"),
    ORDER_TABLE_NOT_EMPTY(400, "Order table not empty"),

    //    Payment
    PAYMENT_NOT_FOUND(404, "Payment not found"),
    TRANSFER_METHOD_NOT_FOUND(404, "Transfer method not found"),
    PAYMENT_METHOD_ALREADY_EXISTED(400, "Payment method already existed"),
    ORDER_COMPLETED(400, "Order already completed");

    private final int status;
    private final String message;
}
