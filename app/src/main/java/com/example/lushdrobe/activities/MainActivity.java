package com.example.lushdrobe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.example.lushdrobe.R;
import com.example.lushdrobe.Fragment.HomeFragment;
import com.example.lushdrobe.Fragment.ProfileFragment;
import com.example.lushdrobe.utils.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar toolbar;
    private DatabaseHelper dbHelper;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);

        initializeViews();
        setupNavigationDrawer();
        loadDefaultFragment();
    }

    private void initializeViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupNavigationDrawer() {
        toolbar.setNavigationOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            Fragment fragment = null;

            if (id == R.id.nav_home) {
                fragment = new HomeFragment();
            } else if (id == R.id.nav_products) {
                startActivity(new Intent(this, ProductListActivity.class));
            } else if (id == R.id.nav_profile) {
                fragment = new ProfileFragment();
            } else if (id == R.id.nav_cart) {
                startActivity(new Intent(this, CartActivity.class));
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            } else if (id == R.id.nav_logout) {
                handleLogout();
            } else if (id == R.id.nav_orders) {
                startActivity(new Intent(this, OrderHistoryActivity.class));
            }

            if (fragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, fragment)
                        .commit();
            }

            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void handleLogout() {
        getSharedPreferences("UserPrefs", MODE_PRIVATE).edit().clear().apply();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    private void loadDefaultFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new HomeFragment())
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        // Setup search functionality with null checks
        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
            if (searchView != null) {
                configureSearchView();
            }
        }

        return true;
    }

    private void configureSearchView() {
        try {
            searchView.setQueryHint(getString(R.string.search_hint)); // Use string resource
            searchView.setIconifiedByDefault(false);
            searchView.setMaxWidth(Integer.MAX_VALUE); // Make search view expand full width

            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    performSearch(query);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // Optional: Implement real-time search as you type
                    if (newText.length() >= 3) { // Only search after 3 characters
                        performSearch(newText);
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void performSearch(String query) {
        if (query == null || query.trim().isEmpty()) {
            return;
        }

        try {
            Intent searchIntent = new Intent(this, ProductListActivity.class);
            searchIntent.putExtra("search_query", query.trim());
            startActivity(searchIntent);

            // Clear search view after submission if it exists
            if (searchView != null) {
                searchView.setQuery("", false);
                searchView.clearFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_cart) {
            startActivity(new Intent(this, CartActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}