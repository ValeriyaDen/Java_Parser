package com.example.restaurantcatalog.service;

import com.example.restaurantcatalog.models.Restaurant;
import com.example.restaurantcatalog.repository.RestaurantRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ExcelExportServiceTest {

    private ExcelExportService excelExportService;
    private RestaurantRepository restaurantRepository;

    @Before
    public void setUp() {
        restaurantRepository = mock(RestaurantRepository.class); // Імітація репозиторію
        excelExportService = new ExcelExportService(restaurantRepository);
    }

    @Test
    public void testGenerateExcelReport_ReturnsNullIfNoData() {
        when(restaurantRepository.findAll()).thenReturn(List.of()); // Порожній список

        byte[] report = excelExportService.generateExcelReport();
        assertNull(report);
    }

    @Test
    public void testGenerateExcelReport_ReturnsDataIfRestaurantsExist() {
        List<Restaurant> mockRestaurants = List.of(
                new Restaurant(1L, "Test Restaurant", "Fast Food", "123 Main St", 4.5, "American", "https://test.com")
        );
        when(restaurantRepository.findAll()).thenReturn(mockRestaurants); // Симуляція даних у базі

        byte[] report = excelExportService.generateExcelReport();
        assertNotNull(report);
        assertTrue(report.length > 0);
    }
}