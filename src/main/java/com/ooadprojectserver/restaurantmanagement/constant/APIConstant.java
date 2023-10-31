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
    public static final String STAFF_USERNAME = "/{staff_username}";
    public static final String PROFILE = "/profile";
    public static final String USER_ID = "/{user_id}";
    //    Owner Routes
    public static final String OWNER = VERSION + "/owner";
    public static final String ALL_USERS = "/users";
    public static final String OWNER_USER_ID = ALL_USERS + USER_ID;

    //    Manager Routes
    public static final String MANAGER = VERSION + "/manager";
    public static final String SCHEDULE = "/schedule";
    public static final String TIMEKEEPING_ID = "/{timekeeping_id}";

    //    Staff Routes
    public static final String STAFF = VERSION + "/staff";
    public static final String ORDER = "/orders";
    public static final String ORDER_ID = ORDER + "/{order_id}";
    public static final String ORDER_LINE_ID = "/{order_line_id}";
    public static final String PAY = "/bill";
    //    Food Routes
    public static final String FOOD = VERSION + "/foods";
    public static final String FOOD_ID = "/{food_id}";
    public static final String SEARCH = "/search";
    public static final String INGREDIENT = VERSION + "/ingredients";
    public static final String INGREDIENT_ID = "/{ingredient_id}";
    public static final String COMPOSITION = VERSION + "/compositions";
    public static final String COMPOSITION_ID = "/{composition_id}";
    public static final String INVENTORY = VERSION + "/inventory";

    //email
    public static final String EMAIL = "/mail";
    public static final String EMAIL_SEND = EMAIL + "/send";
    public static final String EMAIL_SEND_FILE = EMAIL + "/file";
}
