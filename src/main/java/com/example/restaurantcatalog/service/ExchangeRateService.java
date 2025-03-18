package com.example.restaurantcatalog.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;

// Сервісний клас для отримання курсу валют
@Service
public class ExchangeRateService {

    // URL API ПриватБанку для отримання курсів валют
    private static final String PRIVATBANK_API_URL =
            "https://api.privatbank.ua/p24api/pubinfo?json&exchange&coursid=5";

    /*
     * Метод отримує курси валют (USD та EUR)
     * Повертає HTML-таблицю з курсами валют або повідомлення про помилку.
     */
    public String getExchangeRates() {
        // Клієнт для виконання HTTP-запитів
        RestTemplate restTemplate = new RestTemplate();
        // Виконуємо GET-запит
        ResponseEntity<String> response = restTemplate.
                getForEntity(PRIVATBANK_API_URL, String.class);

        // Перевіряємо, чи успішний запит (код 2хх)
        if (response.getStatusCode().is2xxSuccessful()) {
            try {
                // Парсимо JSON-відповідь
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode jsonArray = objectMapper.readTree(response.getBody());

                // Змінні для зберігання курсів валют
                double usdBuy = 0.0, usdSale = 0.0;
                double eurBuy = 0.0, eurSale = 0.0;

                // Проходимо по масиву валют
                for (JsonNode currency : jsonArray) {
                    // Якщо валюта - долар
                    if ("USD".equals(currency.get("ccy").asText())) {
                        usdBuy = currency.get("buy").asDouble();
                        usdSale = currency.get("sale").asDouble();
                    // Якщо валюта - євро
                    } else if ("EUR".equals(currency.get("ccy").asText())) {
                        eurBuy = currency.get("buy").asDouble();
                        eurSale = currency.get("sale").asDouble();
                    }
                }

                // Формуємо HTML-таблицю з отриманими курсами
                return String.format(
                        "<table class='exchange-table'>" +
                                "<tr><th>Валюта</th><th>Купівля</th><th>Продаж</th></tr>" +
                                "<tr><td>USD</td><td>%.2f</td><td>%.2f</td></tr>" +
                                "<tr><td>EUR</td><td>%.2f</td><td>%.2f</td></tr>" +
                                "</table>",
                        usdBuy, usdSale, eurBuy, eurSale
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // Якщо запит не вдався, повертаємо повідомлення про помилку
        return "<p class='error'>Не вдалося отримати курс валют</p>";
    }
}
