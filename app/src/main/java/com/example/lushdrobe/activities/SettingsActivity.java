package com.example.lushdrobe.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import com.example.lushdrobe.R;
import com.example.lushdrobe.utils.DatabaseHelper;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class SettingsActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private SwitchMaterial notificationSwitch;
    private SwitchMaterial darkModeSwitch;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "AppSettings";
    private static final String THEME_PREF = "DarkMode";
    private static final String NOTIFICATION_PREF = "NotificationsEnabled";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Initialize switches
        notificationSwitch = findViewById(R.id.notificationSwitch);
        darkModeSwitch = findViewById(R.id.darkModeSwitch);

        // Load saved preferences
        loadPreferences();

        // Set up switch listeners
        setupNotificationSwitch();
        setupDarkModeSwitch();

        // Set up click listeners for other options
        findViewById(R.id.helpCenterLayout).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, HelpCenterActivity.class));
        });

        findViewById(R.id.aboutLayout).setOnClickListener(v -> {
            startActivity(new Intent(SettingsActivity.this, AboutActivity.class));
        });
    }

    private void loadPreferences() {
        // Load notification preference (default to true)
        boolean notificationsEnabled = sharedPreferences.getBoolean(NOTIFICATION_PREF, true);
        notificationSwitch.setChecked(notificationsEnabled);

        // Load theme preference (default to system/default)
        boolean isDarkMode = sharedPreferences.getBoolean(THEME_PREF,
                AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES);
        darkModeSwitch.setChecked(isDarkMode);
    }

    private void setupNotificationSwitch() {
        notificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(NOTIFICATION_PREF, isChecked);
            editor.apply();

            // Here you would implement actual notification toggle logic
            // For example:
            // NotificationHelper.toggleNotifications(isChecked);
        });
    }

    private void setupDarkModeSwitch() {
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(THEME_PREF, isChecked);
            editor.apply();

            // Apply theme immediately
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Recreate activity to apply theme changes
            recreate();
        });
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}