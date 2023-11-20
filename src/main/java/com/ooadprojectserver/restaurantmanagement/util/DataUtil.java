package com.ooadprojectserver.restaurantmanagement.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.BeanUtils;

import java.lang.reflect.Constructor;

public class DataUtil {
    private static final Logger log = LogManager.getLogger(DataUtil.class);

    public static <T> T copyProperties(Object source, Class<T> classTarget) {
        try {
            Constructor<?> cons = classTarget.getConstructor();
            Object target = cons.newInstance();


            BeanUtils.copyProperties(source, target);
            return (T) target;
        } catch (Exception e) {
            log.error("error copy properties: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static <T> T copyProperties(Object source, Class<T> classTarget, String... ignoreProperties) {
        try {
            Constructor<?> cons = classTarget.getConstructor();
            Object target = cons.newInstance();


            BeanUtils.copyProperties(source, target, ignoreProperties);
            return (T) target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
