package com.example.foodrink.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvIngredientName);
            details = itemView.findViewById(R.id.tvIngredientDetails);
        }
    }
}