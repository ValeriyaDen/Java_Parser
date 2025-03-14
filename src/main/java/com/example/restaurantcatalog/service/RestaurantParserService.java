package com.example.restaurantcatalog.service;

import com.example.restaurantcatalog.models.Restaurant;
import com.example.restaurantcatalog.repository.RestaurantRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// Сервіс для парсингу ресторанів із сайту
@Service
public class RestaurantParserService {

    // Базова URL-адреса каталогу ресторанів
    private static final String BASE_URL = "https://tomato.ua/ua/dnepr";

    // Репозиторій для взаємодії з базою даних
    private RestaurantRepository restaurantRepository;

    // Конструктор для автоматичної ініціалізації репозиторію
    public RestaurantParserService() {
        this.restaurantRepository = restaurantRepository;
    }

    /*
     * Парсить ресторани із вказаної кількості сторінок.
     * Параметр pageLimit - це кількість сторінок для обробки.
     * Повертає список ресторанів.
     */
    public List<Restaurant> parseRestaurants(int pageLimit) {
        List<Restaurant> restaurants = new ArrayList<>();
        int page = 1;

        while (page <= pageLimit) {
            try {
                // Формуємо URL для відповідної сторінки
                String url = BASE_URL + "?page=" + page;
                Document doc = Jsoup.connect(url).get(); // Завантажуємо HTML-код сторінки
                Elements restaurantCards = doc.select(".search-item__center-content"); // Вибираємо всі блоки з ресторанами

                if (restaurantCards.isEmpty()) {
                    break; // Якщо ресторанів на сторінці немає — припиняємо парсинг
                }

                System.out.println("Обробка сторінки: " + page);
                page++; // Переходимо до наступної сторінки

                for (Element card : restaurantCards) {
                    // Отримуємо назву ресторану
                    String name = card.select(".search-item__center-title.desctop").text();

                    // Отримуємо категорії (тип закладу)
                    Elements categoryElements = card.select(".search-item__center-content-item a.dark-text");
                    String categories = categoryElements.stream()
                            .map(Element::text)
                            .collect(Collectors.joining(", "));

                    // Отримуємо посилання на сторінку ресторану
                    String restaurantUrl = card.select("a[href]").attr("abs:href");

                    // Викликаємо додатковий метод для отримання детальної інформації про ресторан
                    Restaurant restaurant = parseRestaurantDetails(name, categories, restaurantUrl);
                    restaurants.add(restaurant);
                }

            } catch (IOException e) {
                e.printStackTrace();
                break; // Якщо сталася помилка, припиняємо парсинг
            }
        }
        return restaurants; // Повертаємо список зібраних ресторанів
    }

    /**
     * Отримує детальну інформацію про ресторан.
     * Параметр name - це назва ресторану.
     * Параметр category - це тип закладу.
     * Параметр restaurantUrl - це URL сторінки ресторану.
     * Повертає об'єкт `Restaurant` із зібраними даними.
     */
    Restaurant parseRestaurantDetails(String name, String category, String restaurantUrl) {
        try {
            Document doc = Jsoup.connect(restaurantUrl).get();

            // Отримуємо адресу ресторану
            String address = doc.select(".address").text();

            // Отримуємо тип кухні
            Elements cuisineElements = doc.select("div.p-0.desctop_show a.dark-text span[itemprop=servesCuisine]");
            String cuisine = cuisineElements.stream()
                    .map(Element::text)
                    .collect(Collectors.joining(", "));

            // Отримуємо посилання на офіційний сайт (якщо є)
            String site = doc.select(".rest-main-info__title a").attr("href");
            if (site.isEmpty()) {
                site = "Не вказано"; // Якщо немає сайту, пишемо "Не вказано"
            }

            // Отримуємо рейтинг ресторану
            Elements ratingElements = doc.select(".rest_block_raiting");
            String ratingText = ratingElements.stream()
                    .map(Element::text)
                    .findFirst() // Беремо тільки перше значення
                    .orElse("3.5") // Якщо немає рейтингу, ставимо 3.5 за замовчуванням
                    .trim();

            double rating;
            try {
                rating = Double.parseDouble(ratingText);
            } catch (NumberFormatException e) {
                rating = 3.5;
            }

            // Повертаємо об'єкт ресторану
            return new Restaurant(null, name, category, address, rating, cuisine, site);
        } catch (IOException e) {
            e.printStackTrace();
            return new Restaurant(null, "Невідомий", "Не вказано", "Невідомо", 3.5, "Невідомо", "Не вказано");
        }
    }
}
