package com.example.foodrink.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodrink.R;
import com.example.foodrink.model.Ingredient;
import com.example.foodrink.ui.IngredientDetailFragment;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder> {

    private List<Ingredient> ingredientList;
    private FragmentActivity activity;

    public IngredientAdapter(List<Ingredient> ingredientList, FragmentActivity activity) {
        this.ingredientList = ingredientList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ingredient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        holder.name.setText(ingredient.getName());
        holder.details.setText(ingredient.getQuantity() + " " + ingredient.getUnit());

        // Load image using Glide
        if (ingredient.getImageUrl() != null && !ingredient.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(Uri.parse(ingredient.getImageUrl()))
                    .placeholder(R.drawable.ic_pantry)
                    .into(holder.image);
        } else {
            holder.image.setImageResource(R.drawable.ic_pantry);
        }

        // Tambahkan click listener untuk membuka detail
        holder.itemView.setOnClickListener(v -> {
            IngredientDetailFragment detailFragment = IngredientDetailFragment.newInstance(ingredient.getId());
            activity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return ingredientList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, details;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvIngredientName);
            details = itemView.findViewById(R.id.tvIngredientDetails);
            image = itemView.findViewById(R.id.ingredientImage);
        }
    }
}