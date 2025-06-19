package com.example.lushdrobe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lushdrobe.R;
import com.example.lushdrobe.models.User;
import com.example.lushdrobe.utils.DatabaseHelper;

public class ProfileActivity extends AppCompatActivity {
    private TextView nameText, emailText;
    private Button editProfileButton, changePasswordButton, orderHistoryButton;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        dbHelper = new DatabaseHelper(this);
        userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("userId", -1);

        nameText = findViewById(R.id.nameText);
        emailText = findViewById(R.id.emailText);
        editProfileButton = findViewById(R.id.editProfileButton);
        changePasswordButton = findViewById(R.id.changePasswordButton);
        orderHistoryButton = findViewById(R.id.orderHistoryButton);

        loadUserData();

        editProfileButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, EditProfileActivity.class));
        });

        changePasswordButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
        });

        orderHistoryButton.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, OrderHistoryActivity.class));
        });
    }

    private void loadUserData() {
        if (userId != -1) {
            User user = dbHelper.getUser(String.valueOf(userId));
            if (user != null) {
                nameText.setText(user.getName());
                emailText.setText(user.getEmail());
            }
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}