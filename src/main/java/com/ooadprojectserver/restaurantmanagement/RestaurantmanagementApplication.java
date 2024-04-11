package com.ooadprojectserver.restaurantmanagement;

import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.sql.Driver;

@SpringBootApplication
@EnableScheduling
public class RestaurantmanagementApplication {
	public static void main(String[] args) {
		SpringApplication.run(RestaurantmanagementApplication.class, args);
	}

	/*
	* I try to create a DataSource using this method
	* but the DataSourceBuilder is not able to find the driver
	* */
	public static DataSource createDataSource(Class<? extends Driver> driverClassName,
								Class<? extends DataSource> type,
								String url, String username, String password,
								ClassLoader classLoader){
		return DataSourceBuilder.create(classLoader)
				.type(type)
				.driverClassName(driverClassName.getName())
				.username(username)
				.password(password)
				.build();
	}
}
