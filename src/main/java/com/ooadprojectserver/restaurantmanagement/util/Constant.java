package com.ooadprojectserver.restaurantmanagement.util;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Class Name: Constant.java
 *
 * @brief: Define constant
 */
public class Constant {
    // API FORMAT DATE
    public static final String API_FORMAT_DATE = "yyyy/MM/dd HH:mm:ss";

    // LANG
//    public static final String LANG_DEFAULT = "en"; // Lang default
//    public static final String LANG_AUTO = "auto";

    //TIME REPORT
    public static enum TIME_REPORT {
        LAST_WEEK,
        LAST2WEEKS,
        LAST_MONTH,
        LAST_3MONTHS,
        LAST_6MONTHS,
        LAST_YEAR
    }

    // ACCOUNT STATUS
    @RequiredArgsConstructor
    @Getter
    public static enum STATUS {
        ACTIVE_STATUS(1,"Active"),
        DELETED_STATUS(2, "Deleted"),
        REVOKE_STATUS(3,"Revoke"),
        DISABLED_STATUS(4,"Disable"),
        DELETED_FOREVER_STATUS(5,"Deleted forever"),
        PENDING(6,"Pending"),
        TRIAL_ACCOUNT_STATUS(7,"Trial");

        private final Integer value;
        private final String type;
    }


    //PERMISSION ROLE
    @RequiredArgsConstructor
    @Getter
    public static enum PERMISSION {

        //owner's permission
        OWNER_READ("owner:read"),
        OWNER_UPDATE("owner:update"),
        OWNER_CREATE("owner:create"),
        OWNER_DELETE("owner:delete"),

        //manager's permission
        MANAGER_READ("management:read"),
        MANAGER_UPDATE("management:update"),
        MANAGER_CREATE("management:create"),
        MANAGER_DELETE("management:delete"),

        //staff's permission
        STAFF_READ("staff:read"),
        STAFF_UPDATE("staff:update"),
        STAFF_CREATE("staff:create"),
        STAFF_DELETE("staff:delete")
        ;

        private final String permission;
    }




    @RequiredArgsConstructor
    @Getter
    public static enum ROLE{
        STAFF(1, Set.of(
                PERMISSION.STAFF_READ,
                PERMISSION.STAFF_UPDATE,
                PERMISSION.STAFF_CREATE,
                PERMISSION.STAFF_DELETE
        )),

        MANAGER(2, Set.of(
                PERMISSION.MANAGER_READ,
                PERMISSION.MANAGER_UPDATE,
                PERMISSION.MANAGER_CREATE,
                PERMISSION.MANAGER_DELETE,
                PERMISSION.STAFF_READ,
                PERMISSION.STAFF_UPDATE,
                PERMISSION.STAFF_CREATE,
                PERMISSION.STAFF_DELETE
        )),
        OWNER(3, Set.of(
                PERMISSION.OWNER_READ,
                PERMISSION.OWNER_UPDATE,
                PERMISSION.OWNER_CREATE,
                PERMISSION.OWNER_DELETE,
                PERMISSION.MANAGER_READ,
                PERMISSION.MANAGER_UPDATE,
                PERMISSION.MANAGER_CREATE,
                PERMISSION.MANAGER_DELETE
        ));

        private final Integer value;

        private final Set<PERMISSION> permissions;

        public List<SimpleGrantedAuthority> getAuthorities() {
            var authorities = getPermissions()
                    .stream()
                    .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                    .collect(Collectors.toList());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
            return authorities;
        }
    }

    //TIME
    public static final long ONE_MINUTE_IN_MILLIS = 60000;
    public static final long ONE_SECOND_IN_MILLIS = 1000;
    public static final long JWT_EXPIRATION_MILLISECONDS = 86400000; //a day
    public static final long REFRESH_JWT_EXPIRATION_MILLISECONDS = 604800000; //7 days
    public static final long DEFAULT_REMEMBER_LOGIN_MILLISECONDS = 1296000000; // 15 days
    public static final long DEFAULT_SESSION_TIME_OUT = 1800000; // 30 minutes

    // define paging results, use for default value of @RequestParam, so type of data is String
    public static final String DEFAULT_PAGE_SIZE = "25";
    public static final String DEFAULT_PAGE_NUMBER = "0";

    @RequiredArgsConstructor
    @Getter
    public static enum ParamError {
        MISSING_USERNAME_AND_EMAIL("accountName", "Missing both user name and email address"),
        USER_NAME("userName", "Invalid user name"),
        EMAIL_ADDRESS("email", "Invalid email address"),
        PASSWORD("passwordHash", "Invalid password hash"),
        PHONE_NUMBER("phone", "Invalid phone number"),
        FIRST_NAME("firstName", "Invalid first name"),
        LAST_NAME("lastName", "Invalid last name"),
        APP_NAME("appName", "Invalid app name"),
        APP_DOMAIN("appDomain", "Invalid app domain"),
        SERVER_KEY("serverKey", "Invalid server key"),
        TOKEN_EXPIRE_DURATION("tokenExpireDuration", "Invalid token expiry duration"),
        REDIRECT_URL("redirectUrl", "Invalid redirect URL"),
        ROLE_NAME("roleName", "Invalid role name"),
        ROLE_DESC("roleDescription", "Invalid role description");

        private final String name;
        private final String desc;
    }


    @RequiredArgsConstructor
    @Getter
    public static enum ORDER_STATUS {
        PENDING(0),
        SHIPPING(1),
        COMPLETED(2),
        CANCEL(3);

        private final int status;
    }

    @RequiredArgsConstructor
    @Getter
    public static enum TIMEKEEPING_STATUS {
        ABSENCE(0),
        LATE(1);

        private final int status;
    }

}
