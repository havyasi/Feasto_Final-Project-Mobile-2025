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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrink.DBHelper;
import com.example.foodrink.R;
import com.example.foodrink.adapter.FavoriteAdapter;
import com.example.foodrink.model.FavoriteRecipe;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private TextView tvEmptyFavorites;
    private FavoriteAdapter adapter;
    private List<FavoriteRecipe> favoriteList;
    private DBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        tvEmptyFavorites = view.findViewById(R.id.tvEmptyFavorites);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DBHelper(getContext());
        favoriteList = new ArrayList<>();
        adapter = new FavoriteAdapter(favoriteList);
        recyclerView.setAdapter(adapter);

        loadFavorites();

        // Add swipe to delete functionality
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                adapter.deleteItem(position);

                // Show empty view if no items left
                if (adapter.getItemCount() == 0) {
                    tvEmptyFavorites.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Reload favorites when returning to this fragment
        loadFavorites();
    }

    private void loadFavorites() {
        favoriteList.clear();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM favorites ORDER BY saved_date DESC", null);

        if (cursor.moveToFirst()) {
            do {
                FavoriteRecipe recipe = new FavoriteRecipe(
                        cursor.getInt(0),      // id
                        cursor.getInt(1),      // recipe_id
                        cursor.getString(2),   // title
                        cursor.getString(3),   // image_url
                        cursor.getString(4),   // summary
                        cursor.getString(5),   // ingredients
                        cursor.getString(6),   // instructions
                        cursor.getString(7)    // saved_date
                );
                favoriteList.add(recipe);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        if (favoriteList.isEmpty()) {
            tvEmptyFavorites.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmptyFavorites.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }
}