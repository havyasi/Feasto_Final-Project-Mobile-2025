package com.example.foodrink.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrink.DBHelper;
import com.example.foodrink.R;
import com.example.foodrink.adapter.PopularRecipeAdapter;
import com.example.foodrink.model.PopularRecipe;
import com.example.foodrink.util.GridSpacingItemDecoration;
import com.example.foodrink.util.ThemeManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView tvIngredientCount, tvFavoriteCount;
    private Button btnQuickSearch;
    private RecyclerView recyclerViewPopular;
    private DBHelper dbHelper;
    private PopularRecipeAdapter adapter;
    private List<PopularRecipe> popularRecipeList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find the theme toggle button
        ImageButton themeButton = view.findViewById(R.id.themeButton);

        // Update the button icon based on current theme
        updateThemeIcon(themeButton);

        // Set click listener for theme changes
        themeButton.setOnClickListener(v -> {
            boolean isDark = ThemeManager.isDarkTheme(requireContext());
            ThemeManager.setDarkTheme(requireContext(), !isDark);
            updateThemeIcon(themeButton);
        });

        tvIngredientCount = view.findViewById(R.id.tvIngredientCount);
        tvFavoriteCount = view.findViewById(R.id.tvFavoriteCount);
        btnQuickSearch = view.findViewById(R.id.btnQuickSearch);
        recyclerViewPopular = view.findViewById(R.id.recyclerViewPopular);

        dbHelper = new DBHelper(getContext());

        // Pastikan data dummy populer ada
        // ensurePopularRecipesExist();

        // Set up RecyclerView untuk popular recipes
        recyclerViewPopular.setLayoutManager(
                new GridLayoutManager(getContext(), 2));
        int spacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerViewPopular.addItemDecoration(new GridSpacingItemDecoration(2, spacing, true));
        recyclerViewPopular.setPadding(-25, 0, 0, 0);

        popularRecipeList = new ArrayList<>();

        // Load and display data
        loadPopularRecipes();
        updateCounts();

        // Set up quick search button - now navigates directly to Recipe page
        btnQuickSearch.setOnClickListener(v -> {
            RecipeSearchFragment searchFragment = new RecipeSearchFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, searchFragment)
                    .addToBackStack(null)
                    .commit();

            BottomNavigationView bottomNav = requireActivity().findViewById(R.id.bottom_navigation);
            bottomNav.setSelectedItemId(R.id.nav_recipe);
        });

        return view;
    }

    private void updateThemeIcon(ImageButton button) {
        boolean isDark = ThemeManager.isDarkTheme(requireContext());
        button.setImageResource(isDark ?
                R.drawable.ic_theme_dark :
                R.drawable.ic_theme_light);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Update counts every time fragment becomes visible
        updateCounts();
    }


    private void loadPopularRecipes() {
        popularRecipeList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Check if table is empty and populate it if needed
        Cursor checkCursor = db.rawQuery("SELECT COUNT(*) FROM popular_recipes", null);
        checkCursor.moveToFirst();
        int count = checkCursor.getInt(0);
        checkCursor.close();

        if (count == 0) {
            // Populate database with sample recipes
            db.close();
            SQLiteDatabase writeDb = dbHelper.getWritableDatabase();
            dbHelper.populatePopularRecipes(writeDb);
            writeDb.close();
            db = dbHelper.getReadableDatabase();
        }

        // Query popular recipes
        Cursor cursor = db.rawQuery("SELECT * FROM popular_recipes", null);

        if (cursor.moveToFirst()) {
            do {
                int recipeId = cursor.getInt(cursor.getColumnIndex("recipe_id"));
                String title = cursor.getString(cursor.getColumnIndex("title"));
                String imageUrl = cursor.getString(cursor.getColumnIndex("image_url"));

                PopularRecipe recipe = new PopularRecipe(recipeId, title, imageUrl);
                popularRecipeList.add(recipe);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // Set adapter with the data
        recyclerViewPopular.setAdapter(new PopularRecipeAdapter(popularRecipeList));
    }

    private void updateIngredientCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM ingredients", null);

        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        db.close();

        tvIngredientCount.setText("Anda memiliki " + count + " bahan makanan");
    }

    private void updateCounts() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Count ingredients
        Cursor ingredientCursor = db.rawQuery("SELECT COUNT(*) FROM ingredients", null);
        ingredientCursor.moveToFirst();
        int ingredientCount = ingredientCursor.getInt(0);
        ingredientCursor.close();

        // Count favorites
        Cursor favoriteCursor = db.rawQuery("SELECT COUNT(*) FROM favorites", null);
        favoriteCursor.moveToFirst();
        int favoriteCount = favoriteCursor.getInt(0);
        favoriteCursor.close();

        db.close();

        // Update UI
        tvIngredientCount.setText("Anda memiliki " + ingredientCount + " bahan makanan");
        tvFavoriteCount.setText(favoriteCount + " resep favorit");
    }


}