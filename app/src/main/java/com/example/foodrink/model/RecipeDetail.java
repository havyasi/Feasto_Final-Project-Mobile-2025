package com.example.foodrink.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RecipeDetail {
    @SerializedName("id")
    private int id;

    @SerializedName("title")
    private String title;

    @SerializedName("image")
    private String image;

    @SerializedName("summary")
    private String summary;

    @SerializedName("instructions")
    private String instructions;

    @SerializedName("extendedIngredients")
    private List<ExtendedIngredient> extendedIngredients;

    public static class ExtendedIngredient {
        @SerializedName("original")
        private String original;

        public String getOriginal() {
            return original;
        }
    }

    // Getters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getImage() { return image; }
    public String getSummary() { return summary; }
    public String getInstructions() { return instructions; }
    public List<ExtendedIngredient> getExtendedIngredients() { return extendedIngredients; }
}