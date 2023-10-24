package com.ooadprojectserver.restaurantmanagement.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class EmailConstant {
    public static final String HOST = "spring.mail.host";
    public static final String PORT = "spring.mail.port";
    //    private static final String PROTOCOL = "spring.mail.protocol";
    public static final String USERNAME = "spring.mail.username";
    public static final String PASSWORD = "spring.mail.password";
    public static final String UTF_8_ENCODING = "UTF-8";

}
