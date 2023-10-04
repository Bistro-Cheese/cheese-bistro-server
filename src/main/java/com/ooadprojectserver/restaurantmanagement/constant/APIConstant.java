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
    //    Users Routes
    public static final String USERS = VERSION + "/users";
    public static final String PROFILE = "/profile";
    //    Owner Routes
    public static final String OWNER = VERSION + "/owner";
    //    Product Routes
    public static final String FOOD = VERSION + "/foods";
    public static final String FOOD_ID = "/{food_id}";
    public static final String SEARCH_FOOD = "/search";
}
