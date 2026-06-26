package com.example.mybookcatalog;

import android.os.Bundle;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private String pendingCategory = null;
    private long backPressedTime;

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

        // Custom back button behavior
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (bottomNav.getSelectedItemId() != R.id.nav_home) {
                    // If not on Home tab, navigate back to Home
                    bottomNav.setSelectedItemId(R.id.nav_home);
                } else {
                    // If on Home tab, implement double-back-to-exit
                    if (backPressedTime + 2000 > System.currentTimeMillis()) {
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                    }
                    backPressedTime = System.currentTimeMillis();
                }
            }
        });
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
