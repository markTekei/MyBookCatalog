package com.example.mybookcatalog;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CatalogFragment extends Fragment {

    private List<Book> allBooks;
    private List<Book> filteredBooks;
    private BookAdapter bookAdapter;
    private CategoryAdapter categoryAdapter;
    private RecyclerView rvBooks;
    private RecyclerView rvCategories;
    private TextView textViewStats;
    private View emptyState;
    
    private String currentSearchQuery = "";
    private String selectedCategory = "All";

    public static CatalogFragment newInstance(String category) {
        CatalogFragment fragment = new CatalogFragment();
        Bundle args = new Bundle();
        args.putString("selected_category", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        if (getArguments() != null) {
            selectedCategory = getArguments().getString("selected_category", "All");
        }

        // Adjust for system bars
        view.setPadding(0, getStatusBarHeight(), 0, 0);

        rvBooks = view.findViewById(R.id.recyclerView);
        rvBooks.setLayoutManager(new GridLayoutManager(getContext(), 2));

        rvCategories = view.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        textViewStats = view.findViewById(R.id.textViewStats);
        emptyState = view.findViewById(R.id.emptyState);

        MaterialButton buttonAddBook = view.findViewById(R.id.buttonAddBook);
        buttonAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddBookActivity.class);
            startActivity(intent);
        });

        // Make search icon clickable
        view.findViewById(R.id.buttonSearch).setOnClickListener(v -> {
            Toast.makeText(getContext(), "Search activated. Please type in the search bar (coming soon)", Toast.LENGTH_SHORT).show();
        });

        loadData();
        setupCategories();
        
        if (!selectedCategory.equals("All")) {
            applyFilters();
        }

        return view;
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private void loadData() {
        allBooks = BookRepository.getInstance().getAllBooks();
        filteredBooks = new ArrayList<>(allBooks);
        
        bookAdapter = new BookAdapter(filteredBooks, book -> {
            Intent intent = new Intent(getContext(), BookDetailActivity.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        rvBooks.setAdapter(bookAdapter);
        updateStats();
    }

    private void setupCategories() {
        List<CategoryAdapter.CategoryInfo> categoryInfos = new ArrayList<>();
        
        // Add "All"
        categoryInfos.add(new CategoryAdapter.CategoryInfo("All", allBooks.size(), 
                R.drawable.ic_book, R.color.slate_100, R.color.text_secondary));
        
        // Categories matching the home page
        categoryInfos.add(new CategoryAdapter.CategoryInfo("Fiction", getCount("Fiction"), 
                R.drawable.ic_book, R.color.cat_fiction_bg, R.color.cat_fiction_icon));
        
        categoryInfos.add(new CategoryAdapter.CategoryInfo("Self Help", getCount("Self Help"), 
                R.drawable.ic_customers, R.color.cat_selfhelp_bg, R.color.cat_selfhelp_icon));
        
        categoryInfos.add(new CategoryAdapter.CategoryInfo("Business", getCount("Business"), 
                R.drawable.ic_inventory, R.color.cat_business_bg, R.color.cat_business_icon));

        categoryInfos.add(new CategoryAdapter.CategoryInfo("Kids", getCount("Kids"), 
                R.drawable.ic_book, R.color.cat_kids_bg, R.color.cat_kids_icon));

        categoryInfos.add(new CategoryAdapter.CategoryInfo("Mystery", getCount("Mystery"), 
                R.drawable.ic_book, R.color.cat_mystery_bg, R.color.cat_mystery_icon));

        categoryInfos.add(new CategoryAdapter.CategoryInfo("Technology", getCount("Technology"), 
                R.drawable.ic_inventory, R.color.cat_tech_bg, R.color.cat_tech_icon));

        categoryAdapter = new CategoryAdapter(categoryInfos, categoryName -> {
            selectedCategory = categoryName;
            applyFilters();
        });
        rvCategories.setAdapter(categoryAdapter);
        
        // Ensure the correct category is highlighted
        categoryAdapter.setSelectedCategory(selectedCategory);
    }

    private int getCount(String category) {
        int count = 0;
        for (Book book : allBooks) {
            if (book.getCategory().equals(category)) count++;
        }
        return count;
    }

    private void applyFilters() {
        filteredBooks.clear();
        String query = currentSearchQuery.toLowerCase(Locale.US).trim();

        for (Book book : allBooks) {
            boolean matchesSearch = book.getTitle().toLowerCase(Locale.US).contains(query) ||
                                  book.getAuthor().toLowerCase(Locale.US).contains(query);
            
            boolean matchesCategory = selectedCategory.equals("All") || 
                                     book.getCategory().equals(selectedCategory);

            if (matchesSearch && matchesCategory) {
                filteredBooks.add(book);
            }
        }

        bookAdapter.notifyDataSetChanged();
        updateStats();
        
        if (filteredBooks.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvBooks.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvBooks.setVisibility(View.VISIBLE);
        }
    }

    private void updateStats() {
        String stats = String.format(Locale.US, "%d books", filteredBooks.size());
        textViewStats.setText(stats);
    }
}
