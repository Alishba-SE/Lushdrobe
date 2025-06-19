package com.example.lushdrobe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lushdrobe.R;
import com.example.lushdrobe.models.User;
import com.example.lushdrobe.utils.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {
    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton;
    private TextView loginText;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        dbHelper = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginText = findViewById(R.id.loginText);
        progressBar = findViewById(R.id.progressBar);

        registerButton.setOnClickListener(v -> registerUser());
        loginText.setOnClickListener(v -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void registerUser() {
        final String name = nameEditText.getText().toString().trim();
        final String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            passwordEditText.setError("Password must be at least 6 characters");
            return;
        }

        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Passwords don't match");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        registerButton.setEnabled(false);

        // Check if email already exists
        if (dbHelper.getUser(email) != null) {
            progressBar.setVisibility(View.GONE);
            registerButton.setEnabled(true);
            Toast.makeText(this, "Email already registered", Toast.LENGTH_LONG).show();
            return;
        }

        // Create new user
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        // Add user to database
        long id = dbHelper.addUser(user);

        if (id != -1) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Registration failed", Toast.LENGTH_LONG).show();
        }

        progressBar.setVisibility(View.GONE);
        registerButton.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}