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
    public static final String USER_ID = "/{user_id}";
    public static final String STAFF_USERNAME = "/{staff_username}";
    public static final String PROFILE = "/profile";
    //    Owner Routes
    public static final String OWNER = VERSION + "/owner";
    //    Manager Routes
    public static final String MANAGER = VERSION + "/manager";
    public static final String SCHEDULE = "/schedule";
    public static final String TIMEKEEPING_ID = "/{timekeeping_id}";
    //    Staff Routes
    public static final String STAFF = VERSION + "/staff";
    //    Product Routes
    public static final String FOOD = VERSION + "/foods";
    public static final String FOOD_ID = "/{food_id}";
    public static final String SEARCH_FOOD = "/search";
}
