package com.example.projetmobilediitwm;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

public class AddBookActivity extends AppCompatActivity {

    private EditText bookTitleEditText, bookAuthorEditText, bookPriceEditText, bookCategoryEditText;
    private ImageView bookCoverImageView;
    private Button saveBookButton, selectCoverButton;
    private DatabaseHelper db;
    private Uri imageUri;

    private static final int PICK_IMAGE = 1;
    private static final int REQUEST_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        bookTitleEditText = findViewById(R.id.bookTitleEditText);
        bookAuthorEditText = findViewById(R.id.bookAuthorEditText);
        bookPriceEditText = findViewById(R.id.bookPriceEditText);
        bookCategoryEditText = findViewById(R.id.bookCategoryEditText);
        bookCoverImageView = findViewById(R.id.bookCoverImageView);
        saveBookButton = findViewById(R.id.saveBookButton);
        selectCoverButton = findViewById(R.id.selectCoverButton);

        db = new DatabaseHelper(this);

        selectCoverButton.setOnClickListener(v -> {
            if (checkAndRequestPermissions()) {
                openGallery();
            }
        });

        saveBookButton.setOnClickListener(v -> saveBookToDatabase());
    }

    private boolean checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_PERMISSION);
                return false;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION);
                return false;
            }
        }
        return true;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            Glide.with(this)
                    .load(imageUri)
                    .into(bookCoverImageView);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Toast.makeText(this, "Permission denied. Unable to access images.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveBookToDatabase() {
        String title = bookTitleEditText.getText().toString().trim();
        String author = bookAuthorEditText.getText().toString().trim();
        String price = bookPriceEditText.getText().toString().trim();
        String category = bookCategoryEditText.getText().toString().trim();

        String userEmail = getCurrentUserEmail();

        if (!title.isEmpty() && !author.isEmpty() && !price.isEmpty() && !category.isEmpty() && imageUri != null) {
            Log.d("AddBookActivity", "Données saisies : " +
                    "\nTitle: " + title +
                    "\nAuthor: " + author +
                    "\nPrice: " + price +
                    "\nCategory: " + category +
                    "\nUser Email: " + userEmail +
                    "\nCover Image URI: " + imageUri.toString());

            db.insertBook(title, author, price, imageUri.toString(), category, userEmail);
            Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddBookActivity.this, BooksActivity.class));
        } else {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentUserEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("email", "guest@example.com");
    }
}
