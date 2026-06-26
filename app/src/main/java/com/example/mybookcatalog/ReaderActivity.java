package com.example.mybookcatalog;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class ReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        Toolbar toolbar = findViewById(R.id.readerToolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Book Preview");
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        TextView textViewTitle = findViewById(R.id.textViewReaderTitle);
        TextView textViewAuthor = findViewById(R.id.textViewReaderAuthor);
        TextView textViewDetails = findViewById(R.id.textViewReaderDetails);
        TextView textViewContent = findViewById(R.id.textViewBookContent);

        Book book = (Book) getIntent().getSerializableExtra("book");
        if (book != null) {
            textViewTitle.setText(book.getTitle());
            textViewAuthor.setText(String.format(Locale.getDefault(), "by %s", book.getAuthor()));
            
            String details = String.format(Locale.getDefault(), "%s • %d Pages • %s",
                book.getCategory(), 
                book.getPages(), 
                book.getLanguage() != null ? book.getLanguage() : "English");
            textViewDetails.setText(details);

            textViewContent.setText(book.getContent());
        }
    }
}
