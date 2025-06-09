package com.example.foodrink.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
import com.example.foodrink.adapter.IngredientAdapter;
import com.example.foodrink.model.Ingredient;
import com.example.foodrink.util.GridSpacingItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PantryFragment extends Fragment {

    private static final String TAG = "PantryFragment";
    private RecyclerView recyclerView;
    private TextView tvEmptyMessage;
    private FloatingActionButton fabAddIngredient;
    private IngredientAdapter adapter;
    private List<Ingredient> ingredientList;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewIngredients);
        tvEmptyMessage = view.findViewById(R.id.tvEmptyMessage);
        fabAddIngredient = view.findViewById(R.id.fabAddIngredient);

        // Setup RecyclerView with GridLayoutManager (2 columns)
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);

        // Add spacing between grid items
        int spacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, true));

        dbHelper = new DBHelper(getContext());
        ingredientList = new ArrayList<>();

        // Initialize adapter with the fragment activity
        adapter = new IngredientAdapter(ingredientList, getActivity());
        recyclerView.setAdapter(adapter);

        // Load ingredients
        refreshIngredientList();

        // Setup FAB to add new ingredient
        fabAddIngredient.setOnClickListener(v -> {
            IngredientDetailFragment detailFragment = IngredientDetailFragment.newInstance(-1);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Refresh list when returning to this fragment
        refreshIngredientList();
    }

    private List<Ingredient> getAllIngredients() {
        List<Ingredient> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        try {
            Cursor cursor = db.rawQuery("SELECT * FROM ingredients ORDER BY name", null);
            Log.d(TAG, "Found " + cursor.getCount() + " ingredients in database");

            if (cursor.moveToFirst()) {
                do {
                    int idIndex = cursor.getColumnIndex("id");
                    int nameIndex = cursor.getColumnIndex("name");
                    int quantityIndex = cursor.getColumnIndex("quantity");
                    int unitIndex = cursor.getColumnIndex("unit");
                    int locationIndex = cursor.getColumnIndex("storage_location");
                    int categoryIndex = cursor.getColumnIndex("category");
                    int imageUrlIndex = cursor.getColumnIndex("image_url");

                    if (idIndex >= 0 && nameIndex >= 0 && quantityIndex >= 0 && unitIndex >= 0) {
                        int id = cursor.getInt(idIndex);
                        String name = cursor.getString(nameIndex);
                        int quantity = cursor.getInt(quantityIndex);
                        String unit = cursor.getString(unitIndex);

                        String location = locationIndex >= 0 ? cursor.getString(locationIndex) : "";
                        String category = categoryIndex >= 0 ? cursor.getString(categoryIndex) : "";
                        String imageUrl = imageUrlIndex >= 0 ? cursor.getString(imageUrlIndex) : null;

                        Ingredient ingredient = new Ingredient(id, name, quantity, unit, location, category, imageUrl);
                        list.add(ingredient);
                        Log.d(TAG, "Added ingredient: " + name);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            Log.e(TAG, "Error loading ingredients: " + e.getMessage());
        } finally {
            db.close();
        }

        return list;
    }

    private void refreshIngredientList() {
        ingredientList.clear();
        ingredientList.addAll(getAllIngredients());

        Log.d(TAG, "Refreshed ingredient list, size: " + ingredientList.size());

        if (ingredientList.isEmpty()) {
            tvEmptyMessage.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyMessage.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}