package com.example.lushdrobe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lushdrobe.R;
import com.example.lushdrobe.utils.DatabaseHelper;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText currentPassword, newPassword, confirmPassword;
    private Button btnChangePassword;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // Initialize views
        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        dbHelper = new DatabaseHelper(this);

        // Get current user email from SharedPreferences
        String userEmail = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                .getString("user_email", null);

        btnChangePassword.setOnClickListener(v -> {
            String currentPass = currentPassword.getText().toString().trim();
            String newPass = newPassword.getText().toString().trim();
            String confirmPass = confirmPassword.getText().toString().trim();

            if (validateInputs(currentPass, newPass, confirmPass)) {
                if (dbHelper.checkUserPassword(userEmail, currentPass)) {
                    if (newPass.equals(confirmPass)) {
                        if (dbHelper.updateUserPassword(userEmail, newPass)) {
                            Toast.makeText(this, "Password changed successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(this, "Password update failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        confirmPassword.setError("Passwords don't match");
                    }
                } else {
                    currentPassword.setError("Incorrect current password");
                }
            }
        });
    }

    private boolean validateInputs(String currentPass, String newPass, String confirmPass) {
        boolean isValid = true;

        if (currentPass.isEmpty()) {
            currentPassword.setError("Current password required");
            isValid = false;
        }

        if (newPass.isEmpty()) {
            newPassword.setError("New password required");
            isValid = false;
        } else if (newPass.length() < 6) {
            newPassword.setError("Password must be at least 6 characters");
            isValid = false;
        }

        if (confirmPass.isEmpty()) {
            confirmPassword.setError("Please confirm password");
            isValid = false;
        }

        return isValid;
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}