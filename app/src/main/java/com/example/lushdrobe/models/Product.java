package com.example.lushdrobe.models;

import java.io.Serializable;

public class Product implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;
    private String category;
    private int stock;
    private float rating;  // New field
    private String brand;  // New field

    public Product() {
        // Required empty constructor
    }

    // Updated constructor with all fields
    public Product(int id, String name, String description, double price,
                   String imageUrl, String category, int stock, float rating, String brand) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
        this.stock = stock;
        this.rating = rating;
        this.brand = brand;
    }

    // Constructor without rating and brand (for backward compatibility)
    public Product(int id, String name, String description, double price,
                   String imageUrl, String category, int stock) {
        this(id, name, description, price, imageUrl, category, stock, 0.0f, "");
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    // New methods for rating
    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    // New methods for brand
    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}