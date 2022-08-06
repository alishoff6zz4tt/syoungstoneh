package com.example.kidstodoapp;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import static android.content.Context.MODE_PRIVATE;

public class NightMode {
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void defaultMode(Context context) {
        sharedPreferences = context.getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        final boolean nightModeOn = sharedPreferences.getBoolean("isNightModeOn", false);
        if(nightModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static void updatePreference(Boolean bool) {
        if(bool) {
            editor.putBoolean("isNightModeOn", true);
        }
        else {
            editor.putBoolean("isNightModeOn", false);
        }
        editor.apply();
    }
}
