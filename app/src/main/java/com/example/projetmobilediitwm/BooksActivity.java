package com.example.projetmobilediitwm;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class BooksActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private BooksAdapter adapter;
    private DatabaseHelper db;
    private List<Book> books;
    private List<Book> filteredBooks;
    private SearchView searchViewBooks;
    private ImageView filterIcon;
    private ImageView addBookButton;
    private String selectedFilter = "Title";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        db = new DatabaseHelper(this);

        TextView greetingTextView = findViewById(R.id.greetingTextView);

        ImageView cartIcon = findViewById(R.id.cartIcon);

        cartIcon.setOnClickListener(v -> {
            Intent intent = new Intent(BooksActivity.this, CartActivity.class);
            startActivity(intent);
        });
        ImageView userBooksIcon = findViewById(R.id.userBooksIcon);

        userBooksIcon.setOnClickListener(v -> {
            Intent intent = new Intent(BooksActivity.this, UserBooksActivity.class);
            startActivity(intent);
        });



        String username = getLoggedInUsername();
        if (username != null) {
            greetingTextView.setText("Hello, " + username);
        }

        recyclerView = findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        searchViewBooks = findViewById(R.id.searchViewBooks);
        filterIcon = findViewById(R.id.filterIcon);
        addBookButton = findViewById(R.id.addBookButton);

        loadBooks();
        addBookButton.setOnClickListener(v -> {
            Intent intent = new Intent(BooksActivity.this, AddBookActivity.class);
            startActivityForResult(intent, 100);
        });
        filterIcon.setOnClickListener(v -> showFilterDialog());
        configureSearchView();
    }

    private String getLoggedInUsername() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String email = sharedPreferences.getString("email", null);

        if (email != null) {
            return db.getUsernameByEmail(email);
        } else {
            return "Guest";
        }
    }
    private String getLoggedInEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("email", null); // email de l'utilisateur connecté
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadBooks();
    }

    private void showFilterDialog() {
        final String[] filters = {"Title", "Author", "Category"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a filter")
                .setSingleChoiceItems(filters, getSelectedFilterIndex(filters), (dialog, which) -> {
                    selectedFilter = filters[which];
                    dialog.dismiss();
                })
                .create()
                .show();
    }


    private int getSelectedFilterIndex(String[] filters) {
        for (int i = 0; i < filters.length; i++) {
            if (filters[i].equals(selectedFilter)) {
                return i;
            }
        }
        return 0;
    }


    private void loadBooks() {
        books = db.getAllBooks();
        filteredBooks = new ArrayList<>(books);

        adapter = new BooksAdapter(filteredBooks);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BooksAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                Intent intent = new Intent(BooksActivity.this, DetailBookActivity.class);

                String bookUserEmail = db.getBookUserEmailById(String.valueOf(book.getId()));
                Log.d("BooksActivity", "Sending bookId: " + book.getId());
                intent.putExtra("bookId",String.valueOf(book.getId()));
                intent.putExtra("title", book.getTitle());
                intent.putExtra("author", book.getAuthor());
                intent.putExtra("price", book.getPrice());
                intent.putExtra("category", book.getCategory());
                intent.putExtra("cover", book.getCover());
                intent.putExtra("user_email", bookUserEmail);

                startActivity(intent);
            }
        });

    }


    private void configureSearchView() {
        searchViewBooks.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return true;
            }
        });
    }

    private void filterBooks(String query) {
        filteredBooks.clear();

        if (query.isEmpty()) {
            filteredBooks.addAll(books);
        } else {
            String lowerCaseQuery = query.toLowerCase();

            for (Book book : books) {
                switch (selectedFilter) {
                    case "Title":
                        if (book.getTitle().toLowerCase().contains(lowerCaseQuery)) {
                            filteredBooks.add(book);
                        }
                        break;
                    case "Author":
                        if (book.getAuthor().toLowerCase().contains(lowerCaseQuery)) {
                            filteredBooks.add(book);
                        }
                        break;
                    case "Category":
                        if (book.getCategory().toLowerCase().contains(lowerCaseQuery)) {
                            filteredBooks.add(book);
                        }
                        break;
                }
            }
        }

        adapter.notifyDataSetChanged();
    }


}
