package com.example.foodrink;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.foodrink.model.RecipeDetail;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeDetailActivity extends AppCompatActivity {

    private TextView tvTitle, tvIngredients, tvInstructions;
    private ImageView ivImage;
    private Button btnSaveFavorite;
    private ProgressBar progressBar;
    private DBHelper dbHelper;
    private int recipeId;
    private RecipeDetail recipeDetail;
    private static final String API_KEY = "e1456a682ef441f1a3de259a8c8ef616";
    private FloatingActionButton fabFavorite;
    private boolean isInFavorites = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        tvTitle = findViewById(R.id.tvRecipeTitle);
        ivImage = findViewById(R.id.ivRecipeImage);
        tvIngredients = findViewById(R.id.tvIngredients);
        tvInstructions = findViewById(R.id.tvInstructions);
        progressBar = findViewById(R.id.progressBar);
        fabFavorite = findViewById(R.id.fabFavorite);

        dbHelper = new DBHelper(this);

        recipeId = getIntent().getIntExtra("RECIPE_ID", 0);

        loadRecipeDetails();

        fabFavorite.setOnClickListener(v -> toggleFavorite());
    }

    private void loadRecipeDetails() {
        progressBar.setVisibility(View.VISIBLE);
        ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);
        Call<RecipeDetail> call = apiService.getRecipeDetails(recipeId, API_KEY);
        call.enqueue(new Callback<RecipeDetail>() {
            @Override
            public void onResponse(Call<RecipeDetail> call, Response<RecipeDetail> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    recipeDetail = response.body();
                    displayRecipeDetails();
                    checkFavoriteStatus();
                } else {
                    Toast.makeText(RecipeDetailActivity.this, "Gagal memuat detail resep", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeDetail> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RecipeDetailActivity.this, "Kesalahan jaringan" , Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkFavoriteStatus() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT id FROM favorites WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeDetail.getId())});

        isInFavorites = cursor.getCount() > 0;
        updateFavoriteIcon();

        cursor.close();
        db.close();
    }

    private void updateFavoriteIcon() {
        if (isInFavorites) {
            fabFavorite.setImageResource(R.drawable.ic_favorite); // Filled heart icon
        } else {
            fabFavorite.setImageResource(R.drawable.ic_favorite_border); // Outline heart icon
        }
    }

    private void toggleFavorite() {
        if (recipeDetail == null) {
            Toast.makeText(this, "Data resep tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isInFavorites) {
            // Remove from favorites
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            int deleted = db.delete("favorites", "recipe_id = ?",
                    new String[]{String.valueOf(recipeDetail.getId())});
            db.close();

            if (deleted > 0) {
                isInFavorites = false;
                updateFavoriteIcon();
                Toast.makeText(this, "Dihapus dari favorit", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add to favorites
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("recipe_id", recipeDetail.getId());
            values.put("title", recipeDetail.getTitle());
            values.put("image_url", recipeDetail.getImage());
            values.put("summary", recipeDetail.getSummary());

            StringBuilder ingredientsBuilder = new StringBuilder();
            recipeDetail.getExtendedIngredients().forEach(ingredient ->
                    ingredientsBuilder.append(ingredient.getOriginal()).append(";")
            );
            values.put("ingredients", ingredientsBuilder.toString());
            values.put("instructions", recipeDetail.getInstructions());
            values.put("saved_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

            long insertId = db.insert("favorites", null, values);
            db.close();

            if (insertId != -1) {
                isInFavorites = true;
                updateFavoriteIcon();
                Toast.makeText(this, "Berhasil disimpan ke favorit", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gagal menyimpan ke favorit", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void displayRecipeDetails() {
        tvTitle.setText(recipeDetail.getTitle());

        // Load image with Glide
        Glide.with(this)
                .load(recipeDetail.getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .into(ivImage);

        // Process ingredients
        StringBuilder ingredientsBuilder = new StringBuilder();
        recipeDetail.getExtendedIngredients().forEach(ingredient ->
                ingredientsBuilder.append("• ").append(ingredient.getOriginal()).append("\n")
        );
        tvIngredients.setText(ingredientsBuilder.toString());

        // Process instructions (removing HTML tags)
        if (recipeDetail.getInstructions() != null) {
            tvInstructions.setText(Html.fromHtml(recipeDetail.getInstructions(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            tvInstructions.setText("Instruksi tidak tersedia.");
        }
    }

    private void saveToFavorites() {
        if (recipeDetail == null) {
            Toast.makeText(this, "Data resep tidak tersedia", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if recipe already exists in favorites
        SQLiteDatabase checkDb = dbHelper.getReadableDatabase();
        Cursor cursor = checkDb.rawQuery(
                "SELECT * FROM favorites WHERE recipe_id = ?",
                new String[]{String.valueOf(recipeDetail.getId())});

        if (cursor.getCount() > 0) {
            Toast.makeText(this, "Resep sudah ada di favorit", Toast.LENGTH_SHORT).show();
            cursor.close();
            checkDb.close();
            return;
        }
        cursor.close();
        checkDb.close();

        // Recipe doesn't exist in favorites, so add it
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeDetail.getId());
        values.put("title", recipeDetail.getTitle());
        values.put("image_url", recipeDetail.getImage());
        values.put("summary", recipeDetail.getSummary());

        // Prepare ingredients list as string
        StringBuilder ingredientsBuilder = new StringBuilder();
        recipeDetail.getExtendedIngredients().forEach(ingredient ->
                ingredientsBuilder.append(ingredient.getOriginal()).append(";")
        );
        values.put("ingredients", ingredientsBuilder.toString());
        values.put("instructions", recipeDetail.getInstructions());
        values.put("saved_date", new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        long insertId = db.insert("favorites", null, values);
        db.close();

        if (insertId != -1) {
            Toast.makeText(this, "Berhasil disimpan ke favorit", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Gagal menyimpan ke favorit", Toast.LENGTH_SHORT).show();
        }
    }
}