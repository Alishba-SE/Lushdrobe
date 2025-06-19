package com.example.lushdrobe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.lushdrobe.R;
import com.example.lushdrobe.models.Order;
import com.example.lushdrobe.utils.DatabaseHelper;

public class OrderConfirmationActivity extends AppCompatActivity {
    private TextView orderIdText, dateText, totalText, statusText;
    private Button continueShoppingButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        dbHelper = new DatabaseHelper(this);

        orderIdText = findViewById(R.id.orderIdText);
        dateText = findViewById(R.id.dateText);
        totalText = findViewById(R.id.totalText);
        statusText = findViewById(R.id.statusText);
        continueShoppingButton = findViewById(R.id.continueShoppingButton);

        int orderId = getIntent().getIntExtra("orderId", -1);
        if (orderId != -1) {
            loadOrderDetails(orderId);
        }

        continueShoppingButton.setOnClickListener(v -> {
            startActivity(new Intent(OrderConfirmationActivity.this, MainActivity.class));
            finish();
        });
    }

    private void loadOrderDetails(int orderId) {
        Order order = dbHelper.getOrder(orderId);
        if (order != null) {
            orderIdText.setText(String.format("Order ID: %d", order.getId()));
            dateText.setText(String.format("Date: %s", order.getDate()));
            totalText.setText(String.format("Total: $%.2f", order.getTotalPrice()));
            statusText.setText(String.format("Status: %s", order.getStatus()));
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}