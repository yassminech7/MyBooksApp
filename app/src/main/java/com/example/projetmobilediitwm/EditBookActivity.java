package com.example.projetmobilediitwm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class EditBookActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText editTitle, editAuthor, editPrice, editCategory;
    private ImageView editCoverImage;
    private Button saveButton;
    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        editTitle = findViewById(R.id.editTitle);
        editAuthor = findViewById(R.id.editAuthor);
        editPrice = findViewById(R.id.editPrice);
        editCategory = findViewById(R.id.editCategory);
        editCoverImage = findViewById(R.id.editCoverImage);
        saveButton = findViewById(R.id.saveButton);

        db = new DatabaseHelper(this);

        bookId = getIntent().getStringExtra("bookId");
        String title = getIntent().getStringExtra("title");
        String author = getIntent().getStringExtra("author");
        String price = getIntent().getStringExtra("price");
        String category = getIntent().getStringExtra("category");
        String coverUrl = getIntent().getStringExtra("cover");
        String userEmail = getIntent().getStringExtra("user_email");

        editTitle.setText(title);
        editAuthor.setText(author);
        editPrice.setText(price);
        editCategory.setText(category);

        if (coverUrl != null && !coverUrl.isEmpty()) {
            Glide.with(this)
                    .load(coverUrl)
                    .placeholder(R.drawable.bookcover)
                    .error(R.drawable.bookcover)
                    .into(editCoverImage);
        }

        saveButton.setOnClickListener(v -> saveBook());
    }

    private void saveBook() {
        String title = editTitle.getText().toString().trim();
        String author = editAuthor.getText().toString().trim();
        String price = editPrice.getText().toString().trim();
        String category = editCategory.getText().toString().trim();

        String coverUrl = getIntent().getStringExtra("cover");
        String userEmail = getIntent().getStringExtra("user_email");

        if (title.isEmpty() || author.isEmpty() || price.isEmpty() || category.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
        } else {
            boolean isUpdated = db.updateBook(bookId, title, author, price, category, coverUrl, userEmail);
            if (isUpdated) {
                Toast.makeText(this, "Book updated successfully", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(EditBookActivity.this, DetailBookActivity.class);
                intent.putExtra("bookId", bookId);
                intent.putExtra("title", title);
                intent.putExtra("author", author);
                intent.putExtra("price", price);
                intent.putExtra("category", category);
                intent.putExtra("cover", coverUrl);
                intent.putExtra("user_email", userEmail);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Failed to update book", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
