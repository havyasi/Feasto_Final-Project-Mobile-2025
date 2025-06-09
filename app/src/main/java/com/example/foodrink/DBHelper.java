package com.example.foodrink;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartpantry.db";
    private static final int DATABASE_VERSION = 5;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabel ingredients dengan kolom kategori
        db.execSQL("CREATE TABLE ingredients (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, " +
                "quantity INTEGER, " +
                "unit TEXT, " +
                "storage_location TEXT, " +
                "category TEXT)");

        // Tabel lainnya yang sudah ada
        db.execSQL("CREATE TABLE favorites (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER, title TEXT, image_url TEXT, summary TEXT, " +
                "ingredients TEXT, instructions TEXT, saved_date TEXT)");

        // Tambahkan tabel baru untuk resep populer
        db.execSQL("CREATE TABLE popular_recipes (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "recipe_id INTEGER, " +
                "title TEXT, " +
                "image_url TEXT, " +
                "summary TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Existing upgrade logic...

        // Tambahkan tabel popular_recipes jika belum ada
        if (oldVersion < 5) {
            try {
                db.execSQL("CREATE TABLE IF NOT EXISTS popular_recipes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "recipe_id INTEGER, " +
                        "title TEXT, " +
                        "image_url TEXT, " +
                        "summary TEXT)");
            } catch (Exception e) {
                // Table might already exist
            }
        }
    }

    // Tambahkan metode untuk mengisi data dummy
    public void populatePopularRecipes(SQLiteDatabase db) {
        // Hapus data lama jika ada
        db.delete("popular_recipes", null, null);

        // Data dummy resep populer
        insertPopularRecipe(db, 716429, "Pasta with Garlic, Scallions, Cauliflower & Breadcrumbs",
                "https://spoonacular.com/recipeImages/716429-556x370.jpg",
                "Resep pasta populer dengan bawang putih dan kembang kol");

        insertPopularRecipe(db, 715538, "Grilled Salmon with Avocado Greek Salad",
                "https://spoonacular.com/recipeImages/715538-556x370.jpg",
                "Salmon panggang dengan salad Yunani dan alpukat");

        insertPopularRecipe(db, 716276, "Chocolate Peanut Butter Banana Smoothie",
                "https://spoonacular.com/recipeImages/716276-556x370.jpg",
                "Smoothie coklat dengan selai kacang dan pisang");

        insertPopularRecipe(db, 795751, "Chicken Fajita Stuffed Bell Pepper",
                "https://spoonacular.com/recipeImages/795751-556x370.jpg",
                "Paprika isi ayam fajita yang lezat dan bergizi");

        insertPopularRecipe(db, 643857, "Falafel Burgers",
                "https://spoonacular.com/recipeImages/643857-556x370.jpg",
                "Burger vegetarian dengan falafel yang lezat");
    }

    private void insertPopularRecipe(SQLiteDatabase db, int recipeId, String title, String imageUrl, String summary) {
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("title", title);
        values.put("image_url", imageUrl);
        values.put("summary", summary);
        db.insert("popular_recipes", null, values);
    }

}

