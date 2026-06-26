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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrdersFragment extends Fragment {

    private List<Book> cartItems;
    private CartAdapter adapter;
    private TextView tvCount;
    private TextView tvTotalPrice;
    private View emptyState;
    private RecyclerView rvCart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders, container, false);

        rvCart = view.findViewById(R.id.rvCartItems);
        rvCart.setLayoutManager(new LinearLayoutManager(getContext()));

        tvCount = view.findViewById(R.id.tvCartItemsCount);
        tvTotalPrice = view.findViewById(R.id.tvTotalPrice);
        
        // Load real cart items
        cartItems = BookRepository.getInstance().getCartItems();
        
        adapter = new CartAdapter(cartItems, position -> {
            Book book = cartItems.get(position);
            BookRepository.getInstance().removeFromCart(book);
            updateUI();
        });
        rvCart.setAdapter(adapter);

        view.findViewById(R.id.btnCheckout).setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                showToast("Your cart is empty!");
            } else {
                showToast("Order placed successfully!");
                BookRepository.getInstance().clearCart();
                updateUI();
            }
        });

        updateUI();

        return view;
    }

    private void updateUI() {
        if (cartItems.isEmpty()) {
            tvCount.setText("Your cart is empty");
            tvTotalPrice.setText("KSh 0");
            rvCart.setVisibility(View.GONE);
            // Optionally show an empty state view here if one existed in fragment_orders.xml
        } else {
            rvCart.setVisibility(View.VISIBLE);
            tvCount.setText(String.format(java.util.Locale.US, "%d items in your bag", cartItems.size()));
            
            double total = 0;
            for (Book item : cartItems) {
                total += item.getRetailPrice();
            }
            tvTotalPrice.setText(String.format(java.util.Locale.US, "KSh %,.0f", total));
        }
        adapter.notifyDataSetChanged();
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private static class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
        private final List<Book> items;
        private final OnItemRemoveListener removeListener;

        interface OnItemRemoveListener {
            void onRemove(int position);
        }

        CartAdapter(List<Book> items, OnItemRemoveListener removeListener) {
            this.items = items;
            this.removeListener = removeListener;
        }

        @NonNull
        @Override
        public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
            return new CartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
            Book book = items.get(position);
            holder.tvTitle.setText(book.getTitle());
            holder.tvAuthor.setText(book.getAuthor());
            holder.tvPrice.setText(String.format(java.util.Locale.US, "KSh %.0f", book.getRetailPrice()));
            
            holder.btnRemove.setOnClickListener(v -> {
                if (removeListener != null) {
                    removeListener.onRemove(holder.getAdapterPosition());
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        static class CartViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle, tvAuthor, tvPrice;
            View btnRemove;

            CartViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(R.id.tvBookTitle);
                tvAuthor = itemView.findViewById(R.id.tvBookAuthor);
                tvPrice = itemView.findViewById(R.id.tvBookPrice);
                btnRemove = itemView.findViewById(R.id.btnRemoveContainer);
            }
        }
    }
}
