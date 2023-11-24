package com.ooadprojectserver.restaurantmanagement.constant;

public class APIConstant {
    // Common Routes
    public static final String VERSION = "/api/v1";
    public static final String SEARCH = "/search";
    public static final String ID = "/{id}";

    // Auth Routes
    public static final String AUTH = VERSION + "/auth";
    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";
    public static final String REFRESH_TOKEN = "/refresh-token";

    // Users Routes
    public static final String USERS = VERSION + "/users";
    public static final String USER_ID = "/{userId}";
    public static final String PROFILE = "/profile";

    // Timekeeping Routes
    public static final String SCHEDULES = VERSION + "/schedules";
    public static final String TIMEKEEPING_ID = "/{timekeeping_id}";
    public static final String MANAGER = "/manager";
    public static final String STAFF = "/staff";
    public static final String STAFF_ID = "/{staffId}";

    // Order Routes
    public static final String ORDERS = VERSION + "/orders";
    public static final String ORDER_ID = "/{order_id}";
    public static final String ORDER_LINES = VERSION + "/order-lines";

    public static final String ORDER_LINE_ID = "/{order_line_id}";
    public static final String TABLES = VERSION + "/tables";
    public static final String TABLE_ID = "/{table_id}";

    // Payment Routes
    public static final String PAYMENTS = VERSION + "/payments";
    public static final String TRANSFER_METHODS = VERSION + "/transfer-methods";

    // Discount Routes
    public static final String DISCOUNTS = VERSION + "/discounts";

    // Bill Routes
    public static final String BILLS = VERSION + "/bills";

    // Food Routes
    public static final String FOODS = VERSION + "/foods";
    public static final String FOOD_ID = "/{food_id}";

    // Ingredient Routes
    public static final String INGREDIENT = VERSION + "/ingredients";
    public static final String INGREDIENT_ID = "/{ingredient_id}";

    // Inventory Routes
    public static final String INVENTORY = VERSION + "/inventory";
    public static final String INVENTORY_ID = "/{inventory_id}";
    public static final String STOCK = "/stock";


    // Email Routes
    public static final String EMAIL = VERSION + "/email";
    public static final String EMAIL_SEND = "/send";
    public static final String EMAIL_SEND_FILE = "/send-file";
}
