package com.example.foodrink.model;

public class FavoriteRecipe {
    private int id;
    private int recipeId;
    private String title;
    private String imageUrl;
    private String summary;
    private String ingredients;
    private String instructions;
    private String savedDate;

    public FavoriteRecipe(int id, int recipeId, String title, String imageUrl, String summary, String ingredients, String instructions, String savedDate) {
        this.id = id;
        this.recipeId = recipeId;
        this.title = title;
        this.imageUrl = imageUrl;
        this.summary = summary;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.savedDate = savedDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getSavedDate() {
        return savedDate;
    }

    public void setSavedDate(String savedDate) {
        this.savedDate = savedDate;
    }

}
