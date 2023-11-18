package com.ooadprojectserver.restaurantmanagement.util;

import java.sql.Timestamp;
import java.time.Instant;

public class DateTimeUtils {
    private DateTimeUtils() {
    }

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final String DATE_FORMAT2 = "dd/MM/yyyy";

    public static Timestamp resultTimestamp() {
        Instant instant = Instant.now();
        return Timestamp.from(instant);
    }
}
