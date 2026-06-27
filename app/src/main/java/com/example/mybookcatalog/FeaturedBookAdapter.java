package com.example.mybookcatalog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FeaturedBookAdapter extends RecyclerView.Adapter<FeaturedBookAdapter.FeaturedBookViewHolder> {

    public interface OnFeaturedClickListener {
        void onBookClick(Book book);
        void onExploreClick(Book book);
    }

    private List<Book> bookList;
    private OnFeaturedClickListener listener;

    public FeaturedBookAdapter(List<Book> bookList, OnFeaturedClickListener listener) {
        this.bookList = bookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FeaturedBookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_featured_book, parent, false);
        return new FeaturedBookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedBookViewHolder holder, int position) {
        if (bookList.isEmpty()) return;
        
        // Use modulo for infinite looping
        Book book = bookList.get(position % bookList.size());
        
        holder.textViewTitle.setText(book.getTitle());
        holder.textViewAuthor.setText(book.getAuthor());
        holder.textViewCategoryTag.setText(book.getCategory());
        
        if (book.getCoverImageRes() != 0) {
            holder.imageViewCover.setImageResource(book.getCoverImageRes());
        } else {
            holder.imageViewCover.setImageResource(R.drawable.ic_launcher_background);
        }
        
        holder.itemView.setOnClickListener(v -> listener.onBookClick(book));
        holder.btnExplore.setOnClickListener(v -> listener.onExploreClick(book));
    }

    @Override
    public int getItemCount() {
        // Return a large number to enable pseudo-infinite scrolling
        // 1000 cycles through the book list is plenty for a seamless experience
        return bookList.isEmpty() ? 0 : bookList.size() * 1000;
    }

    static class FeaturedBookViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textViewAuthor;
        TextView textViewCategoryTag;
        ImageView imageViewCover;
        MaterialButton btnExplore;

        public FeaturedBookViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewCategoryTag = itemView.findViewById(R.id.textViewCategoryTag);
            imageViewCover = itemView.findViewById(R.id.imageViewCover);
            btnExplore = itemView.findViewById(R.id.btnExplore);
        }
    }
}
