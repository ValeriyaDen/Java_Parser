package com.example.restaurantcatalog.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "restaurants") // Об'єкти цього класу будуть збережені в таблиці `restaurants`
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id // Поле `id` є унікальним ідентифікатором (первинний ключ)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Генерує `id` автоматично (AUTO_INCREMENT у базі даних)
    private Long id;

    // Поле `name`, `category`, `address` не може бути `null`
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String category;
    @Column(nullable = false)
    private String address;

    private double rating;  // Рейтинг ресторану
    private String cuisine; // Тип кухні
    private String site;    // Сайт ресторану
}

