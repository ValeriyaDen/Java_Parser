package com.example.restaurantcatalog.controllers;

import com.example.restaurantcatalog.models.Restaurant;
import com.example.restaurantcatalog.repository.RestaurantRepository;
import com.example.restaurantcatalog.service.ExcelExportService;
import com.example.restaurantcatalog.service.RestaurantParserService;
import com.example.restaurantcatalog.service.ExchangeRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Контролер обробляє REST-запити для ресторанного каталогу
@RestController
@RequiredArgsConstructor // Автоматично створює конструктор для фінальних змінних
@RequestMapping("/api/restaurants") // Базовий шлях для всіх методів контролера
public class RestaurantController {

    // Репозиторій для взаємодії з базою даних
    private final RestaurantRepository restaurantRepository;

    // Сервіс для парсингу ресторанів
    private final RestaurantParserService parserService;

    // Сервіс для отримання курсу валют
    private final ExchangeRateService exchangeRateService;

    // Сервіс для експорту даних у Excel
    private final ExcelExportService excelExportService;

    /*
     * Отримує всі ресторани.
     * Повертає список всіх ресторанів у базі.
     */
    @GetMapping
    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    /*
     * Парсинг ресторанів з веб-сайту та збереження їх у базу.
     * Параметр pageLimit - це кількість сторінок для парсингу.
     * Повертає список збережених ресторанів.
     */
    @GetMapping("/parse")
    public ResponseEntity<List<Restaurant>> parseAndSaveRestaurants(@RequestParam(defaultValue = "2") int pageLimit) {
        List<Restaurant> parsedRestaurants = parserService.parseRestaurants(pageLimit); // Парсимо ресторани

        // Фільтруємо тільки нові ресторани, щоб уникнути дублікатів
        List<Restaurant> newRestaurants = parsedRestaurants.stream()
                .filter(r -> !restaurantRepository.existsByName(r.getName()))
                .toList();

        List<Restaurant> savedRestaurants = restaurantRepository.saveAll(newRestaurants); // Зберігаємо нові ресторани
        return ResponseEntity.ok(savedRestaurants);
    }

    /*
     * Отримати курс валют (USD та EUR).
     * Повертає HTML-таблицю з курсами валют.
     */
    @GetMapping("/exchange-rates")
    public String getExchangeRates() {
        return exchangeRateService.getExchangeRates();
    }

    /*
     * Експортувати ресторани у форматі Excel.
     * Повертає Excel-файл у відповідь.
     */
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportRestaurantsToExcel() {
        byte[] excelData = excelExportService.generateExcelReport(); // Генеруємо Excel-звіт

        if (excelData == null) {
            return ResponseEntity.internalServerError().build(); // Якщо звіт не згенерувався, повертаємо помилку 500
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Встановлюємо тип файлу
        headers.setContentDispositionFormData("attachment", "restaurants.xlsx"); // Встановлюємо ім'я файлу

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData); // Повертаємо файл у відповідь
    }
}
