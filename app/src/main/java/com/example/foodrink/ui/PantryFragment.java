package com.example.foodrink.ui;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodrink.AddIngredientActivity;
import com.example.foodrink.DBHelper;
import com.example.foodrink.R;
import com.example.foodrink.adapter.IngredientAdapter;
import com.example.foodrink.model.Ingredient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PantryFragment extends Fragment {

    private RecyclerView recyclerView;
    private IngredientAdapter adapter;
    private List<Ingredient> ingredientList;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pantry, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewIngredients);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DBHelper(getContext());
        ingredientList = getAllIngredients();

        // Kirim activity saat membuat adapter
        adapter = new IngredientAdapter(ingredientList, requireActivity());
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fabAddIngredient);
        fab.setOnClickListener(v -> {
            // Opsi untuk menggunakan fragment alih-alih activity
            IngredientDetailFragment detailFragment = IngredientDetailFragment.newInstance(-1);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, detailFragment)
                    .addToBackStack(null)
                    .commit();

            // Atau tetap gunakan activity seperti sebelumnya:
            // Intent intent = new Intent(getContext(), AddIngredientActivity.class);
            // startActivity(intent);
        });

        return view;
    }

    private List<Ingredient> getAllIngredients() {
        List<Ingredient> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ingredients", null);

        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4)
                );
                list.add(ingredient);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}
