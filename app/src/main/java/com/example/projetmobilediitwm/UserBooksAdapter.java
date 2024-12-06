package com.example.projetmobilediitwm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class UserBooksAdapter extends RecyclerView.Adapter<UserBooksAdapter.UserBooksViewHolder> {
    private List<Book> userBooks;

    public UserBooksAdapter(List<Book> userBooks) {
        this.userBooks = userBooks;
    }

    @Override
    public UserBooksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book_user, parent, false);
        return new UserBooksViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserBooksViewHolder holder, int position) {
        Book book = userBooks.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return userBooks.size();
    }

    public static class UserBooksViewHolder extends RecyclerView.ViewHolder {
        private TextView bookTitle, bookAuthor, bookPrice, bookCategory;
        private ImageView bookCover;

        public UserBooksViewHolder(View itemView) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.UserbookTitle);
            bookAuthor = itemView.findViewById(R.id.UserBookAuthor);
            bookPrice = itemView.findViewById(R.id.UserBookPrice);
            bookCategory = itemView.findViewById(R.id.UserBookCategory);
            bookCover = itemView.findViewById(R.id.UserBookCover);
        }

        public void bind(Book book) {
            bookTitle.setText(book.getTitle());
            bookAuthor.setText(book.getAuthor());
            bookPrice.setText("Price: "  + book.getPrice() + "DT ");
            bookCategory.setText("Category: " + book.getCategory());
            Glide.with(itemView.getContext())
                    .load(book.getCover())
                    .placeholder(R.drawable.bookcover)
                    .into(bookCover);
        }
    }
}
