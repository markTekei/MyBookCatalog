package com.example.mybookcatalog;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;

import java.util.List;
import java.util.Locale;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(String categoryName);
    }

    private final List<CategoryInfo> categories;
    private final OnCategoryClickListener listener;
    private int selectedPosition = 0; // "All" is at 0 by default

    public CategoryAdapter(List<CategoryInfo> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public void setSelectedCategory(String categoryName) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).name.equalsIgnoreCase(categoryName)) {
                int oldPos = selectedPosition;
                selectedPosition = i;
                notifyItemChanged(oldPos);
                notifyItemChanged(selectedPosition);
                break;
            }
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryInfo category = categories.get(position);
        Context context = holder.itemView.getContext();

        holder.tvName.setText(category.name);
        holder.tvCount.setText(String.format(Locale.US, "%d Books", category.bookCount));
        holder.ivIcon.setImageResource(category.iconRes);
        
        // Colors
        holder.layoutBg.setBackgroundColor(ContextCompat.getColor(context, category.bgColorRes));
        holder.ivIcon.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(context, category.iconColorRes)));

        // Selection style
        if (selectedPosition == position) {
            holder.rootCard.setStrokeColor(ContextCompat.getColor(context, R.color.colorPrimary));
            holder.rootCard.setStrokeWidth(4);
        } else {
            holder.rootCard.setStrokeColor(ContextCompat.getColor(context, android.R.color.transparent));
            holder.rootCard.setStrokeWidth(0);
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPos = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            notifyItemChanged(oldPos);
            notifyItemChanged(selectedPosition);
            listener.onCategoryClick(category.name);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView rootCard;
        LinearLayout layoutBg;
        ImageView ivIcon;
        TextView tvName;
        TextView tvCount;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            rootCard = itemView.findViewById(R.id.rootCard);
            layoutBg = itemView.findViewById(R.id.layoutBg);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvName = itemView.findViewById(R.id.tvName);
            tvCount = itemView.findViewById(R.id.tvCount);
        }
    }

    public static class CategoryInfo {
        String name;
        int bookCount;
        int iconRes;
        int bgColorRes;
        int iconColorRes;

        public CategoryInfo(String name, int bookCount, int iconRes, int bgColorRes, int iconColorRes) {
            this.name = name;
            this.bookCount = bookCount;
            this.iconRes = iconRes;
            this.bgColorRes = bgColorRes;
            this.iconColorRes = iconColorRes;
        }
    }
}
