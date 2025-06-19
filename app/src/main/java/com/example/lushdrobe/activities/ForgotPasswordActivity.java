package com.example.lushdrobe.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lushdrobe.R;
import com.example.lushdrobe.utils.DatabaseHelper;
import com.google.android.material.textfield.TextInputEditText;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextInputEditText emailEditText;
    private Button resetPasswordButton;
    private TextView backToLoginText;
    private DatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        dbHelper = new DatabaseHelper(this);

        emailEditText = findViewById(R.id.emailEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        backToLoginText = findViewById(R.id.backToLoginText);

        resetPasswordButton.setOnClickListener(v -> resetPassword());
        backToLoginText.setOnClickListener(v -> goBackToLogin());
    }

    private void resetPassword() {
        String email = emailEditText.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Please enter a valid email");
            return;
        }

        // Check if email exists in database
        if (dbHelper.doesEmailExist(email)) {
            // In a real app, you would send a password reset email here
            // For now, we'll just show a toast
            Toast.makeText(this, "Password reset link sent to " + email, Toast.LENGTH_LONG).show();

            // Optionally navigate back to login
            goBackToLogin();
        } else {
            Toast.makeText(this, "No account found with this email", Toast.LENGTH_LONG).show();
        }
    }

    private void goBackToLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}