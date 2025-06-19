package com.example.lushdrobe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.lushdrobe.R;
import com.example.lushdrobe.models.CartItem;
import com.example.lushdrobe.models.Product;
import com.example.lushdrobe.utils.DatabaseHelper;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView productImage;
    private TextView productName, productPrice, productDescription, productCategory, productStock;
    private Button addToCartButton;
    private Product product;
    private DatabaseHelper dbHelper;
    private int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        dbHelper = new DatabaseHelper(this);
        userId = getSharedPreferences("UserPrefs", MODE_PRIVATE).getInt("userId", -1);

        productImage = findViewById(R.id.productImage);
        productName = findViewById(R.id.productName);
        productPrice = findViewById(R.id.productPrice);
        productDescription = findViewById(R.id.productDescription);
        productCategory = findViewById(R.id.productCategory);
        productStock = findViewById(R.id.productStock);
        addToCartButton = findViewById(R.id.addToCartButton);

        int productId = getIntent().getIntExtra("product_id", -1);
        if (productId != -1) {
            product = dbHelper.getProduct(productId);
            if (product != null) {
                displayProductDetails();
            }
        }

        addToCartButton.setOnClickListener(v -> addToCart());
    }

    private void displayProductDetails() {
        int imageRes = getResources().getIdentifier(
                product.getImageUrl(), "drawable", getPackageName());

        Glide.with(this)
                .load(imageRes)
                .placeholder(R.drawable.placeholder)
                .into(productImage);

        productName.setText(product.getName());
        productPrice.setText(String.format("$%.2f", product.getPrice()));
        productDescription.setText(product.getDescription());
        productCategory.setText(product.getCategory());
        productStock.setText(String.format("In Stock: %d", product.getStock()));
    }

    private void addToCart() {
        if (userId == -1) {
            Toast.makeText(this, "Please login to add items to cart", Toast.LENGTH_SHORT).show();
            return;
        }

        CartItem cartItem = new CartItem();
        cartItem.setUserId(userId);
        cartItem.setProductId(product.getId());
        cartItem.setQuantity(1);
        cartItem.setTotalPrice(product.getPrice());
        cartItem.setProductName(product.getName());
        cartItem.setProductImage(product.getImageUrl());
        cartItem.setProductPrice(product.getPrice());

        long id = dbHelper.addToCart(cartItem);
        if (id != -1) {
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}