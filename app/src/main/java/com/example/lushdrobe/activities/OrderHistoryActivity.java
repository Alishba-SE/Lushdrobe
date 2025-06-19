package com.example.lushdrobe.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lushdrobe.R;
import com.example.lushdrobe.adapters.OrderHistoryAdapter;
import com.example.lushdrobe.models.Order;
import com.example.lushdrobe.utils.DatabaseHelper;
import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderHistoryAdapter adapter;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        // Initialize database helper
        dbHelper = new DatabaseHelper(this);
        userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("userId", -1);

        // Setup RecyclerView
        recyclerView = findViewById(R.id.orderHistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load orders from database
        loadOrderHistory();
    }

    private void loadOrderHistory() {
        if (userId != -1) {
            List<Order> orders = dbHelper.getOrders(userId);
            adapter = new OrderHistoryAdapter(this, orders);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}