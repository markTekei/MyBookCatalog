package com.example.mybookcatalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CustomersFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customers, container, false);

        // This fragment is now repurposed as the Profile Section
        TextView tvName = view.findViewById(R.id.tvProfileName);
        TextView tvEmail = view.findViewById(R.id.tvProfileEmail);

        // Setting some mock profile data
        tvName.setText("Alex Johnson");
        tvEmail.setText("alex.johnson@bookmail.com");

        // Profile Buttons and Menu Items
        view.findViewById(R.id.cvProfileImage).setOnClickListener(v -> 
            showToast("Change profile picture coming soon"));

        view.findViewById(R.id.llOrdersStat).setOnClickListener(v -> 
            showToast("Viewing your 12 orders"));
            
        view.findViewById(R.id.llWishlistStat).setOnClickListener(v -> 
            showToast("Viewing your wishlist"));
            
        view.findViewById(R.id.llPointsStat).setOnClickListener(v -> 
            showToast("Reward points: 450"));

        // Menu Rows
        view.findViewById(R.id.rowAccount).setOnClickListener(v -> 
            showToast("Opening Account Settings"));
            
        view.findViewById(R.id.rowNotifications).setOnClickListener(v -> 
            showToast("Opening Notification Preferences"));
            
        view.findViewById(R.id.rowHelp).setOnClickListener(v -> 
            showToast("Opening Help & Support"));

        view.findViewById(R.id.btnLogout).setOnClickListener(v -> 
            showToast("Logging out..."));

        return view;
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
