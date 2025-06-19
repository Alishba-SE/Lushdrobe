package com.example.lushdrobe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lushdrobe.R;
import com.example.lushdrobe.adapters.CartAdapter;
import com.example.lushdrobe.models.CartItem;
import com.example.lushdrobe.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private List<CartItem> cartItems;
    private ProgressBar progressBar;
    private TextView totalPriceText, emptyCartText;
    private Button checkoutButton;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        dbHelper = new DatabaseHelper(this);
        userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("userId", -1);

        initializeViews();
        setupRecyclerView();
        setupCheckoutButton();
        loadCartItems();
    }

    private void initializeViews() {
        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        totalPriceText = findViewById(R.id.totalPriceText);
        emptyCartText = findViewById(R.id.emptyCartText);
        checkoutButton = findViewById(R.id.checkoutButton);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        cartItems = new ArrayList<>();
        adapter = new CartAdapter(this, cartItems, this::updateTotalPrice,
                this::removeCartItem, this::updateCartItemQuantity);
        recyclerView.setAdapter(adapter);
    }

    private void setupCheckoutButton() {
        checkoutButton.setOnClickListener(v -> {
            if (!cartItems.isEmpty()) {
                proceedToCheckout();
            } else {
                showEmptyCartMessage();
            }
        });
    }

    private void proceedToCheckout() {
        double total = calculateTotal();
        Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
        intent.putExtra("totalPrice", total);
        startActivity(intent);
    }

    private void showEmptyCartMessage() {
        Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
    }

    private void loadCartItems() {
        progressBar.setVisibility(View.VISIBLE);

        if (userId != -1) {
            cartItems = dbHelper.getCartItems(userId);
            adapter.updateCartItems(cartItems);
            updateUI();
        }

        progressBar.setVisibility(View.GONE);
    }

    private void updateUI() {
        double total = calculateTotal();
        updateTotalPrice(total);

        if (cartItems.isEmpty()) {
            emptyCartText.setVisibility(View.VISIBLE);
            checkoutButton.setEnabled(false);
        } else {
            emptyCartText.setVisibility(View.GONE);
            checkoutButton.setEnabled(true);
        }
    }

    private double calculateTotal() {
        double total = 0.0;
        for (CartItem item : cartItems) {
            total += item.getTotalPrice();
        }
        return total;
    }

    public void updateTotalPrice(double total) {
        totalPriceText.setText(String.format(Locale.getDefault(), "Total: $%.2f", total));
    }

    public void removeCartItem(int cartItemId) {
        dbHelper.removeFromCart(cartItemId);
        loadCartItems(); // Refresh the cart
    }

    public void updateCartItemQuantity(int cartItemId, int quantity, double price) {
        dbHelper.updateCartItemQuantity(cartItemId, quantity, price);
        loadCartItems(); // Refresh the cart
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}