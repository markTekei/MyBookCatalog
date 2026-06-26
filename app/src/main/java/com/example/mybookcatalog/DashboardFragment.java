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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private EditText etSearch;
    private LinearLayout featuredBooksContainer;
    private View hsvCategories;
    private RecyclerView rvCategoriesExpanded;
    private View hsvFeatured;
    private RecyclerView rvFeaturedExpanded;
    private RecyclerView rvNewArrivals;
    private BookAdapter newArrivalsAdapter;
    private List<Book> allBooks;
    private List<Book> newArrivalsList;
    
    private TextView tvSeeAllFeatured;
    private TextView tvSeeAllNew;
    private TextView tvSeeAllCategories;
    
    private OnBackPressedCallback onBackPressedCallback;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        onBackPressedCallback = new OnBackPressedCallback(false) {
            @Override
            public void handleOnBackPressed() {
                collapseAllSections();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        allBooks = BookRepository.getInstance().getAllBooks();
        etSearch = view.findViewById(R.id.etSearch);
        featuredBooksContainer = view.findViewById(R.id.featuredBooksContainer);
        hsvCategories = view.findViewById(R.id.hsvCategories);
        rvCategoriesExpanded = view.findViewById(R.id.rvCategoriesExpanded);
        hsvFeatured = view.findViewById(R.id.hsvFeatured);
        rvFeaturedExpanded = view.findViewById(R.id.rvFeaturedExpanded);
        rvNewArrivals = view.findViewById(R.id.rvNewArrivals);
        tvSeeAllFeatured = view.findViewById(R.id.tvSeeAllFeatured);
        tvSeeAllNew = view.findViewById(R.id.tvSeeAllNew);
        tvSeeAllCategories = view.findViewById(R.id.tvSeeAllCategories);
        
        // Re-enable "See all" for New Arrivals but it will show all books only when clicked
        tvSeeAllNew.setVisibility(View.VISIBLE);

        BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);

        view.findViewById(R.id.btnMenu).setOnClickListener(this::showHeaderMenu);
        view.findViewById(R.id.btnSearch).setOnClickListener(v -> {
            etSearch.requestFocus();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
        });
        
        view.findViewById(R.id.ivProfileAvatar).setOnClickListener(v -> {
            if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_profile);
        });

        // Setup Category Clicks to navigate to Catalog tab
        view.findViewById(R.id.catFiction).setOnClickListener(v -> openCategoryInCatalog("Fiction"));
        view.findViewById(R.id.catKids).setOnClickListener(v -> openCategoryInCatalog("Kids"));
        view.findViewById(R.id.catSelfHelp).setOnClickListener(v -> openCategoryInCatalog("Self Help"));
        view.findViewById(R.id.catBusiness).setOnClickListener(v -> openCategoryInCatalog("Business"));

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterBooks(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        tvSeeAllCategories.setOnClickListener(v -> {
            expandCategoriesSection();
        });

        tvSeeAllFeatured.setOnClickListener(v -> {
            expandFeaturedSection();
        });

        tvSeeAllNew.setOnClickListener(v -> {
            expandNewArrivalsSection();
        });

        setupFeaturedBooks();
        setupNewArrivals();
        updateCategoryCounts(view);

        return view;
    }

    private void openCategoryInCatalog(String category) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToCatalog(category);
        }
    }

    private void updateCategoryCounts(View view) {
        ((TextView) view.findViewById(R.id.tvFictionCount)).setText(String.format(Locale.US, "%d Books", getCount("Fiction")));
        ((TextView) view.findViewById(R.id.tvKidsCount)).setText(String.format(Locale.US, "%d Books", getCount("Kids")));
        ((TextView) view.findViewById(R.id.tvSelfHelpCount)).setText(String.format(Locale.US, "%d Books", getCount("Self Help")));
        ((TextView) view.findViewById(R.id.tvBusinessCount)).setText(String.format(Locale.US, "%d Books", getCount("Business")));
    }

    private void expandCategoriesSection() {
        hsvCategories.setVisibility(View.GONE);
        rvCategoriesExpanded.setVisibility(View.VISIBLE);
        
        rvCategoriesExpanded.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        List<CategoryAdapter.CategoryInfo> categoryInfos = new ArrayList<>();
        categoryInfos.add(new CategoryAdapter.CategoryInfo("Fiction", getCount("Fiction"), R.drawable.ic_book, R.color.cat_fiction_bg, R.color.cat_fiction_icon));
        categoryInfos.add(new CategoryAdapter.CategoryInfo("Kids", getCount("Kids"), R.drawable.ic_book, R.color.cat_kids_bg, R.color.cat_kids_icon));
        categoryInfos.add(new CategoryAdapter.CategoryInfo("Self Help", getCount("Self Help"), R.drawable.ic_customers, R.color.cat_selfhelp_bg, R.color.cat_selfhelp_icon));
        categoryInfos.add(new CategoryAdapter.CategoryInfo("Business", getCount("Business"), R.drawable.ic_inventory, R.color.cat_business_bg, R.color.cat_business_icon));
        categoryInfos.add(new CategoryAdapter.CategoryInfo("Mystery", getCount("Mystery"), R.drawable.ic_book, R.color.cat_mystery_bg, R.color.cat_mystery_icon));
        categoryInfos.add(new CategoryAdapter.CategoryInfo("Technology", getCount("Technology"), R.drawable.ic_inventory, R.color.cat_tech_bg, R.color.cat_tech_icon));

        CategoryAdapter adapter = new CategoryAdapter(categoryInfos, categoryName -> {
            openCategoryInCatalog(categoryName);
        });
        rvCategoriesExpanded.setAdapter(adapter);
        rvCategoriesExpanded.setNestedScrollingEnabled(false);
        
        tvSeeAllCategories.setVisibility(View.GONE);
        onBackPressedCallback.setEnabled(true);
    }

    private int getCount(String category) {
        int count = 0;
        for (Book book : allBooks) {
            if (book.getCategory().equals(category)) count++;
        }
        return count;
    }

    private void expandFeaturedSection() {
        hsvFeatured.setVisibility(View.GONE);
        rvFeaturedExpanded.setVisibility(View.VISIBLE);
        
        rvFeaturedExpanded.setLayoutManager(new GridLayoutManager(getContext(), 2));
        BookAdapter expandedAdapter = new BookAdapter(allBooks, this::openBookDetail);
        rvFeaturedExpanded.setAdapter(expandedAdapter);
        rvFeaturedExpanded.setNestedScrollingEnabled(false);
        
        tvSeeAllFeatured.setVisibility(View.GONE);
        onBackPressedCallback.setEnabled(true);
    }

    private void expandNewArrivalsSection() {
        newArrivalsList.clear();
        newArrivalsList.addAll(allBooks); // Show all products when clicked
        newArrivalsAdapter.notifyDataSetChanged();
        tvSeeAllNew.setVisibility(View.GONE);
        onBackPressedCallback.setEnabled(true);
    }

    private void collapseAllSections() {
        hsvCategories.setVisibility(View.VISIBLE);
        rvCategoriesExpanded.setVisibility(View.GONE);
        tvSeeAllCategories.setVisibility(View.VISIBLE);

        hsvFeatured.setVisibility(View.VISIBLE);
        rvFeaturedExpanded.setVisibility(View.GONE);
        tvSeeAllFeatured.setVisibility(View.VISIBLE);
        
        newArrivalsList.clear();
        newArrivalsList.addAll(getFeaturedNewArrivals());
        newArrivalsAdapter.notifyDataSetChanged();
        tvSeeAllNew.setVisibility(View.VISIBLE);
        
        onBackPressedCallback.setEnabled(false);
    }

    private List<Book> getFeaturedNewArrivals() {
        List<Book> result = new ArrayList<>();
        // Select exactly two books per category with outstanding covers for "New Arrivals"
        List<String> outstandingTitles = Arrays.asList(
            "milk and honey", "The Alchemist",         // Fiction
            "The Adventures of Sherlock Holmes", "The Silent Patient", // Mystery
            "The Pragmatic Programmer", "Clean Code",         // Technology
            "Zero to One", "The Intelligent Investor",        // Business
            "The Cat in the Hat", "The Very Hungry Caterpillar", // Kids
            "Ego is the Enemy", "Atomic Habits"        // Self Help
        );

        for (String title : outstandingTitles) {
            for (Book book : allBooks) {
                if (book.getTitle().equalsIgnoreCase(title)) {
                    result.add(book);
                    break;
                }
            }
        }
        return result;
    }

    private void filterBooks(String query) {
        List<Book> filtered = new ArrayList<>();
        // Only search/filter within the "New Arrivals" subset as per request
        List<Book> sourceList = getFeaturedNewArrivals();
        for (Book book : sourceList) {
            if (book.getTitle().toLowerCase().contains(query.toLowerCase()) || 
                book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(book);
            }
        }
        newArrivalsList.clear();
        newArrivalsList.addAll(filtered);
        newArrivalsAdapter.notifyDataSetChanged();
        
        if (!query.isEmpty()) {
            onBackPressedCallback.setEnabled(true);
        } else {
            collapseAllSections();
        }
    }

    private void setupFeaturedBooks() {
        featuredBooksContainer.removeAllViews();
        
        // Default only one image shown: "goodbye kiss"
        Book tempBook = null;
        for (Book book : allBooks) {
            if (book.getTitle().equalsIgnoreCase("goodbye kiss")) {
                tempBook = book;
                break;
            }
        }
        
        if (tempBook == null && !allBooks.isEmpty()) {
            tempBook = allBooks.get(0);
        }

        final Book goodbyeKiss = tempBook;

        if (goodbyeKiss != null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View bookView = inflater.inflate(R.layout.item_book, featuredBooksContainer, false);
            ViewGroup.LayoutParams lp = bookView.getLayoutParams();
            lp.width = (int) (160 * getResources().getDisplayMetrics().density);
            bookView.setLayoutParams(lp);

            ((TextView) bookView.findViewById(R.id.textViewTitle)).setText(goodbyeKiss.getTitle());
            ((TextView) bookView.findViewById(R.id.textViewAuthor)).setText(goodbyeKiss.getAuthor());
            ((TextView) bookView.findViewById(R.id.textViewCategoryTag)).setText(goodbyeKiss.getCategory());
            
            ImageView ivCover = bookView.findViewById(R.id.imageViewCover);
            if (goodbyeKiss.getCoverImageRes() != 0) {
                ivCover.setImageResource(goodbyeKiss.getCoverImageRes());
            }

            bookView.setOnClickListener(v -> openBookDetail(goodbyeKiss));
            featuredBooksContainer.addView(bookView);
        }
    }

    private void setupNewArrivals() {
        rvNewArrivals.setLayoutManager(new LinearLayoutManager(getContext()));
        newArrivalsList = new ArrayList<>();
        newArrivalsList.addAll(getFeaturedNewArrivals());
        newArrivalsAdapter = new BookAdapter(newArrivalsList, this::openBookDetail);
        rvNewArrivals.setAdapter(newArrivalsAdapter);
        rvNewArrivals.setNestedScrollingEnabled(false);
    }

    private void openBookDetail(Book book) {
        Intent intent = new Intent(getContext(), BookDetailActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }

    private void showHeaderMenu(View v) {
        PopupMenu popup = new PopupMenu(getContext(), v);
        popup.getMenu().add("Help Section");
        popup.getMenu().add("My Profile");
        popup.getMenu().add("Settings");
        popup.setOnMenuItemClickListener(item -> {
            if (item.getTitle().equals("My Profile")) {
                BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
                if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_profile);
            } else if (item.getTitle().equals("Help Section")) {
                showToast("Opening Help & Support...");
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
