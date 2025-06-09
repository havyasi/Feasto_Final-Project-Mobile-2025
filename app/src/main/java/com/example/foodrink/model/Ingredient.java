package com.example.foodrink.model;

public class Ingredient {
    private int id;
    private String name;
    private int quantity;
    private String unit;
    private String storageLocation;
    private String category;

    public Ingredient(int id, String name, int quantity, String unit, String storageLocation, String category) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.storageLocation = storageLocation;
        this.category = category;
    }

    // Constructor yang kompatibel dengan kode yang sudah ada
    public Ingredient(int id, String name, int quantity, String unit, String storageLocation) {
        this(id, name, quantity, unit, storageLocation, "");
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public String getUnit() { return unit; }
    public String getStorageLocation() { return storageLocation; }
    public String getCategory() { return category; }
}