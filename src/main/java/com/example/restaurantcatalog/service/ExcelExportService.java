package com.example.restaurantcatalog.service;

import com.example.restaurantcatalog.models.Restaurant;
import com.example.restaurantcatalog.repository.RestaurantRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

// Сервісний клас для експорту даних у формат Excel
@Service
public class ExcelExportService {

    private final RestaurantRepository restaurantRepository;

    // Конструктор для автоматичної ініціалізації репозиторію через Spring
    public ExcelExportService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    private void createRow(Row row, int columnIndex, String value) {
        row.createCell(columnIndex).setCellValue(value != null ? value : "Не вказано");
    }

    /*
     * Генерує Excel-файл із даними про ресторани.
     * Повертає масив байтів або `null`, якщо даних немає.
     */
    public byte[] generateExcelReport() {
        // Отримуємо всі ресторани з бази
        List<Restaurant> restaurants = restaurantRepository.findAll();

        // Якщо ресторанів немає, повертаємо null і повідомлення в консоль
        if (restaurants.isEmpty()) {
            System.out.println("️Немає ресторанів у базі!");
            return null;
        }

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Створюємо аркуш Excel з назвою "Restaurants"
            Sheet sheet = workbook.createSheet("Restaurants");

            // Додаємо заголовки стовпців
            Row headerRow = sheet.createRow(0);
            String[] columns = {"ID", "Назва", "Адреса", "Рейтинг", "Кухня", "Сайт"};

            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]); // Встановлюємо назву стовпця
                cell.setCellStyle(createHeaderStyle(workbook)); // Застосовуємо стиль заголовка
            }

            // Додаємо дані ресторанів у таблицю
            int rowNum = 1;
            for (Restaurant restaurant : restaurants) {
                Row row = sheet.createRow(rowNum++);
                createRow(row, 0, String.valueOf(restaurant.getId()));
                createRow(row, 1, restaurant.getName());
                createRow(row, 2, restaurant.getAddress());
                createRow(row, 3, String.valueOf(restaurant.getRating()));
                createRow(row, 4, restaurant.getCuisine());
                createRow(row, 5, restaurant.getSite());
            }

            // Автоматично розширюємо стовпці
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // Записуємо дані у вихідний потік
            workbook.write(outputStream);
            return outputStream.toByteArray(); // Повертаємо готовий Excel-файл у вигляді байтів

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*
     * Створює стиль заголовка таблиці (жирний шрифт).
     * Повертає стиль комірки для заголовків
     */
    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true); // Робимо текст заголовка жирним
        headerStyle.setFont(font);
        return headerStyle;
    }
}
