package com.example.foodrink.model;

public class Ingredient {
    private int id;
    private String name;
    private int quantity;
    private String unit;
    private String storageLocation;
    private String category;
    private String imageUrl;

    public Ingredient(int id, String name, int quantity, String unit, String storageLocation, String category, String imageUrl) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.storageLocation = storageLocation;
        this.category = category;
        this.imageUrl = imageUrl;
    }

    // Keep existing constructors for backward compatibility
    public Ingredient(int id, String name, int quantity, String unit, String storageLocation, String category) {
        this(id, name, quantity, unit, storageLocation, category, null);
    }

    public Ingredient(int id, String name, int quantity, String unit, String storageLocation) {
        this(id, name, quantity, unit, storageLocation, "", null);
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getStorageLocation() { return storageLocation; }
    public String getCategory() { return category; }
    public String getImageUrl() {
        return imageUrl;
    }
}