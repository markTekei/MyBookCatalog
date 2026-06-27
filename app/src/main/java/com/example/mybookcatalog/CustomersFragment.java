package com.example.mybookcatalog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private ActivityResultLauncher<String[]> galleryLauncher;
    private static final String PREFS_NAME = "ProfilePrefs";
    private static final String KEY_PROFILE_URI = "profile_uri";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Use OpenDocument to get a URI that we can take persistable permissions on
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        saveProfileImage(uri);
                        displayProfileImage(uri);
                        showToast(getString(R.string.profile_updated));
                    }
                }
        );
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customers, container, false);

        TextView tvName = view.findViewById(R.id.tvProfileName);
        TextView tvEmail = view.findViewById(R.id.tvProfileEmail);
        ivProfilePicture = view.findViewById(R.id.ivProfilePicture);

        // Setting your profile data from strings.xml
        tvName.setText(R.string.user_name);
        tvEmail.setText(R.string.user_email);

        loadSavedProfileImage();

        view.findViewById(R.id.cvProfileImage).setOnClickListener(v -> 
            galleryLauncher.launch(new String[]{"image/*"}));

        // Rest of click listeners...
        view.findViewById(R.id.btnLogout).setOnClickListener(v -> {
            showToast(getString(R.string.logout_message));
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) getActivity().finish();
        });

        return view;
    }

    private void saveProfileImage(Uri uri) {
        if (getContext() != null) {
            try {
                // Grant persistable permission so it works across app restarts
                getContext().getContentResolver().takePersistableUriPermission(uri, 
                        Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (SecurityException ignored) {}
            
            SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            prefs.edit().putString(KEY_PROFILE_URI, uri.toString()).apply();
        }
    }

    private void loadSavedProfileImage() {
        if (getContext() != null) {
            SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String uriString = prefs.getString(KEY_PROFILE_URI, null);
            if (uriString != null) {
                displayProfileImage(Uri.parse(uriString));
            }
        }
    }

    private void displayProfileImage(Uri uri) {
        if (ivProfilePicture != null) {
            ivProfilePicture.setImageURI(uri);
            ivProfilePicture.setImageTintList(null); // Remove default icon tint
            ivProfilePicture.setColorFilter(null);
            ivProfilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
