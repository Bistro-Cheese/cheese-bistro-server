package com.ooadprojectserver.restaurantmanagement.util;

public class APIName {
    // version
    public static final String VERSION = "api/v1";

    // charset
    public static final String CHARSET = "application/json;charset=utf-8";

    /** action user **/
    public static final String LOGIN = VERSION + "/user/login";

    public static final String REGISTER = VERSION + "/user/register";

    public static final String LOGOUT = VERSION + "/user/logout";

    /** SSO **/
    public static final String OAUTH_LOGIN = VERSION + "/oauth/login";
    public static final String OAUTH_IMPLICIT_LOGIN = VERSION + "/oauth/implicit/login";
    public static final String CHECK_API_KEY = VERSION + "/oauth";

    /** product api links**/
    public static final String PRODUCTS = VERSION + "/products";

    public static final String PRODUCTS_BY_CATEGORY = "/category";


    //Find products by list of id
    public static final String PRODUCT_BY_ID = "/detail/{product_id}";

    public static final String PRODUCT_DETAILS = "/product_details/{product_id}";

    public static final String PRODUCT_ATTRIBUTES = VERSION + "/product-attributes";

    public static final String PRODUCT_CREATE = "/create";
    public static final String PRODUCTS_DELETE = "/delete";
    public static final String PRODUCTS_UPDATE = "/update";


    /** category api links **/
    public static final String CATEGORIES = VERSION + "/categories";
    public static final String CATEGORIES_ID = VERSION + "/categories/{id}";

    /** order api links **/
    public static final String ORDERS = VERSION + "/orders";
    public static final String ORDER_CREATE = "/create";
    public static final String ORDER_UPDATE = "/update";
    public static final String ORDERS_LIST = "/listOrder";
    public static final String CHANGE_STATUS_ORDERS = "/change/{order_id}/{status}";


}
