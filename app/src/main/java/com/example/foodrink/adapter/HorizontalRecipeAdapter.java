package com.example.foodrink.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodrink.R;
import com.example.foodrink.RecipeDetailActivity;
import com.example.foodrink.model.RecipeResponse;

import java.util.List;

public class HorizontalRecipeAdapter extends RecyclerView.Adapter<HorizontalRecipeAdapter.ViewHolder> {

    private List<RecipeResponse> recipeList;

    public HorizontalRecipeAdapter(List<RecipeResponse> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_horizontal_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecipeResponse recipe = recipeList.get(position);
        holder.title.setText(recipe.getTitle());

        // Set ingredient match text
        int usedCount = recipe.getUsedCount();
        holder.ingredientMatch.setText("Menggunakan " + usedCount + " bahan pantry");

        Glide.with(holder.itemView.getContext())
                .load(recipe.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);

        // Set click listener to open detail activity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getId());
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView ingredientMatch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ivRecipeImage);
            title = itemView.findViewById(R.id.tvRecipeTitle);
            ingredientMatch = itemView.findViewById(R.id.tvIngredientMatch);
        }
    }
}