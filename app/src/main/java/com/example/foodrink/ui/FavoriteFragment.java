package com.example.foodrink.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrink.DBHelper;
import com.example.foodrink.R;
import com.example.foodrink.adapter.FavoriteGridAdapter;
import com.example.foodrink.model.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvEmptyFavorites;
    private View progressBar;
    private FavoriteGridAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        tvEmptyFavorites = view.findViewById(R.id.tvEmptyFavorites);
        progressBar = view.findViewById(R.id.progressBar);

        // Set up RecyclerView with a GridLayoutManager with 2 columns
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.setHasFixedSize(true);

        // Load favorites
        loadFavorites();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh favorites when returning to this fragment
        loadFavorites();
    }

    private void loadFavorites() {
        progressBar.setVisibility(View.VISIBLE);

        List<RecipeResponse> favoriteRecipes = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT recipe_id, title, image_url FROM favorites", null);

            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex("recipe_id");
                    int titleIndex = cursor.getColumnIndex("title");
                    int imageIndex = cursor.getColumnIndex("image_url");

                    if (idIndex >= 0 && titleIndex >= 0 && imageIndex >= 0) {
                        int id = cursor.getInt(idIndex);
                        String title = cursor.getString(titleIndex);
                        String imageUrl = cursor.getString(imageIndex);

                        favoriteRecipes.add(new RecipeResponse(id, title, imageUrl));
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } finally {
            db.close();
        }

        // Update UI based on results
        progressBar.setVisibility(View.GONE);

        if (favoriteRecipes.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            tvEmptyFavorites.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            tvEmptyFavorites.setVisibility(View.GONE);

            // Set or update adapter
            if (adapter == null) {
                adapter = new FavoriteGridAdapter(favoriteRecipes);
                recyclerView.setAdapter(adapter);
            } else {
                adapter.updateData(favoriteRecipes);
            }
        }
    }
}