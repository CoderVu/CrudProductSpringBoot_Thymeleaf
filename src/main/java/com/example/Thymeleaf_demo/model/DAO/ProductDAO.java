package com.example.Thymeleaf_demo.model.DAO;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public class ProductDAO {
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private Long id;
    private String name;
    private String brand;
    private String category;
    private double price;
    private String description;
    private LocalDateTime createdAt;
    private MultipartFile imageFile;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public MultipartFile getImageFile() {
        return imageFile;
    }

    public void setImageFile(MultipartFile imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isValid() {
        return name != null && !name.isEmpty() &&
                brand != null && !brand.isEmpty() &&
                category != null && !category.isEmpty() &&
                description != null && !description.isEmpty();
    }
}
