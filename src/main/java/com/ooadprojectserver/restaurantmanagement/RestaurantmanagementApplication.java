package com.ooadprojectserver.restaurantmanagement;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RestaurantmanagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestaurantmanagementApplication.class, args);
	}
}
