package com.example.restaurantcatalog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Головний клас Spring Boot-додатка
@SpringBootApplication
public class RestaurantCatalogApplication {

	/*
	 * Головний метод, який запускає Spring Boot-додаток.
	 */
	public static void main(String[] args) {
		SpringApplication.run(RestaurantCatalogApplication.class, args);
	}
}
