package com.example.projetmobilediitwm;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {
    private List<Book> books;
    private OnItemClickListener listener;

    public BooksAdapter(List<Book> books) {
        this.books = books;
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BookViewHolder holder, int position) {
        Book book = books.get(position);

        if (book != null) {
            holder.title.setText(book.getTitle() != null ? book.getTitle() : "Title unknown");
            holder.author.setText(book.getAuthor() != null ? book.getAuthor() : "Author unknown");
            holder.price.setText(book.getPrice() != null ? book.getPrice() : "Price unknown");
            holder.category.setText(book.getCategory() != null ? book.getCategory() : "Category unknown");

            if (book.getCover() != null && !book.getCover().isEmpty()) {
                Glide.with(holder.itemView.getContext())
                        .load(book.getCover())
                        .placeholder(R.drawable.bookcover)
                        .error(R.drawable.bookcover)
                        .into(holder.bookImage);
            } else {
                holder.bookImage.setImageResource(R.drawable.bookcover);
            }

            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(book);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return books != null ? books.size() : 0;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        TextView title, author, price, category;
        ImageView bookImage;

        public BookViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.bookTitle);
            author = itemView.findViewById(R.id.bookAuthor);
            price = itemView.findViewById(R.id.bookPrice);
            category = itemView.findViewById(R.id.bookCategory);
            bookImage = itemView.findViewById(R.id.bookCover);
        }
    }
}
