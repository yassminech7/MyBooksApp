package com.example.projetmobilediitwm;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private DatabaseHelper db;
    private List<Book> cartBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = new DatabaseHelper(this);

        loadCartBooks();
    }

    private void loadCartBooks() {
        String currentUserEmail = getLoggedInEmail();
        cartBooks = db.getBooksInCart(currentUserEmail);

        adapter = new CartAdapter(cartBooks, db);
        recyclerView.setAdapter(adapter);
    }

    private String getLoggedInEmail() {
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        return sharedPreferences.getString("email", null);
    }
}
