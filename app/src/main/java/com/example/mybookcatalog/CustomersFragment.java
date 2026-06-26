package com.example.mybookcatalog;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CustomersFragment extends Fragment {

    private ImageView ivProfilePicture;
    private ActivityResultLauncher<String> galleryLauncher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize the gallery launcher to pick images
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null && ivProfilePicture != null) {
                        ivProfilePicture.setImageURI(uri);
                        ivProfilePicture.setColorFilter(null); // Remove the tint if there was any
                        showToast("Profile picture updated");
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customers, container, false);

        // This fragment is now repurposed as the Profile Section
        TextView tvName = view.findViewById(R.id.tvProfileName);
        TextView tvEmail = view.findViewById(R.id.tvProfileEmail);
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);

        // Setting your profile data
        tvName.setText("MARK TEKEI");
        tvEmail.setText("marktekei903@gmail.com");

        // allow adding a profile picture by accessing photos
        view.findViewById(R.id.cvProfileImage).setOnClickListener(v -> 
            galleryLauncher.launch("image/*"));

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

        // Functional Log Out button
        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            showToast("Logging out...");
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) {
                getActivity().finish();
            }
        });

        return view;
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
