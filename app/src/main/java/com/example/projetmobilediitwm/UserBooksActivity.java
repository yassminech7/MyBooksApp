package com.example.projetmobilediitwm;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserBooksActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UserBooksAdapter adapter;
    private DatabaseHelper db;
    private List<Book> userBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_books);

        recyclerView = findViewById(R.id.recyclerViewUserBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);

        loadUserBooks();
    }

    private void loadUserBooks() {
        String currentUserEmail = getLoggedInEmail();
        userBooks = db.getBooksAddedByUser(currentUserEmail);

        adapter = new UserBooksAdapter(userBooks);
        recyclerView.setAdapter(adapter);
    }

    private String getLoggedInEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }
}
