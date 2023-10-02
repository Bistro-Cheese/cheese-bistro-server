package com.ooadprojectserver.restaurantmanagement.constant;

public class APIConstant {
    //    Version
    public static final String VERSION = "/api/v1";
    public static final String AUTH = VERSION + "/auth";
    //    Auth Routes
    public static final String REGISTER = "/register";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String REFRESH_TOKEN = "/refresh-token";

    // Product Routes
    public static final String FOOD = VERSION + "/food";

    public static final String ALL_FOOD = "/all";
    public static final String FOOD_BY_ID = "/{food_id}";
    public static final String CREATE_FOOD = "/create";
    public static final String DELETE_FOOD = "/delete/{food_id}";
    public static final String UPDATE_FOOD = "/update/{food_id}";
    public static final String FOOD_FILTER_LIST = "/filter";
}
