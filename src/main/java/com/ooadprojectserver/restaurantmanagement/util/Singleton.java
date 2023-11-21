package com.ooadprojectserver.restaurantmanagement.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Singleton {
    private static ObjectMapper objectMapper;

    public static ObjectMapper getObjectMapper() {
        if (DataUtil.isNullObject(objectMapper)) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper;
    }

}
