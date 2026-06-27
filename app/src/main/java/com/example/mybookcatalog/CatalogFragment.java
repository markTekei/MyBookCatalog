package com.example.mybookcatalog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

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
    private EditText editTextSearch;
    
    private String currentSearchQuery = "";
    private String selectedCategory = "All";

    public static CatalogFragment newInstance(String category, String searchQuery) {
        CatalogFragment fragment = new CatalogFragment();
        Bundle args = new Bundle();
        args.putString("selected_category", category);
        args.putString("search_query", searchQuery);
        fragment.setArguments(args);
        return fragment;
    }

    public static CatalogFragment newInstance(String category) {
        return newInstance(category, "");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        // Adjust for system bars
        view.setPadding(0, getStatusBarHeight(), 0, 0);

        // Recover state from arguments if arriving from home
        if (getArguments() != null) {
            if (getArguments().containsKey("selected_category")) {
                selectedCategory = getArguments().getString("selected_category", "All");
            }
            if (getArguments().containsKey("search_query")) {
                currentSearchQuery = getArguments().getString("search_query", "");
            }
        }

        // Initialize UI components
        rvBooks = view.findViewById(R.id.recyclerView);
        rvBooks.setLayoutManager(new GridLayoutManager(getContext(), 2));

        rvCategories = view.findViewById(R.id.rvCategories);
        rvCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        textViewStats = view.findViewById(R.id.textViewStats);
        emptyState = view.findViewById(R.id.emptyState);
        editTextSearch = view.findViewById(R.id.editTextSearch);

        MaterialButton buttonAddBook = view.findViewById(R.id.buttonAddBook);
        buttonAddBook.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AddBookActivity.class);
            startActivity(intent);
        });

        // Initialize Data and Adapters
        loadInitialData();
        setupCategoryList();
        setupSearchLogic(view);

        // Perform initial filtering based on navigation arguments
        applyFilters();

        return view;
    }

    private void loadInitialData() {
        allBooks = BookRepository.getInstance().getAllBooks();
        filteredBooks = new ArrayList<>(allBooks);
        
        bookAdapter = new BookAdapter(filteredBooks, book -> {
            Intent intent = new Intent(getContext(), BookDetailActivity.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });
        rvBooks.setAdapter(bookAdapter);
    }

    private void setupCategoryList() {
        List<CategoryAdapter.CategoryInfo> categoryInfos = new ArrayList<>();
        
        String[] catNames = {"All", "Fiction", "Kids", "Self Help", "Business", "Mystery", "Technology"};
        for (String name : catNames) {
            categoryInfos.add(new CategoryAdapter.CategoryInfo(name, getCount(name), getCoversForCategory(name)));
        }

        categoryAdapter = new CategoryAdapter(categoryInfos, categoryName -> {
            selectedCategory = categoryName;
            applyFilters();
        });
        rvCategories.setAdapter(categoryAdapter);
        
        // Highlight the category passed from Home
        categoryAdapter.setSelectedCategory(selectedCategory);
    }

    private List<Integer> getCoversForCategory(String category) {
        List<Integer> covers = new ArrayList<>();
        if (allBooks == null) return covers;
        for (Book book : allBooks) {
            if (category.equalsIgnoreCase("All") || book.getCategory().equalsIgnoreCase(category)) {
                covers.add(book.getCoverImageRes());
                if (covers.size() >= 3) break;
            }
        }
        return covers;
    }

    private void setupSearchLogic(View root) {
        if (currentSearchQuery != null && !currentSearchQuery.isEmpty()) {
            editTextSearch.setText(currentSearchQuery);
        }

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchQuery = s.toString();
                applyFilters();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Search icon interaction
        View searchIcon = root.findViewById(R.id.buttonSearch);
        if (searchIcon != null) {
            searchIcon.setOnClickListener(v -> {
                editTextSearch.requestFocus();
                if (getActivity() != null) {
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) imm.showSoftInput(editTextSearch, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }
    }

    private int getCount(String category) {
        if (allBooks == null) return 0;
        if (category.equalsIgnoreCase("All")) return allBooks.size();
        int count = 0;
        for (Book book : allBooks) {
            if (book.getCategory().equalsIgnoreCase(category)) count++;
        }
        return count;
    }

    private void applyFilters() {
        if (allBooks == null || bookAdapter == null) return;

        filteredBooks.clear();
        String query = currentSearchQuery.toLowerCase(Locale.US).trim();

        for (Book book : allBooks) {
            boolean matchesSearch = query.isEmpty() || 
                                  book.getTitle().toLowerCase(Locale.US).contains(query) ||
                                  book.getAuthor().toLowerCase(Locale.US).contains(query);
            
            boolean matchesCategory = selectedCategory.equalsIgnoreCase("All") || 
                                     book.getCategory().equalsIgnoreCase(selectedCategory);

            if (matchesSearch && matchesCategory) {
                filteredBooks.add(book);
            }
        }

        bookAdapter.notifyDataSetChanged();
        updateStatsText();
        
        if (filteredBooks.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            rvBooks.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            rvBooks.setVisibility(View.VISIBLE);
        }
    }

    private void updateStatsText() {
        String stats = String.format(Locale.US, "%d books in %s", filteredBooks.size(), selectedCategory);
        textViewStats.setText(stats);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
