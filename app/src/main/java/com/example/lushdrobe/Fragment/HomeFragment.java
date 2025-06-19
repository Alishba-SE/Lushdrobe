package com.example.lushdrobe.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lushdrobe.R;
import com.example.lushdrobe.activities.ProductListActivity;
import com.example.lushdrobe.adapters.FeaturedProductAdapter;
import com.example.lushdrobe.models.Product;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView featuredProductsRecyclerView;
    private Button browseProductsButton;
    private ImageView heroImageView;
    private TextView welcomeTextView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize views
        welcomeTextView = view.findViewById(R.id.welcomeTextView);
        heroImageView = view.findViewById(R.id.heroImageView);
        browseProductsButton = view.findViewById(R.id.browseProductsButton);
        featuredProductsRecyclerView = view.findViewById(R.id.featuredProductsRecyclerView);

        // Setup featured products RecyclerView
        setupFeaturedProducts();

        // Set click listener for browse products button
        browseProductsButton.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), ProductListActivity.class))
        );

        return view;
    }

    private void setupFeaturedProducts() {
        // Set layout manager for horizontal scrolling
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        featuredProductsRecyclerView.setLayoutManager(layoutManager);

        // Get featured products (mock data - replace with actual data from database)
        List<Product> featuredProducts = getFeaturedProducts();

        // Setup adapter
        FeaturedProductAdapter adapter = new FeaturedProductAdapter(getContext(), featuredProducts, product -> {
            // Handle product click
            // Intent to ProductDetailActivity
        });

        featuredProductsRecyclerView.setAdapter(adapter);
    }

    private List<Product> getFeaturedProducts() {
        // Mock data - replace with actual database call
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Matte Lipstick", "Long-lasting color", 12.99, "lipstick", "Lipstick", 50));
        products.add(new Product(2, "Eyeshadow Palette", "12 vibrant shades", 24.99, "eyeshadow", "Eyeshadow", 30));
        products.add(new Product(3, "Mascara", "Volume boost", 9.99, "mascara", "Mascara", 40));
        products.add(new Product(4, "Foundation", "Natural finish", 15.99, "foundation", "Foundation", 25));
        return products;
    }
}