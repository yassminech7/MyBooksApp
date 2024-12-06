package com.example.projetmobilediitwm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class DetailBookActivity extends AppCompatActivity {

    private static final String TAG = "DetailBookActivity";
    private DatabaseHelper db;
    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_book);

        ImageView detailBookCover = findViewById(R.id.detailBookCover);
        TextView detailBookTitle = findViewById(R.id.detailBookTitle);
        TextView detailBookAuthor = findViewById(R.id.detailBookAuthor);
        TextView detailBookPrice = findViewById(R.id.detailBookPrice);
        TextView detailBookCategory = findViewById(R.id.detailBookCategory);
        Button deleteButton = findViewById(R.id.deleteButton);
        Button editButton = findViewById(R.id.editButton);
        Button addToCartButton = findViewById(R.id.addToCartButton);

        db = new DatabaseHelper(this);

        try {
            bookId = getIntent().getStringExtra("bookId");
            final String title = getIntent().getStringExtra("title");
            final String author = getIntent().getStringExtra("author");
            final String price = getIntent().getStringExtra("price");
            final String category = getIntent().getStringExtra("category");
            final String coverUrl = getIntent().getStringExtra("cover");
            final String bookUserEmail = getIntent().getStringExtra("user_email");

            String currentUserEmail = getCurrentUserEmail();
            Log.d(TAG, "Book ID : " + bookId);
            Log.d(TAG, "Book User Email: " + bookUserEmail);
            Log.d(TAG, "Current User Email: " + currentUserEmail);

            if (bookUserEmail != null && currentUserEmail != null && bookUserEmail.equals(currentUserEmail)) {
                deleteButton.setVisibility(View.VISIBLE);
                editButton.setVisibility(View.VISIBLE);
                addToCartButton.setVisibility(View.GONE);

                deleteButton.setOnClickListener(v -> deleteBook());
                editButton.setOnClickListener(v -> editBook());
            } else {
                deleteButton.setVisibility(View.GONE);
                editButton.setVisibility(View.GONE);
                addToCartButton.setVisibility(View.VISIBLE);

                addToCartButton.setOnClickListener(v -> addToCart(bookId, title, price));
            }

            detailBookTitle.setText(title != null ? title : "Unknown Title");
            detailBookAuthor.setText(author != null ? "Author: " + author : "Author: Unknown");
            detailBookPrice.setText(price != null ? "Price: " + price + " DT" : "Price: N/A");
            detailBookCategory.setText(category != null ? "Category: " + category : "Category: N/A");

            if (coverUrl != null && !coverUrl.isEmpty()) {
                Glide.with(this)
                        .load(Uri.parse(coverUrl))
                        .placeholder(R.drawable.bookcover)
                        .error(R.drawable.bookcover)
                        .into(detailBookCover);
            } else {
                detailBookCover.setImageResource(R.drawable.bookcover);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving book details", e);
            Toast.makeText(this, "Error loading details", Toast.LENGTH_SHORT).show();
            finish();
        }
    }


    private String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", "guest@example.com");
        Log.d(TAG, "Current User Email from SharedPreferences: " + email);
        return email;
    }

    private void deleteBook() {
        if (bookId != null) {
            boolean isDeleted = db.deleteBookById(bookId);
            if (isDeleted) {
                Toast.makeText(this, "Book successfully deleted", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error while deleting", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Unable to find this book", Toast.LENGTH_SHORT).show();
        }
    }

    private void editBook() {
        Intent intent = new Intent(DetailBookActivity.this, EditBookActivity.class);
        intent.putExtra("bookId", bookId);
        intent.putExtra("title", getIntent().getStringExtra("title"));
        intent.putExtra("author", getIntent().getStringExtra("author"));
        intent.putExtra("price", getIntent().getStringExtra("price"));
        intent.putExtra("category", getIntent().getStringExtra("category"));
        intent.putExtra("cover", getIntent().getStringExtra("cover"));
        intent.putExtra("user_email", getIntent().getStringExtra("user_email"));
        startActivity(intent);
    }
    private void addToCart(String bookId, String title, String price) {
        String currentUserEmail = getCurrentUserEmail();
        if (currentUserEmail.equals("guest@example.com")) {
            Toast.makeText(this, "Please log in to add to cart", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.isBookInCart(bookId, currentUserEmail)) {
            Toast.makeText(this, "This book is already in your cart", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isAdded = db.addToCart(bookId, currentUserEmail);
        if (isAdded) {
            Toast.makeText(this, "Book added to cart", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
        }
    }


}
