package com.example.restaurantcatalog.repository;

import com.example.restaurantcatalog.models.Restaurant;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) // Додаємо Spring Runner для тестування
@SpringBootTest // Запускає Spring Boot-контекст для тестування
public class RestaurantRepositoryTest {

    @Autowired
    private RestaurantRepository restaurantRepository; // Автоматично створює репозиторій

    @Before
    public void setUp() {
        restaurantRepository.save(new Restaurant(null, "Test Restaurant", "Cafe", "Test Address", 4.5, "Italian", "https://test.com"));
    }

    @Test
    public void testExistsByName() {
        boolean exists = restaurantRepository.existsByName("Test Restaurant");
        assertTrue(exists); // Має бути true, бо такий ресторан є в базі
    }
}