package com.example.lushdrobe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lushdrobe.R;
import com.example.lushdrobe.models.Order;
import com.example.lushdrobe.utils.DatabaseHelper;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {
    private EditText nameEditText, addressEditText, cityEditText, zipEditText, phoneEditText;
    private RadioGroup paymentMethodGroup;
    private TextView totalPriceText;
    private Button placeOrderButton;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private double totalPrice;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        dbHelper = new DatabaseHelper(this);
        userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("userId", -1);

        nameEditText = findViewById(R.id.nameEditText);
        addressEditText = findViewById(R.id.addressEditText);
        cityEditText = findViewById(R.id.cityEditText);
        zipEditText = findViewById(R.id.zipEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        paymentMethodGroup = findViewById(R.id.paymentMethodGroup);
        totalPriceText = findViewById(R.id.totalPriceText);
        placeOrderButton = findViewById(R.id.placeOrderButton);
        progressBar = findViewById(R.id.progressBar);

        totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        totalPriceText.setText(String.format("Total: $%.2f", totalPrice));

        placeOrderButton.setOnClickListener(v -> placeOrder());
    }

    private void placeOrder() {
        String name = nameEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();
        String city = cityEditText.getText().toString().trim();
        String zip = zipEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        int selectedPaymentId = paymentMethodGroup.getCheckedRadioButtonId();
        String paymentMethod = "";

        if (selectedPaymentId == R.id.cashRadioButton) {
            paymentMethod = "Cash on Delivery";
        } else if (selectedPaymentId == R.id.cardRadioButton) {
            paymentMethod = "Credit Card";
        }

        if (TextUtils.isEmpty(name)) {
            nameEditText.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(address)) {
            addressEditText.setError("Address is required");
            return;
        }

        if (TextUtils.isEmpty(city)) {
            cityEditText.setError("City is required");
            return;
        }

        if (TextUtils.isEmpty(zip)) {
            zipEditText.setError("ZIP code is required");
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            phoneEditText.setError("Phone is required");
            return;
        }

        if (paymentMethod.isEmpty()) {
            Toast.makeText(this, "Please select payment method", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        placeOrderButton.setEnabled(false);

        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        Order order = new Order(-1, userId, name, address,
                city,
                zip,
                phone,
                paymentMethod,
                totalPrice,
                "Processing",
                date
        );

        long orderId = dbHelper.createOrder(order);

        if (orderId != -1) {
            // Clear cart after successful order
            dbHelper.clearCart(userId);

            Intent intent = new Intent(CheckoutActivity.this, OrderConfirmationActivity.class);
            intent.putExtra("orderId", (int) orderId);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
        }

        progressBar.setVisibility(View.GONE);
        placeOrderButton.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}