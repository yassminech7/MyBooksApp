package com.example.projetmobilediitwm;

public class Book {
    private int id;
    private String title;
    private String author;
    private String price;
    private String cover;
    private String category;

    public Book(int id, String title, String author, String price, String cover, String category) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.cover = cover;
        this.category = category;
    }

    public Book() {
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPrice() {
        return price;
    }

    public String getCover() {
        return cover;
    }

    public String getCategory() {
        return category;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
