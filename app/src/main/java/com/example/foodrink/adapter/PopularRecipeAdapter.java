package com.example.foodrink.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodrink.R;
import com.example.foodrink.model.PopularRecipe;

import java.util.List;

public class PopularRecipeAdapter extends RecyclerView.Adapter<PopularRecipeAdapter.ViewHolder> {

    private final List<PopularRecipe> recipeList;

    public PopularRecipeAdapter(List<PopularRecipe> recipeList) {
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_popular_recipe, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PopularRecipe recipe = recipeList.get(position);

        // Set the recipe title
        holder.title.setText(recipe.getTitle());

        // Load image with Glide
        if (recipe.getImageUrl() != null && !recipe.getImageUrl().isEmpty()) {
            Glide.with(holder.image.getContext())
                    .load(recipe.getImageUrl())
                    .placeholder(R.drawable.ic_recipe)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_recipe);
        }
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        ViewHolder(View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ivPopularRecipeImage);
            title = itemView.findViewById(R.id.tvPopularRecipeTitle);
        }
    }
}