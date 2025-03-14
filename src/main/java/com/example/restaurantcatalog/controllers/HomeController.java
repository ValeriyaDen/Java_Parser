package com.example.restaurantcatalog.controllers;

import com.example.restaurantcatalog.models.Restaurant;
import com.example.restaurantcatalog.repository.RestaurantRepository;
import com.example.restaurantcatalog.service.ExchangeRateService;
import com.example.restaurantcatalog.service.RestaurantParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor // Lombok автоматично генерує конструктор для фінальних змінних
public class HomeController {

    // Репозиторій для взаємодії з базою даних
    private final RestaurantRepository restaurantRepository;

    // Сервіс для парсингу ресторанів
    private final RestaurantParserService parserService;

    // Сервіс для отримання курсу валют
    private final ExchangeRateService exchangeRateService;

    /*
     * Головна сторінка сайту.
     * Завантажує всі ресторани з бази даних та передає їх у шаблон `index.html`.
     */
    @GetMapping("/")
    public String homePage(Model model) {
        List<Restaurant> restaurants = restaurantRepository.findAll(); // Отримуємо всі ресторани
        model.addAttribute("restaurants", restaurants); // Передаємо їх у шаблон
        return "index"; // Повертаємо назву HTML-файлу для відображення
    }

    /*
     * Обробляє натискання кнопки "Спарсити ресторани".
     * Парсить ресторани, фільтрує нові та додає їх у базу.
     */
    @PostMapping("/parse")
    public String parseRestaurants(@RequestParam(defaultValue = "2") int pageLimit) {
        List<Restaurant> parsedRestaurants = parserService.parseRestaurants(pageLimit); // Парсимо сторінки

        // Фільтруємо ресторани
        List<Restaurant> filteredRestaurants = parsedRestaurants.stream()
                .filter(r -> !restaurantRepository.existsByName(r.getName()))
                .toList();

        restaurantRepository.saveAll(filteredRestaurants); // Зберігаємо нові ресторани у базі
        return "redirect:/"; // Перенаправлення на головну сторінку для оновлення таблиці
    }

    /*
     * Обробляє натискання кнопки "Отримати курс валют".
     * Отримує курс та додає його у шаблон `index.html`.
     */
    @GetMapping("/rate")
    public String getExchangeRate(Model model) {
        String exchangeRateMessage = exchangeRateService.getExchangeRates(); // Отримуємо курс валют
        model.addAttribute("exchangeRateMessage", exchangeRateMessage); // Додаємо його в шаблон

        // Передаємо список ресторанів, щоб вони не зникали після оновлення сторінки
        List<Restaurant> restaurants = restaurantRepository.findAll();
        model.addAttribute("restaurants", restaurants);

        return "index"; // Повертаємо оновлену сторінку
    }
}
