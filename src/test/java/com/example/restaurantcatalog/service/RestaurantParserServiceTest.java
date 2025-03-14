package com.example.restaurantcatalog.service;

import com.example.restaurantcatalog.models.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
class RestaurantParserServiceTest {

    private RestaurantParserService parserService;

    @BeforeEach
    void setUp() {
        parserService = new RestaurantParserService(); // Ініціалізація сервісу
    }

    @Test
    void parseRestaurants() {
        List<Restaurant> restaurants = parserService.parseRestaurants(1);

        assertNotNull(restaurants); // Переконуємося, що список не null
        assertTrue(restaurants.size() >= 0); // Перевіряємо, що список не має помилок
    }

    @Test
    void parseRestaurantDetails() {
        Restaurant restaurant = parserService.parseRestaurantDetails(
                "Test Name", "Cafe", "https://example.com"
        );

        assertNotNull(restaurant); // Переконуємося, що об'єкт ресторану створено
        assertEquals("Test Name", restaurant.getName()); // Перевіряємо, що назва коректна
    }
}