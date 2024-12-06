package com.example.projetmobilediitwm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<Book> cartBooks;
    private DatabaseHelper db;
    public CartAdapter(List<Book> cartBooks, DatabaseHelper db) {
        this.cartBooks = cartBooks;
        this.db = db;
    }

    @Override
    public CartViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CartViewHolder holder, int position) {
        Book book = cartBooks.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return cartBooks.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        private TextView bookTitle, bookAuthor, bookPrice, bookCategory;
        private ImageView bookCover, deleteIcon;

        public CartViewHolder(View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.cartBookTitle);
            bookAuthor = itemView.findViewById(R.id.cartBookAuthor);
            bookPrice = itemView.findViewById(R.id.cartBookPrice);
            bookCategory = itemView.findViewById(R.id.cartBookCategory);
            bookCover = itemView.findViewById(R.id.cartBookCover);
            deleteIcon = itemView.findViewById(R.id.cartBookDelete);
        }

        public void bind(Book book) {
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            bookPrice.setText("Price: " + book.getPrice() + "DT ");
            bookCategory.setText("Category: " + book.getCategory());
            Glide.with(itemView.getContext())
                    .load(book.getCover())
                    .placeholder(R.drawable.bookcover)
                    .into(bookCover);
            deleteIcon.setOnClickListener(v -> {
                cartBooks.remove(getAdapterPosition());
                notifyItemRemoved(getAdapterPosition());
                db.removeBookFromCart(book);
                Toast.makeText(itemView.getContext(), "Book successfully removed from your cart", Toast.LENGTH_SHORT).show();
            });
        }
    }
}

