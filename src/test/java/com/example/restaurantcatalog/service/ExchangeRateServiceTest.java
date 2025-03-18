package com.example.restaurantcatalog.service;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ExchangeRateServiceTest {

    private ExchangeRateService exchangeRateService;

    @Before
    public void setUp() {
        exchangeRateService = new ExchangeRateService();
    }

    @Test
    public void testGetExchangeRates_ReturnsHtml() {
        String response = exchangeRateService.getExchangeRates();
        assertNotNull(response);
        assertTrue(response.contains("Валюта"));
    }
}
