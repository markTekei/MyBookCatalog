package com.example.mybookcatalog;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ReaderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);

        TextView textViewContent = findViewById(R.id.textViewBookContent);

        Book book = (Book) getIntent().getSerializableExtra("book");
        if (book != null) {
            setTitle(book.getTitle());
            textViewContent.setText(book.getContent());
        }
    }
}
