package com.example.mybookcatalog;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AddBookActivity extends AppCompatActivity {

    private TextInputEditText editTextTitle, editTextAuthor, editTextYear, editTextPages,
            editTextRating, editTextISBN, editTextRetailPrice, editTextStock, editTextDescription;
    private AutoCompleteTextView autoCompleteGenre;

    private static final String[] GENRES = {
            "Fiction", "Non-Fiction", "Science Fiction", "Fantasy", "Mystery",
            "Romance", "Biography", "History", "Self-Help", "Technology",
            "Poetry", "Drama", "Adventure", "Horror", "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        // Initialize views
        editTextTitle = findViewById(R.id.editTextTitle);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextYear = findViewById(R.id.editTextYear);
        editTextPages = findViewById(R.id.editTextPages);
        editTextRating = findViewById(R.id.editTextRating);
        editTextISBN = findViewById(R.id.editTextISBN);
        editTextRetailPrice = findViewById(R.id.editTextRetailPrice);
        editTextStock = findViewById(R.id.editTextStock);
        editTextDescription = findViewById(R.id.editTextDescription);
        autoCompleteGenre = findViewById(R.id.autoCompleteGenre);

        // Set up genre dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, GENRES);
        autoCompleteGenre.setAdapter(adapter);

        MaterialButton buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String title = editTextTitle.getText().toString().trim();
        String author = editTextAuthor.getText().toString().trim();

        if (title.isEmpty() || author.isEmpty()) {
            Toast.makeText(this, "Title and Author are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Parse optional numeric fields
        int year = parseInteger(editTextYear.getText().toString(), 0);
        int pages = parseInteger(editTextPages.getText().toString(), 0);
        double rating = parseDouble(editTextRating.getText().toString(), 0.0);
        double price = parseDouble(editTextRetailPrice.getText().toString(), 0.0);
        int stock = parseInteger(editTextStock.getText().toString(), 0);

        String genre = autoCompleteGenre.getText().toString();
        String isbn = editTextISBN.getText().toString().trim();
        String description = editTextDescription.getText().toString().trim();

        // Create new book object
        Book newBook = new Book(
                title, author, description, genre, 
                "Full content coming soon...", // Content placeholder
                isbn, "Unknown Publisher", "1st Edition", year, "English",
                price * 0.7, price, 0.0, stock, pages, rating
        );

        // Add to repository
        BookRepository.getInstance().addBook(newBook);

        Toast.makeText(this, "Book added to catalog!", Toast.LENGTH_SHORT).show();
        finish(); // Go back to inventory
    }

    private int parseInteger(String value, int defaultValue) {
        try {
            return value.isEmpty() ? defaultValue : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private double parseDouble(String value, double defaultValue) {
        try {
            return value.isEmpty() ? defaultValue : Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
