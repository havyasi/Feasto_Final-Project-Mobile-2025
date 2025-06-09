package com.example.foodrink.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodrink.DBHelper;
import com.example.foodrink.R;
import com.example.foodrink.RecipeDetailActivity;
import com.example.foodrink.model.FavoriteRecipe;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<FavoriteRecipe> favoriteList;
    private Context context; // Add context field

    public FavoriteAdapter(List<FavoriteRecipe> favoriteList) {
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext(); // Initialize context here
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteRecipe recipe = favoriteList.get(position);
        holder.title.setText(recipe.getTitle());

        Glide.with(holder.itemView.getContext())
                .load(recipe.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.image);

        // Click to view recipe details
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getRecipeId());
            holder.itemView.getContext().startActivity(intent);
        });

        // Long-press to delete with confirmation
        holder.itemView.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
            builder.setTitle("Hapus Resep Favorit")
                    .setMessage("Apakah Anda yakin ingin menghapus resep ini dari favorit?")
                    .setPositiveButton("Ya", (dialog, which) -> {
                        deleteItem(position);
                    })
                    .setNegativeButton("Tidak", (dialog, which) -> {
                        dialog.dismiss();
                    })
                    .show();
            return true;
        });
    }

    // Fixed delete method using stored context
    public void deleteItem(int position) {
        if (position >= 0 && position < favoriteList.size()) {
            FavoriteRecipe recipe = favoriteList.get(position);

            // Delete from database
            DBHelper dbHelper = new DBHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("favorites", "id = ?", new String[]{String.valueOf(recipe.getId())});
            db.close();

            // Remove from list and update UI
            favoriteList.remove(position);
            notifyItemRemoved(position);
        }
    }


    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.ivFavoriteImage);
            title = itemView.findViewById(R.id.tvFavoriteTitle);
        }
    }
}