package com.example.foodrink;

import com.example.foodrink.model.RecipeDetail;
import com.example.foodrink.model.RecipeResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("recipes/findByIngredients")
    Call<List<RecipeResponse>> getRecipesByIngredients(
            @Query("ingredients") String ingredients,
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/{id}/information")
    Call<RecipeDetail> getRecipeDetails(
            @Path("id") int id,
            @Query("apiKey") String apiKey
    );

    @GET("recipes/random")
    Call<List<RecipeResponse>> getRandomRecipes(
            @Query("number") int number,
            @Query("apiKey") String apiKey
    );
}

