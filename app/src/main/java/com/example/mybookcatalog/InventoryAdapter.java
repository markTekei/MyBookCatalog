package com.example.mybookcatalog;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class InventoryAdapter extends RecyclerView.Adapter<InventoryAdapter.InventoryViewHolder> {

    private List<Book> inventoryList;

    public InventoryAdapter(List<Book> inventoryList) {
        this.inventoryList = inventoryList;
    }

    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_inventory, parent, false);
        return new InventoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        Book book = inventoryList.get(position);
        holder.textViewTitle.setText(book.getTitle());
        holder.textViewAuthor.setText(book.getAuthor());
        holder.textViewISBN.setText(String.format(Locale.US, "ISBN: %s", book.getIsbn()));
        holder.textViewStock.setText(String.valueOf(book.getStockQuantity()));

        // Simple color coding for low stock alert
        if (book.getStockQuantity() < 10) {
            holder.textViewStock.setTextColor(0xFFFF0000); // Red
        } else {
            holder.textViewStock.setTextColor(0xFF2E7D32); // Green
        }
    }

    @Override
    public int getItemCount() {
        return inventoryList.size();
    }

    static class InventoryViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle, textViewAuthor, textViewISBN, textViewStock;

        public InventoryViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewInventoryTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewInventoryAuthor);
            textViewISBN = itemView.findViewById(R.id.textViewInventoryISBN);
            textViewStock = itemView.findViewById(R.id.textViewInventoryStock);
        }
    }
}
