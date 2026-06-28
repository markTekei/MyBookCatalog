package com.example.mybookcatalog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class CustomersFragment extends Fragment {

    private ImageView ivProfilePicture;
    private ActivityResultLauncher<String[]> galleryLauncher;
    
    private static final String PREFS_NAME = "ProfilePrefs";
    private static final String KEY_PROFILE_PATH = "profile_image_path";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                uri -> {
                    if (uri != null) {
                        saveProfileImage(uri);
                        loadSavedProfileImage();
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

        tvName.setText(R.string.user_name);
        tvEmail.setText(R.string.user_email);

        loadSavedProfileImage();

        ivProfilePicture.setOnClickListener(v -> 
            galleryLauncher.launch(new String[]{"image/*"}));

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
        Context context = getContext();
        if (context == null) return;
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            if (is != null) is.close();

            if (bitmap != null) {
                // Resize to prevent memory issues and ensure visibility
                int maxSize = 800;
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                if (width > maxSize || height > maxSize) {
                    float ratio = (float) width / (float) height;
                    if (ratio > 1) {
                        width = maxSize;
                        height = (int) (maxSize / ratio);
                    } else {
                        height = maxSize;
                        width = (int) (maxSize * ratio);
                    }
                    bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                }

                File file = new File(context.getFilesDir(), "profile_photo.jpg");
                try (FileOutputStream os = new FileOutputStream(file)) {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
                }
                
                SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
                prefs.edit().putString(KEY_PROFILE_PATH, file.getAbsolutePath()).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadSavedProfileImage() {
        if (getContext() != null && ivProfilePicture != null) {
            SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String path = prefs.getString(KEY_PROFILE_PATH, null);
            
            // RESET: Completely clear tints and filters to fix "black photo" bug
            ivProfilePicture.setImageTintList(null);
            ivProfilePicture.setColorFilter(null);
            ivProfilePicture.setBackgroundColor(Color.WHITE); 
            ivProfilePicture.setPadding(0, 0, 0, 0);
            
            if (path != null) {
                File file = new File(path);
                if (file.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    if (bitmap != null) {
                        ivProfilePicture.setImageBitmap(bitmap);
                        ivProfilePicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        return;
                    }
                }
            }
            
            // Fallback placeholder
            ivProfilePicture.setImageResource(R.drawable.ic_customers);
            ivProfilePicture.setPadding(32, 32, 32, 32);
            ivProfilePicture.setImageTintList(android.content.res.ColorStateList.valueOf(
                    ContextCompat.getColor(requireContext(), R.color.colorPrimary)));
        }
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
