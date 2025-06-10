package com.example.foodrink.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrink.DBHelper;
import com.example.foodrink.R;
import com.example.foodrink.adapter.PopularRecipeAdapter;
import com.example.foodrink.model.RecipeResponse;
import com.example.foodrink.util.ThemeManager;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private View rootView;
    private DBHelper dbHelper;
    private RecyclerView recyclerViewPopular;
    private TextView tvIngredientCount, tvFavoriteCount;
    private ImageButton themeButton;
    private Button btnQuickSearch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views using the rootView
        recyclerViewPopular = rootView.findViewById(R.id.recyclerViewPopular);
        tvIngredientCount = rootView.findViewById(R.id.tvIngredientCount);
        tvFavoriteCount = rootView.findViewById(R.id.tvFavoriteCount);
        themeButton = rootView.findViewById(R.id.themeButton);
        btnQuickSearch = rootView.findViewById(R.id.btnQuickSearch);

        // Initialize database helper
        dbHelper = new DBHelper(getContext());

        // Set up RecyclerView with grid layout (2 columns)
        recyclerViewPopular.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Set up theme toggle button
        setupThemeButton();

        // Load user stats
        updateUserStats();

        // Load popular recipes
        loadPopularRecipes();

        // Set up quick search button
        btnQuickSearch.setOnClickListener(v -> {
            // Navigate to search fragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RecipeSearchFragment())
                    .addToBackStack(null)
                    .commit();
        });

        return rootView;
    }

    private void setupThemeButton() {
        // Update button icon based on current theme
        boolean isDarkMode = ThemeManager.isDarkMode(requireContext());
        themeButton.setImageResource(isDarkMode ? R.drawable.ic_theme_dark : R.drawable.ic_theme_light);

        // Set click listener to toggle theme
        themeButton.setOnClickListener(v -> {
            ThemeManager.toggleTheme(requireContext());
            // Update button image
            boolean newDarkMode = ThemeManager.isDarkMode(requireContext());
            themeButton.setImageResource(newDarkMode ? R.drawable.ic_theme_dark : R.drawable.ic_theme_light);
        });
    }

    private void updateUserStats() {
        // Count ingredients
        int ingredientCount = countIngredients();
        tvIngredientCount.setText("Anda memiliki " + ingredientCount + " bahan makanan");

        // Count favorites
        int favoriteCount = countFavorites();
        tvFavoriteCount.setText(favoriteCount + " resep favorit");
    }

    private int countIngredients() {
        int count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM ingredients", null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        } finally {
            db.close();
        }
        return count;
    }

    private int countFavorites() {
        int count = 0;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try {
            Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM favorites", null);
            if (cursor.moveToFirst()) {
                count = cursor.getInt(0);
            }
            cursor.close();
        } finally {
            db.close();
        }
        return count;
    }

    private void loadPopularRecipes() {
        List<RecipeResponse> popularRecipes = new ArrayList<>();
        DBHelper dbHelper = new DBHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        try {
            Cursor cursor = db.rawQuery("SELECT * FROM popular_recipes", null);
            Log.d("HomeFragment", "Number of recipes found: " + cursor.getCount());

            if (cursor.getCount() == 0) {
                // No recipes found, populate the table
                Log.d("HomeFragment", "No recipes found, populating table");
                dbHelper.populatePopularRecipes(db);

                // Query again after populating
                cursor.close();
                cursor = db.rawQuery("SELECT * FROM popular_recipes", null);
                Log.d("HomeFragment", "After population: " + cursor.getCount());
            }

            if (cursor.moveToFirst()) {
                do {
                    int recipeIdIndex = cursor.getColumnIndex("recipe_id");
                    int titleIndex = cursor.getColumnIndex("title");
                    int imageUrlIndex = cursor.getColumnIndex("image_url");

                    if (recipeIdIndex >= 0 && titleIndex >= 0 && imageUrlIndex >= 0) {
                        int id = cursor.getInt(recipeIdIndex);
                        String title = cursor.getString(titleIndex);
                        String imageUrl = cursor.getString(imageUrlIndex);

                        popularRecipes.add(new RecipeResponse(id, title, imageUrl));
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } finally {
            db.close();
        }

        recyclerViewPopular.setHasFixedSize(true);
        // Set a GridLayoutManager with 2 columns
        recyclerViewPopular.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Set adapter with the recipes
        PopularRecipeAdapter adapter = new PopularRecipeAdapter(popularRecipes);
        recyclerViewPopular.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh user stats when fragment is resumed
        updateUserStats();
    }
}