package com.example.mybookcatalog;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {

    private EditText etSearch;
    private ViewPager2 viewPagerFeatured;
    private TabLayout tabLayoutIndicator;
    private View hsvCategories;
    private RecyclerView rvCategoriesExpanded;
    private View hsvFeatured;
    private RecyclerView rvNewArrivals;
    private BookAdapter newArrivalsAdapter;
    private List<Book> allBooks;
    private List<Book> newArrivalsList;
    private List<Book> featuredBooks;
    
    private TextView tvSeeAllNew;
    private TextView tvSeeAllCategories;
    
    private ImageView ivProfileAvatar;
    private TextView tvGreeting;
    
    private OnBackPressedCallback onBackPressedCallback;
    
    // Slower, more modernized slideshow speed (15 seconds per book)
    private static final long SLIDE_DELAY = 15000; 
    
    private Handler slideHandler = new Handler(Looper.getMainLooper());
    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewPagerFeatured != null && featuredBooks != null && !featuredBooks.isEmpty()) {
                // Guaranteed one-by-one sequential slide
                int nextItem = viewPagerFeatured.getCurrentItem() + 1;
                viewPagerFeatured.setCurrentItem(nextItem, true);
            }
        }
    };

    private static final String PREFS_NAME = "ProfilePrefs";
    private static final String KEY_PROFILE_URI = "profile_uri";

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
        viewPagerFeatured = view.findViewById(R.id.viewPagerFeatured);
        tabLayoutIndicator = view.findViewById(R.id.tabLayoutIndicator);
        hsvCategories = view.findViewById(R.id.hsvCategories);
        rvCategoriesExpanded = view.findViewById(R.id.rvCategoriesExpanded);
        hsvFeatured = view.findViewById(R.id.hsvFeatured);
        rvNewArrivals = view.findViewById(R.id.rvNewArrivals);
        tvSeeAllNew = view.findViewById(R.id.tvSeeAllNew);
        tvSeeAllCategories = view.findViewById(R.id.tvSeeAllCategories);
        ivProfileAvatar = view.findViewById(R.id.ivProfileAvatar);
        tvGreeting = view.findViewById(R.id.tvGreeting);
        
        if (tvGreeting != null) {
            tvGreeting.setText(getString(R.string.greeting_format, getString(R.string.user_name)));
        }

        tvSeeAllNew.setVisibility(View.VISIBLE);

        BottomNavigationView bottomNav = getActivity() != null ? getActivity().findViewById(R.id.bottom_navigation) : null;

        view.findViewById(R.id.btnMenu).setOnClickListener(this::showHeaderMenu);
        view.findViewById(R.id.btnSearch).setOnClickListener(v -> {
            etSearch.requestFocus();
            if (getActivity() != null) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        
        if (ivProfileAvatar != null) {
            ivProfileAvatar.setOnClickListener(v -> {
                if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_profile);
            });
        }

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

        tvSeeAllCategories.setOnClickListener(v -> expandCategoriesSection());
        tvSeeAllNew.setOnClickListener(v -> expandNewArrivalsSection());

        setupCategoryCards(view);
        setupFeaturedSlideShow();
        setupNewArrivals();
        loadSavedProfileImage();

        return view;
    }

    private void setupCategoryCards(View view) {
        setupCategoryCard(view.findViewById(R.id.cardFiction), "Fiction");
        setupCategoryCard(view.findViewById(R.id.cardMystery), "Mystery");
        setupCategoryCard(view.findViewById(R.id.cardTech), "Technology");
        setupCategoryCard(view.findViewById(R.id.cardKids), "Kids");
        setupCategoryCard(view.findViewById(R.id.cardSelfHelp), "Self Help");
        setupCategoryCard(view.findViewById(R.id.cardBusiness), "Business");
    }

    private void setupCategoryCard(View cardView, String categoryName) {
        if (cardView == null) return;
        
        TextView tvName = cardView.findViewById(R.id.tvName);
        TextView tvCount = cardView.findViewById(R.id.tvCount);
        ImageView iv1 = cardView.findViewById(R.id.ivCover1);
        ImageView iv2 = cardView.findViewById(R.id.ivCover2);
        ImageView iv3 = cardView.findViewById(R.id.ivCover3);
        
        List<Book> categoryBooks = new ArrayList<>();
        for (Book b : allBooks) {
            if (b.getCategory().equalsIgnoreCase(categoryName)) {
                categoryBooks.add(b);
            }
        }
        
        int bgColor;
        int textColor;
        
        switch (categoryName.toLowerCase()) {
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
                textColor = R.color.cat_tech_icon;
                break;
            default:
                bgColor = R.color.slate_100;
                textColor = R.color.text_primary;
                break;
        }

        if (cardView instanceof MaterialCardView) {
            ((MaterialCardView) cardView).setCardBackgroundColor(ContextCompat.getColor(getContext(), bgColor));
        }
        tvName.setTextColor(ContextCompat.getColor(getContext(), textColor));
        tvCount.setTextColor(ContextCompat.getColor(getContext(), textColor));
        tvCount.setAlpha(0.7f);
        
        tvName.setText(categoryName);
        tvCount.setText(String.format(Locale.US, "%d Books", categoryBooks.size()));
        
        if (categoryBooks.size() > 0) iv1.setImageResource(categoryBooks.get(0).getCoverImageRes());
        if (categoryBooks.size() > 1) iv2.setImageResource(categoryBooks.get(1).getCoverImageRes());
        if (categoryBooks.size() > 2) iv3.setImageResource(categoryBooks.get(2).getCoverImageRes());
        
        cardView.setOnClickListener(v -> openCategoryInCatalog(categoryName));
    }

    private void setupFeaturedSlideShow() {
        // Add ALL books into the featured slideshow
        featuredBooks = new ArrayList<>(allBooks);
        if (featuredBooks.isEmpty()) return;

        FeaturedBookAdapter adapter = new FeaturedBookAdapter(featuredBooks, new FeaturedBookAdapter.OnFeaturedClickListener() {
            @Override
            public void onBookClick(Book book) {
                openBookDetail(book);
            }

            @Override
            public void onExploreClick(Book book) {
                openBookDetail(book);
            }
        });
        viewPagerFeatured.setAdapter(adapter);

        // Modern visual configuration
        viewPagerFeatured.setOffscreenPageLimit(3);
        viewPagerFeatured.setClipToPadding(false);
        viewPagerFeatured.setClipChildren(false);
        
        // Start in the middle of pseudo-infinite range to allow left/right swiping
        // Using a large number but not MAX_VALUE to be safer
        int initialPos = (1000 * featuredBooks.size()) / 2;
        viewPagerFeatured.setCurrentItem(initialPos, false);

        // Smooth and modern Page Transformer (Refined for fluidity)
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(30));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(0.95f + r * 0.05f); // Very subtle scale for a premium feel
            page.setAlpha(0.5f + r * 0.5f);     // Smooth fade out for side items
        });
        viewPagerFeatured.setPageTransformer(compositePageTransformer);

        // Recreate dots for the collection
        tabLayoutIndicator.removeAllTabs();
        for (int i = 0; i < featuredBooks.size(); i++) {
            tabLayoutIndicator.addTab(tabLayoutIndicator.newTab());
        }

        viewPagerFeatured.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                
                // Sync dots with real book index
                int realPos = position % featuredBooks.size();
                TabLayout.Tab tab = tabLayoutIndicator.getTabAt(realPos);
                if (tab != null) tab.select();

                // Modernizing slide flow: ensure no "jumping" by resetting timer
                slideHandler.removeCallbacks(sliderRunnable);
                slideHandler.postDelayed(sliderRunnable, SLIDE_DELAY);
            }
        });
    }

    private void loadSavedProfileImage() {
        if (getContext() != null && ivProfileAvatar != null) {
            SharedPreferences prefs = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String uriString = prefs.getString(KEY_PROFILE_URI, null);
            if (uriString != null) {
                try {
                    Uri uri = Uri.parse(uriString);
                    ivProfileAvatar.setImageURI(null);
                    ivProfileAvatar.setImageURI(uri);
                    ivProfileAvatar.setColorFilter(null);
                    ivProfileAvatar.setImageTintList(null);
                    ivProfileAvatar.setScaleType(ImageView.ScaleType.CENTER_CROP);
                } catch (Exception e) {
                    ivProfileAvatar.setImageResource(R.drawable.ic_customers);
                }
            }
        }
    }

    private void openCategoryInCatalog(String category) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).navigateToCatalog(category);
        }
    }

    private void expandCategoriesSection() {
        hsvCategories.setVisibility(View.GONE);
        rvCategoriesExpanded.setVisibility(View.VISIBLE);
        
        rvCategoriesExpanded.setLayoutManager(new GridLayoutManager(getContext(), 2));
        
        List<CategoryAdapter.CategoryInfo> categoryInfos = new ArrayList<>();
        String[] catNames = {"Fiction", "Mystery", "Technology", "Kids", "Self Help", "Business"};
        for (String name : catNames) {
            List<Integer> covers = new ArrayList<>();
            int count = 0;
            for (Book b : allBooks) {
                if (b.getCategory().equalsIgnoreCase(name)) {
                    if (covers.size() < 3) covers.add(b.getCoverImageRes());
                    count++;
                }
            }
            categoryInfos.add(new CategoryAdapter.CategoryInfo(name, count, covers));
        }

        CategoryAdapter adapter = new CategoryAdapter(categoryInfos, this::openCategoryInCatalog);
        rvCategoriesExpanded.setAdapter(adapter);
        rvCategoriesExpanded.setNestedScrollingEnabled(false);
        
        tvSeeAllCategories.setVisibility(View.GONE);
        onBackPressedCallback.setEnabled(true);
    }

    private void expandNewArrivalsSection() {
        newArrivalsList.clear();
        newArrivalsList.addAll(allBooks); 
        newArrivalsAdapter.notifyDataSetChanged();
        tvSeeAllNew.setVisibility(View.GONE);
        onBackPressedCallback.setEnabled(true);
    }

    private void collapseAllSections() {
        hsvCategories.setVisibility(View.VISIBLE);
        rvCategoriesExpanded.setVisibility(View.GONE);
        tvSeeAllCategories.setVisibility(View.VISIBLE);

        hsvFeatured.setVisibility(View.VISIBLE);
        
        newArrivalsList.clear();
        newArrivalsList.addAll(getFeaturedNewArrivals());
        newArrivalsAdapter.notifyDataSetChanged();
        tvSeeAllNew.setVisibility(View.VISIBLE);
        
        onBackPressedCallback.setEnabled(false);
    }

    private List<Book> getFeaturedNewArrivals() {
        List<Book> result = new ArrayList<>();
        List<String> outstandingTitles = Arrays.asList(
            "milk and honey", "The Alchemist",
            "Sherlock Holmes", "The Silent Patient",
            "The Pragmatic Programmer", "Clean Code",
            "Zero to One", "The Intelligent Investor",
            "The Cat in the Hat", "The Very Hungry Caterpillar",
            "Ego is the Enemy", "Atomic Habits"
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
            if ("My Profile".equals(item.getTitle())) {
                if (getActivity() != null) {
                    BottomNavigationView bottomNav = getActivity().findViewById(R.id.bottom_navigation);
                    if (bottomNav != null) bottomNav.setSelectedItemId(R.id.nav_profile);
                }
            } else if ("Help Section".equals(item.getTitle())) {
                showToast("Opening Help & Support…");
            }
            return true;
        });
        popup.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSavedProfileImage();
        slideHandler.removeCallbacks(sliderRunnable);
        slideHandler.postDelayed(sliderRunnable, SLIDE_DELAY);
    }

    @Override
    public void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    private void showToast(String message) {
        if (getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }
}
