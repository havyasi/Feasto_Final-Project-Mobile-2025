package com.example.foodrink;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.foodrink.ui.FavoriteFragment;
import com.example.foodrink.ui.HomeFragment;
import com.example.foodrink.ui.PantryFragment;
import com.example.foodrink.ui.RecipeFragment;
import com.example.foodrink.util.ThemeManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Make status bar transparent and draw behind it
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // For light theme, make status bar icons dark
        if (!ThemeManager.isDarkMode(this)) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            // For dark theme, make status bar icons light and layout fullscreen
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        // Set the background based on current theme
        updateBackground();

        // Terapkan background secara eksplisit
        getWindow().setBackgroundDrawableResource(R.drawable.lavender);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                selectedFragment = new HomeFragment();
            } else if (itemId == R.id.nav_pantry) {
                selectedFragment = new PantryFragment();
            } else if (itemId == R.id.nav_recipe) {
                selectedFragment = new RecipeFragment();
            } else if (itemId == R.id.nav_favorite) {
                selectedFragment = new FavoriteFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }

            return true;
        });

        // Default fragment - now using HomeFragment
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new HomeFragment())
                .commit();

        // Set the Home menu item as selected by default
        bottomNav.setSelectedItemId(R.id.nav_home);
    }


    @Override
    protected void onResume() {
        super.onResume();
        // Update background when coming back to activity
        // (in case theme was changed elsewhere)
        updateBackground();
    }

    private void updateBackground() {
        ConstraintLayout mainLayout = findViewById(R.id.main);
        boolean isDarkMode = ThemeManager.isDarkMode(this);

        if (isDarkMode) {
            mainLayout.setBackgroundResource(R.drawable.dark_bg4);
        } else {
            mainLayout.setBackgroundResource(R.drawable.lavender);
        }
    }

}