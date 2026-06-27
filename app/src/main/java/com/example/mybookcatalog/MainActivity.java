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
    private String pendingSearchQuery = null;
    private long backPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNav = findViewById(R.id.bottom_navigation);
        
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                selectedFragment = new DashboardFragment();
            } else if (itemId == R.id.nav_categories) {
                if (pendingCategory != null || pendingSearchQuery != null) {
                    selectedFragment = CatalogFragment.newInstance(
                            pendingCategory != null ? pendingCategory : "All",
                            pendingSearchQuery != null ? pendingSearchQuery : ""
                    );
                    pendingCategory = null;
                    pendingSearchQuery = null;
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

        if (savedInstanceState == null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (bottomNav.getSelectedItemId() != R.id.nav_home) {
                    bottomNav.setSelectedItemId(R.id.nav_home);
                } else {
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

    public void navigateToCatalog(String category) {
        navigateToCatalog(category, null);
    }

    public void navigateToCatalog(String category, String searchQuery) {
        this.pendingCategory = category;
        this.pendingSearchQuery = searchQuery;
        bottomNav.setSelectedItemId(R.id.nav_categories);
    }
}
