package com.example.mybookcatalog;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

public class BookDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        ImageView ivCover = findViewById(R.id.imageViewDetailCover);
        TextView tvGenre = findViewById(R.id.textViewDetailGenre);
        TextView tvTitle = findViewById(R.id.textViewDetailTitle);
        TextView tvAuthor = findViewById(R.id.textViewDetailAuthor);
        TextView tvPrice = findViewById(R.id.textViewDetailPrice);
        RatingBar ratingBar = findViewById(R.id.ratingBar);
        
        TextView tvYear = findViewById(R.id.tvDetailYear);
        TextView tvCategory = findViewById(R.id.tvDetailCategory);
        TextView tvPages = findViewById(R.id.tvDetailPages);
        TextView tvISBN = findViewById(R.id.tvDetailISBN);
        
        TextView tvDescription = findViewById(R.id.textViewDetailDescription);
        MaterialButton buttonRead = findViewById(R.id.buttonRead);
        MaterialButton buttonAddToCart = findViewById(R.id.buttonAddToCart);

        Book book = (Book) getIntent().getSerializableExtra("book");

        if (book != null) {
            if (book.getCoverImageRes() != 0) {
                ivCover.setImageResource(book.getCoverImageRes());
            }

            tvGenre.setText(book.getCategory());
            tvTitle.setText(book.getTitle());
            tvAuthor.setText(String.format("by %s", book.getAuthor()));
            tvPrice.setText(String.format(java.util.Locale.US, "KSh %.0f", book.getRetailPrice()));
            ratingBar.setRating((float) book.getRating());

            tvYear.setText(String.valueOf(book.getPublicationYear()));
            tvCategory.setText(book.getCategory());
            tvPages.setText(String.valueOf(book.getPages()));
            tvISBN.setText(book.getIsbn());

            tvDescription.setText(book.getDescription());

            buttonAddToCart.setOnClickListener(v -> {
                BookRepository.getInstance().addToCart(book);
                Toast.makeText(this, book.getTitle() + " added to cart!", Toast.LENGTH_SHORT).show();
            });

            buttonRead.setOnClickListener(v -> {
                Toast.makeText(this, "Opening preview for " + book.getTitle(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}
