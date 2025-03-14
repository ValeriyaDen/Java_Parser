package com.example.restaurantcatalog.repository;

import com.example.restaurantcatalog.models.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Позначаємо, що це репозиторій для роботи з базою даних
@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    /*
     * Перевіряє, чи існує ресторан із вказаною назвою.
     * Параметр name - це назва ресторану.
     * Повертає true, якщо ресторан з таким ім'ям вже є в базі.
     */
    boolean existsByName(String name);
}
