package com.example.foodrink.model;

import com.google.gson.annotations.SerializedName;

public class RecipeResponse {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    @SerializedName("usedIngredientCount")
    private int usedCount;

    @SerializedName("missedIngredientCount")
    private int missedCount;

    // Default constructor needed for Retrofit
    public RecipeResponse() {}

    // Constructor for database operations
    public RecipeResponse(int id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
        // Default values for unused fields
        this.usedCount = 0;
        this.missedCount = 0;
    }

    // Getter & Setter methods
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getImage() { return image; }
    public int getUsedCount() { return usedCount; }
    public int getMissedCount() { return missedCount; }
}