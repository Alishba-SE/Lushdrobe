package com.example.lushdrobe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lushdrobe.R;
import com.example.lushdrobe.utils.DatabaseHelper;

public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_DELAY = 2000;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        dbHelper = new DatabaseHelper(this);

        new Handler().postDelayed(() -> {
            // Check if user is logged in (from SharedPreferences)
            int userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("userId", -1);
            if (userId != -1) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }

            finish();
        }, SPLASH_DELAY);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}