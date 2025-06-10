package com.example.foodrink.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {
    private static final String THEME_PREFS = "theme_prefs";
    private static final String KEY_DARK_MODE = "dark_mode";

    /**
     * Apply the saved theme preference
     */
    public static void applyTheme(Context context) {
        boolean isDarkMode = isDarkMode(context);
        int mode = isDarkMode ?
                AppCompatDelegate.MODE_NIGHT_YES :
                AppCompatDelegate.MODE_NIGHT_NO;
        AppCompatDelegate.setDefaultNightMode(mode);
    }

    /**
     * Toggle between light and dark themes
     */
    public static void toggleTheme(Context context) {
        boolean currentMode = isDarkMode(context);
        setDarkMode(context, !currentMode);
        applyTheme(context);
    }

    /**
     * Check if dark mode is enabled
     */
    public static boolean isDarkMode(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }

    /**
     * Save the dark mode setting to preferences
     */
    private static void setDarkMode(Context context, boolean isDarkMode) {
        SharedPreferences prefs = context.getSharedPreferences(THEME_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(KEY_DARK_MODE, isDarkMode).apply();
    }
}