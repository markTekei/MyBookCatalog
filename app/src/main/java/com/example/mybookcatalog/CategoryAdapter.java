package com.example.mybookcatalog;

import android.content.Context;
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
    private int selectedPosition = -1; 

    public CategoryAdapter(List<CategoryInfo> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
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
        
        // Dynamic colors based on category
        int bgColor;
        int textColor;
        
        switch (category.name.toLowerCase()) {
            case "fiction":
                bgColor = R.color.cat_fiction_bg;
                textColor = R.color.cat_fiction_text;
                break;
            case "kids":
                bgColor = R.color.cat_kids_bg;
                textColor = R.color.cat_kids_text;
                break;
            case "self help":
                bgColor = R.color.cat_selfhelp_bg;
                textColor = R.color.cat_selfhelp_text;
                break;
            case "business":
                bgColor = R.color.cat_business_bg;
                textColor = R.color.cat_business_text;
                break;
            case "mystery":
                bgColor = R.color.cat_mystery_bg;
                textColor = R.color.cat_mystery_text;
                break;
            case "technology":
                bgColor = R.color.cat_tech_bg;
                textColor = R.color.cat_tech_text;
                break;
            default:
                bgColor = R.color.slate_100;
                textColor = R.color.text_primary;
                break;
        }
        
        holder.rootCard.setCardBackgroundColor(ContextCompat.getColor(context, bgColor));
        holder.tvName.setTextColor(ContextCompat.getColor(context, textColor));

        // Display book covers
        if (category.covers != null) {
            if (category.covers.size() > 0) {
                holder.ivCover1.setImageResource(category.covers.get(0));
                holder.ivCover1.setVisibility(View.VISIBLE);
            } else {
                holder.ivCover1.setVisibility(View.GONE);
            }
            
            if (category.covers.size() > 1) {
                holder.ivCover2.setImageResource(category.covers.get(1));
                holder.ivCover2.setVisibility(View.VISIBLE);
            } else {
                holder.ivCover2.setVisibility(View.GONE);
            }
            
            if (category.covers.size() > 2) {
                holder.ivCover3.setImageResource(category.covers.get(2));
                holder.ivCover3.setVisibility(View.VISIBLE);
            } else {
                holder.ivCover3.setVisibility(View.GONE);
            }
        }

        // Selection style
        if (selectedPosition == position) {
            holder.rootCard.setStrokeWidth(4);
            holder.rootCard.setStrokeColor(ContextCompat.getColor(context, R.color.colorPrimary));
        } else {
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

    public void setSelectedCategory(String categoryName) {
        for (int i = 0; i < categories.size(); i++) {
            if (categories.get(i).name.equalsIgnoreCase(categoryName)) {
                int oldPosition = selectedPosition;
                selectedPosition = i;
                notifyItemChanged(oldPosition);
                notifyItemChanged(selectedPosition);
                break;
            }
        }
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView rootCard;
        ImageView ivCover1, ivCover2, ivCover3;
        TextView tvName;
        TextView tvCount;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            rootCard = itemView.findViewById(R.id.rootCard);
            ivCover1 = itemView.findViewById(R.id.ivCover1);
            ivCover2 = itemView.findViewById(R.id.ivCover2);
            ivCover3 = itemView.findViewById(R.id.ivCover3);
            tvName = itemView.findViewById(R.id.tvName);
            tvCount = itemView.findViewById(R.id.tvCount);
        }
    }

    public static class CategoryInfo {
        String name;
        int bookCount;
        List<Integer> covers;

        public CategoryInfo(String name, int bookCount, List<Integer> covers) {
            this.name = name;
            this.bookCount = bookCount;
            this.covers = covers;
        }
    }
}
