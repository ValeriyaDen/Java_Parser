package com.example.restaurantcatalog.service;

import com.example.restaurantcatalog.models.Restaurant;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class RestaurantParserServiceTest {

    private RestaurantParserService parserService;

    @Before
    public void setUp() {
        parserService = new RestaurantParserService(null); // Не зберігаємо в базу
    }

    @Test
    public void testScrapeRestaurants_ReturnsList() {
        List<Restaurant> restaurants = parserService.parseRestaurants(1);

        assertNotNull(restaurants);
        assertTrue(restaurants.size() >= 0);
    }
}
