package com.example.foodrink.model;

public class PopularRecipe {
    private int recipeId;
    private String title;
    private String imageUrl;
    private String summary;

    public PopularRecipe(int recipeId, String title, String imageUrl) {
        this.recipeId = recipeId;
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public PopularRecipe(int recipeId, String title, String imageUrl, String summary) {
        this.recipeId = recipeId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.summary = summary;
    }

    public int getRecipeId() { return recipeId; }
    public String getTitle() { return title; }
    public String getImageUrl() { return imageUrl; }
    public String getSummary() {
        return summary != null ? summary : "";
    }
}