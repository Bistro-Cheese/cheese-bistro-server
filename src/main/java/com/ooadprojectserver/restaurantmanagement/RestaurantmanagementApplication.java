package com.ooadprojectserver.restaurantmanagement;
import com.ooadprojectserver.restaurantmanagement.model.user.Staff;
import com.ooadprojectserver.restaurantmanagement.service.factory.UserFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestaurantmanagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantmanagementApplication.class, args);
	}
}
