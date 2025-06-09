package com.example.foodrink.ui;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodrink.ApiService;
import com.example.foodrink.DBHelper;
import com.example.foodrink.R;
import com.example.foodrink.RetrofitClient;
import com.example.foodrink.adapter.RecipeAdapter;
import com.example.foodrink.model.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeSearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeAdapter adapter;
    private List<RecipeResponse> recipeList;
    private ProgressBar progressBar;
    private DBHelper dbHelper;
    private static final String API_KEY = "e1456a682ef441f1a3de259a8c8ef616";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewRecipes);
        progressBar = view.findViewById(R.id.progressBar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DBHelper(getContext());
        recipeList = new ArrayList<>();
        adapter = new RecipeAdapter(recipeList);
        recyclerView.setAdapter(adapter);

        fetchRecipes();

        return view;
    }

    private void fetchRecipes() {
        progressBar.setVisibility(View.VISIBLE);

        String ingredients = getIngredientQuery();
        if (ingredients.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Tidak ada bahan makanan tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<List<RecipeResponse>> call = apiService.getRecipesByIngredients(ingredients, 20, API_KEY);

        call.enqueue(new Callback<List<RecipeResponse>>() {
            @Override
            public void onResponse(Call<List<RecipeResponse>> call, Response<List<RecipeResponse>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    recipeList.clear();
                    recipeList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Gagal memuat resep", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Kesalahan jaringan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getIngredientQuery() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM ingredients", null);
        List<String> names = new ArrayList<>();
        while (cursor.moveToNext()) {
            names.add(cursor.getString(0));
        }
        cursor.close();
        db.close();
        return TextUtils.join(",", names);
    }
}