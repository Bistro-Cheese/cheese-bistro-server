package com.ooadprojectserver.restaurantmanagement.dto.response.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum APIStatus {
    ERR_INVALID_DATA(100, "Input data is not valid."),
    ERR_USER_NOT_EXIST(110, "User does not exist"),
    ERR_USER_NOT_VALID(111, "User name or password is not correct"),
    USER_ALREADY_EXIST(112, "Email already exist"),
    USER_PENDING_STATUS(113, "User have not activated"),
    INVALID_PARAMETER(200, "Invalid request parameter"),
    ERR_UNAUTHORIZED(401, "Unauthorized or Access Token is expired"),
    ERR_PERMISSION_DENIED(402, "Access Permission denied"),
    ERR_COMPANY_ID_EMPTY(403, "Company id is required"),

    // Common status
    OK(200, null),
    ERR_INTERNAL_SERVER(500, "Internal Error"),
    SQL_ERROR(501, "SQL Error"),
    ERR_BAD_REQUEST(400, "Bad request"),
    ERR_SESSION_DATA_INVALID(603, "Session data invalid"),
    ERR_SESSION_NOT_FOUND(604, "Session not found"),
    // File upload error
    ERR_UPLOAD_FILE(900, "Upload file error."),

    //User status
    ERR_USER_NOT_FOUND(404, "User Not Found"),
    ERR_USER_ADDRESS_NOT_FOUND(405, "User Address Not Found"),
    ERR_GET_LIST_USERS(406, "Get list user error"),
    ERR_MISSING_PASSWORD(407, "Missing new password"),
    ERR_INCORRECT_PASSWORD(408, "Your old password is incorrect"),
    //Food status
    CREATE_FOOD_ERROR(700, "Create product error"),
    DELETE_FOOD_ERROR(701, "Delete product error"),
    GET_FOOD_ERROR(702, "Can't get product detail"),
    UPDATE_FOOD_ERROR(703, "Update product error"),
    GET_LIST_PRODUCT_ERROR(704, "Get list product error"),

    // order
    ERR_GET_LIST_ORDERS(800, "Can not get list orders"),
    ERR_GET_DETAIL_ORDERS(801, "Can not get detail orders"),
    ERR_ORDER_ID_EMPTY(802, "Order can't empty"),
    ERR_DELETE_ORDER(803, "Delete order is fail"),
    ERR_ORDER_ID_NOT_FOUND(804, "Order id not found"),

//    Pagination
    ERR_PAGINATION(400, "Page size or page number must not be less than one");
    private final int status;
    private final String message;

}
