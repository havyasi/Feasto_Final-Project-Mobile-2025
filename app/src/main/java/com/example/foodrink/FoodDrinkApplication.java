package com.example.foodrink;

import android.app.Application;

import com.example.foodrink.util.ThemeManager;

public class FoodDrinkApplication extends Application {

    private int missedCount = 0;

    @Override
    public void onCreate() {
        super.onCreate();
        ThemeManager.applyTheme(this);
    }

    public void setMissedCount(int count) {
        this.missedCount = count;
    }

    public int getMissedCount() {
        return missedCount;
    }
}