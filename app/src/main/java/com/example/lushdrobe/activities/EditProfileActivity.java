package com.example.lushdrobe.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lushdrobe.R;
import com.example.lushdrobe.models.User;
import com.example.lushdrobe.utils.DatabaseHelper;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etAddress;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Initialize views
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnSave = findViewById(R.id.btnSave);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);

        // Get current user ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getInt("userId", -1);

        // Load current user data
        loadUserData();

        btnSave.setOnClickListener(v -> saveProfileChanges());
    }

    private void loadUserData() {
        if (userId != -1) {
            User user = dbHelper.getUserById(userId);
            if (user != null) {
                etName.setText(user.getName());
                etEmail.setText(user.getEmail());
                // Phone and address would need to be added to User model and database
            }
        }
    }

    private void saveProfileChanges() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etAddress.getText().toString().trim();

        if (validateInputs(name, email)) {
            User updatedUser = new User();
            updatedUser.setId(userId);
            updatedUser.setName(name);
            updatedUser.setEmail(email);
            // Set other fields as needed

            if (dbHelper.updateUser(updatedUser)) {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean validateInputs(String name, String email) {
        boolean isValid = true;

        if (name.isEmpty()) {
            etName.setError("Name is required");
            isValid = false;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Enter a valid email address");
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