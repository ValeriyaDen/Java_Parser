package com.example.restaurantcatalog.controllers;

import com.example.restaurantcatalog.models.Restaurant;
import com.example.restaurantcatalog.repository.RestaurantRepository;
import com.example.restaurantcatalog.service.ExcelExportService;
import com.example.restaurantcatalog.service.ExchangeRateService;
import com.example.restaurantcatalog.service.RestaurantParserService;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RestaurantControllerTest {

    private RestaurantRepository restaurantRepository;
    private RestaurantParserService parserService;
    private ExchangeRateService exchangeRateService;
    private ExcelExportService excelExportService;
    private RestaurantController controller;

    @Before
    public void setUp() {
        restaurantRepository = mock(RestaurantRepository.class);
        parserService = mock(RestaurantParserService.class);
        exchangeRateService = mock(ExchangeRateService.class);
        excelExportService = mock(ExcelExportService.class);
        controller = new RestaurantController(restaurantRepository, parserService, exchangeRateService, excelExportService);
    }

    @Test
    public void testGetAllRestaurants_ReturnsEmptyList() {
        when(restaurantRepository.findAll()).thenReturn(List.of());

        List<Restaurant> result = controller.getAllRestaurants();
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testParseAndSaveRestaurants_ReturnsSavedRestaurants() {
        List<Restaurant> parsedRestaurants = List.of(
                new Restaurant(1L, "Test Restaurant", "Cafe", "Main Street", 4.5, "Ukrainian", "https://test.com")
        );

        when(parserService.parseRestaurants(2)).thenReturn(parsedRestaurants);
        when(restaurantRepository.existsByName("Test Restaurant")).thenReturn(false);
        when(restaurantRepository.saveAll(parsedRestaurants)).thenReturn(parsedRestaurants);

        ResponseEntity<List<Restaurant>> response = controller.parseAndSaveRestaurants(2);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Restaurant", response.getBody().get(0).getName());
    }

    @Test
    public void testExportRestaurantsToExcel_WhenDataExists() {
        byte[] excelData = new byte[]{1, 2, 3}; // Імітуємо не порожній Excel-файл
        when(excelExportService.generateExcelReport()).thenReturn(excelData);

        ResponseEntity<byte[]> response = controller.exportRestaurantsToExcel();
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(excelData.length, response.getBody().length);
    }

    @Test
    public void testExportRestaurantsToExcel_WhenNoData() {
        when(excelExportService.generateExcelReport()).thenReturn(null);

        ResponseEntity<byte[]> response = controller.exportRestaurantsToExcel();
        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    public void testGetExchangeRates_ReturnsHtml() {
        String mockResponse = "<table><tr><td>USD</td><td>39.5</td></tr></table>";
        when(exchangeRateService.getExchangeRates()).thenReturn(mockResponse);

        String response = controller.getExchangeRates();
        assertNotNull(response);
        assertTrue(response.contains("USD"));
    }
}
