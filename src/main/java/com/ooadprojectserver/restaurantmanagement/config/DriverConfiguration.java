package com.ooadprojectserver.restaurantmanagement.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.DriverManager;

/**
 * @author : Nguyen Van Quoc Tuan
 * @date: 4/9/2024, Tuesday
 * @description:
 **/
@Component
@RequiredArgsConstructor
public class DriverConfiguration {


    /*
    * This is a simple ApplicationRunner bean that logs all the drivers
    * that are registered with the DriverManager.
    * */
    @Bean
    ApplicationRunner drivesRunner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {

                DriverManager.getDrivers().asIterator().forEachRemaining(
                        driver -> {
                        }
                );

            }
        };
    }
}
