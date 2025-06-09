package com.example.foodrink;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "smartpantry.db";
    private static final int DATABASE_VERSION = 7;

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
                "category TEXT, " +
                "image_url TEXT)");

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

        if (oldVersion <= 5) {
            db.execSQL("DROP TABLE IF EXISTS popular_recipes");
            db.execSQL("CREATE TABLE popular_recipes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "recipe_id INTEGER, " +
                    "title TEXT, " +
                    "image_url TEXT, " +
                    "summary TEXT)");
            db.execSQL("ALTER TABLE ingredients ADD COLUMN image_url TEXT");

            // Repopulate with all 10 recipes
            populatePopularRecipes(db);
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

        insertPopularRecipe(db, 648279, "Italian Tuna Pasta",
                "https://spoonacular.com/recipeImages/648279-556x370.jpg",
                "Nasi goreng dengan bumbu rempah khas Indonesia");

        insertPopularRecipe(db, 661340, "Spinach Salad with Strawberry Vinaigrette",
                "https://spoonacular.com/recipeImages/661340-556x370.jpg",
                "Pasta kerang isi bayam dan jamur dengan saus tomat");

        insertPopularRecipe(db, 632935, "Asparagus Lemon Risotto",
                "https://spoonacular.com/recipeImages/632935-556x370.jpg",
                "Sup asparagus dan kacang polong yang sehat dan cepat dibuat");

        insertPopularRecipe(db, 715594, "Homemade Garlic and Basil French Fries",
                "https://spoonacular.com/recipeImages/715594-556x370.jpg",
                "Coklat hitam buatan sendiri tanpa bahan pengawet");

        insertPopularRecipe(db, 782601, "Red Kidney Bean Jambalaya",
                "https://spoonacular.com/recipeImages/782601-556x370.jpg",
                "Jambalaya kacang merah dengan nasi dan rempah cajun");
    }

    private void insertPopularRecipe(SQLiteDatabase db, int recipeId, String title, String imageUrl, String summary) {
        ContentValues values = new ContentValues();
        values.put("recipe_id", recipeId);
        values.put("title", title);
        values.put("image_url", imageUrl);
        values.put("summary", summary);
        db.insert("popular_recipes", null, values);
    }

    public void refreshPopularRecipes() {
        SQLiteDatabase db = this.getWritableDatabase();
        populatePopularRecipes(db);
    }

}

