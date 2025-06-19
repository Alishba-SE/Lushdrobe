package com.example.lushdrobe.activities;
import android.util.Log;  // Add this import statement
import android.content.Intent;
import android.content.SharedPreferences;
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

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordText, registerText;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dbHelper = new DatabaseHelper(this);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        registerText = findViewById(R.id.registerText);
        progressBar = findViewById(R.id.progressBar);

        loginButton.setOnClickListener(v -> loginUser());
        forgotPasswordText.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
        registerText.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));
    }

    private void loginUser() {
        String email = emailEditText.getText().toString().trim().toLowerCase();
        String password = passwordEditText.getText().toString().trim();

        // Add debug logs
        Log.d("LoginDebug", "Attempting login with email: " + email);
        Log.d("LoginDebug", "Password length: " + password.length());

        if (TextUtils.isEmpty(email)) {
            emailEditText.setError("Email is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            passwordEditText.setError("Password is required");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        loginButton.setEnabled(false);

        User user = dbHelper.getUser(email);

        if (user != null) {
            Log.d("LoginDebug", "Found user: " + user.getEmail());
            Log.d("LoginDebug", "Stored password: " + user.getPassword());
            Log.d("LoginDebug", "Input password: " + password);

            if (user.getPassword().equals(password)) {
                // Save user to SharedPreferences
                SharedPreferences.Editor editor = getSharedPreferences("UserPrefs", MODE_PRIVATE).edit();
                editor.putInt("userId", user.getId());
                editor.putString("userEmail", user.getEmail());
                editor.commit(); // Using commit() instead of apply() for immediate write

                Log.d("LoginDebug", "Login successful, starting MainActivity");
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Log.d("LoginDebug", "Password mismatch");
                Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
            }
        } else {
            Log.d("LoginDebug", "No user found with email: " + email);
            Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_LONG).show();
        }

        progressBar.setVisibility(View.GONE);
        loginButton.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}