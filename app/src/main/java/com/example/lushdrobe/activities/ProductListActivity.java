package com.example.lushdrobe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.lushdrobe.R;
import com.example.lushdrobe.adapters.ProductAdapter;
import com.example.lushdrobe.models.Product;
import com.example.lushdrobe.utils.DatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class ProductListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ProgressBar progressBar;
    private DatabaseHelper dbHelper;
    private List<Product> allProducts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Handle search query if coming from MainActivity
        String searchQuery = getIntent().getStringExtra("search_query");
        if (searchQuery != null && !searchQuery.isEmpty()) {
            setTitle("Search: " + searchQuery);
        }

        loadProducts();
    }

    private void loadProducts() {
        progressBar.setVisibility(View.VISIBLE);

        // Get all products from database
        allProducts = dbHelper.getAllProducts();

        // Check if we have a search query
        String searchQuery = getIntent().getStringExtra("search_query");
        List<Product> displayProducts;

        if (searchQuery != null && !searchQuery.isEmpty()) {
            displayProducts = filterProducts(allProducts, searchQuery);
            if (displayProducts.isEmpty()) {
                Toast.makeText(this, "No products found", Toast.LENGTH_SHORT).show();
            }
        } else {
            displayProducts = new ArrayList<>(allProducts);
        }

        // Initialize adapter with explicit click listeners
        adapter = new ProductAdapter(this, displayProducts, new ProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Product product) {
                Intent intent = new Intent(ProductListActivity.this, ProductDetailActivity.class);
                intent.putExtra("product_id", product.getId());
                startActivity(intent);
            }

            @Override
            public void onAddToCartClick(Product product) {
                // Handle add to cart if needed
            }
        });

        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.GONE);
    }

    private List<Product> filterProducts(List<Product> products, String query) {
        List<Product> filteredList = new ArrayList<>();
        String lowerCaseQuery = query.toLowerCase().trim();

        for (Product product : products) {
            if (product.getName().toLowerCase().contains(lowerCaseQuery) ||
                    product.getDescription().toLowerCase().contains(lowerCaseQuery) ||
                    product.getCategory().toLowerCase().contains(lowerCaseQuery)) {
                filteredList.add(product);
            }
        }
        return filteredList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_list_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Search products...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!query.trim().isEmpty()) {
                    List<Product> filtered = filterProducts(allProducts, query);
                    adapter.updateList(filtered);

                    if (filtered.isEmpty()) {
                        Toast.makeText(ProductListActivity.this, "No products found", Toast.LENGTH_SHORT).show();
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() >= 2) {
                    List<Product> filtered = filterProducts(allProducts, newText);
                    adapter.updateList(filtered);
                } else if (newText.isEmpty()) {
                    adapter.updateList(allProducts);
                }
                return true;
            }
        });

        return true;
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }
}