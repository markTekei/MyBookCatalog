package com.example.mybookcatalog;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private String pendingCategory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        
        // Handle navigation between modules
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new DashboardFragment();
            } else if (itemId == R.id.nav_categories) {
                if (pendingCategory != null) {
                    selectedFragment = CatalogFragment.newInstance(pendingCategory);
                    pendingCategory = null; // Clear it after creating the fragment
                } else {
                    selectedFragment = new CatalogFragment();
                }
            } else if (itemId == R.id.nav_cart) {
                selectedFragment = new OrdersFragment();
            } else if (itemId == R.id.nav_profile) {
                selectedFragment = new CustomersFragment();
            }

            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, selectedFragment)
                        .commit();
            }
            return true;
        });

        // Set Home as the default landing screen
        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    /**
     * Navigates to the Catalog tab and applies a category filter.
     * Uses a pendingCategory variable to ensure the listener creates the correct fragment.
     */
    public void navigateToCatalog(String category) {
        this.pendingCategory = category;
        bottomNav.setSelectedItemId(R.id.nav_categories);
    }
}
