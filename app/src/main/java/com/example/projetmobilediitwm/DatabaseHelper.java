package com.example.projetmobilediitwm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "users.db";

    private static final String TABLE_NAME = "users";

    private static final String COL_ID = "id";
    private static final String COL_USERNAME = "username";
    private static final String COL_EMAIL = "email";
    private static final String COL_PASSWORD = "password";
    private static final String COL_GENDER = "gender";

    public static final String TABLE_BOOKS = "books";
    public static final String COL_BOOK_ID = "id";
    public static final String COL_BOOK_TITLE = "title";
    public static final String COL_BOOK_AUTHOR = "author";
    public static final String COL_BOOK_PRICE = "price";
    public static final String COL_BOOK_COVER = "cover";
    public static final String COL_BOOK_CATEGORY = "category";
    public static final String COL_BOOK_USER_EMAIL = "user_email";

    public static final String TABLE_CART = "cart";
    public static final String COL_CART_ID = "id";
    public static final String COL_CART_USER_EMAIL = "user_email";
    public static final String COL_CART_BOOK_ID = "book_id";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USERNAME + " TEXT, " +
                COL_EMAIL + " TEXT, " +
                COL_PASSWORD + " TEXT, " +
                COL_GENDER + " TEXT)";
        db.execSQL(createTable);

        String CREATE_TABLE_BOOKS = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BOOK_TITLE + " TEXT, " +
                COL_BOOK_AUTHOR + " TEXT, " +
                COL_BOOK_PRICE + " TEXT, " +
                COL_BOOK_COVER + " TEXT," +
        COL_BOOK_CATEGORY + " TEXT,"+
        COL_BOOK_USER_EMAIL + " TEXT);";
        db.execSQL(CREATE_TABLE_BOOKS);

        // Création de la table du panier
        String createTableCart = "CREATE TABLE " + TABLE_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_USER_EMAIL + " TEXT, " +
                COL_CART_BOOK_ID + " INTEGER)";
        db.execSQL(createTableCart);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertUser(String username, String email, String password, String gender) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_USERNAME, username);
        contentValues.put(COL_EMAIL, email);
        contentValues.put(COL_PASSWORD, password);
        contentValues.put(COL_GENDER, gender);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;

    }

    public boolean checkUserExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean validateUserByEmail(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_EMAIL + " = ? AND " + COL_PASSWORD + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{email, password});

        boolean isValid = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return isValid;
    }

    public void insertBook(String title, String author, String price, String cover, String category, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_BOOK_TITLE, title);
        contentValues.put(COL_BOOK_AUTHOR, author);
        contentValues.put(COL_BOOK_PRICE, price);
        contentValues.put(COL_BOOK_COVER, cover);
        contentValues.put(COL_BOOK_CATEGORY, category);
        contentValues.put(COL_BOOK_USER_EMAIL, userEmail);

        Log.d("DatabaseHelper", "Insertion du livre avec les valeurs : " +
                "\nTitle: " + title +
                "\nAuthor: " + author +
                "\nPrice: " + price +
                "\nCover: " + cover +
                "\nCategory: " + category +
                "\nUser Email: " + userEmail);

        long result = db.insert(TABLE_BOOKS, null, contentValues);
        if (result == -1) {
            Log.e("DatabaseHelper", "Insertion échouée");
        } else {
            Log.d("DatabaseHelper", "Livre inséré avec succès");
        }
    }


    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TITLE));
                String author = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_AUTHOR));
                String price = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_PRICE));
                String cover = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_COVER));
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_CATEGORY));

                Book book = new Book(id, title, author, price, cover, category);
                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return books;
    }
    public String getUsernameByEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        String username = "Guest";
        Cursor cursor = db.rawQuery("SELECT username FROM users WHERE email = ?", new String[]{email});
        if (cursor.moveToFirst()) {
            username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
        }
        cursor.close();
        db.close();
        return username;
    }

    public boolean deleteBookById(String bookId) {
        Log.d("DatabaseHelper", "Attempting to delete book with ID: " + bookId);
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BOOKS, COL_BOOK_ID + " = ?", new String[]{bookId});
        db.close();
        return result > 0;
    }

    public String getBookUserEmailById(String bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COL_BOOK_USER_EMAIL + " FROM " + TABLE_BOOKS + " WHERE " + COL_BOOK_ID + " = ?", new String[]{bookId});
        if (cursor.moveToFirst()) {
            String email = cursor.getString(0);
            cursor.close();
            return email;
        }
        cursor.close();
        return null;
    }

    public boolean updateBook(String bookId, String title, String author, String price, String category, String coverUrl, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOK_TITLE, title);
        values.put(COL_BOOK_AUTHOR, author);
        values.put(COL_BOOK_PRICE, price);
        values.put(COL_BOOK_CATEGORY, category);
        values.put(COL_BOOK_COVER, coverUrl);
        values.put(COL_BOOK_USER_EMAIL, userEmail);

        int result = db.update(TABLE_BOOKS, values, COL_BOOK_ID + " = ?", new String[]{bookId});
        db.close();
        return result > 0;
    }
    public Book getBookById(String bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS + " WHERE " + COL_BOOK_ID + " = ?", new String[]{bookId});

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_BOOK_ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TITLE));
            String author = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_AUTHOR));
            String price = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_PRICE));
            String cover = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_COVER));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_CATEGORY));

            cursor.close();
            db.close();
            return new Book(id, title, author, price, cover, category);
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public boolean addToCart(String bookId, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        if (isBookInCart(bookId, userEmail)) {
            Log.d("DatabaseHelper", "The book is already in the basket.");
            return false;
        }

        // Sinon, ajouter le livre au panier
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CART_USER_EMAIL, userEmail);
        contentValues.put(COL_CART_BOOK_ID, bookId);

        long result = db.insert(TABLE_CART, null, contentValues);
        if (result == -1) {
            Log.e("DatabaseHelper", "Failed to add book to cart.");
            return false;
        } else {
            Log.d("DatabaseHelper", "Book successfully added to cart.");
            return true;
        }
    }

    // Méthode pour vérifier si un livre est déjà dans le panier
    public boolean isBookInCart(String bookId, String userEmail) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CART + " WHERE " + COL_CART_BOOK_ID + " = ? AND " + COL_CART_USER_EMAIL + " = ?",
                new String[]{bookId, userEmail});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }
    public List<Book> getBooksInCart(String userEmail) {
        List<Book> cartBooks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM cart WHERE user_email = ?", new String[]{userEmail});
        if (cursor.moveToFirst()) {
            do {
                String bookId = cursor.getString(cursor.getColumnIndex("book_id"));
                Book book = getBookById(bookId);
                cartBooks.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartBooks;
    }
    public List<Book> getBooksAddedByUser(String userEmail) {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM books WHERE user_email = ?", new String[]{userEmail});

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book();
                book.setId(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                book.setTitle(cursor.getString(cursor.getColumnIndexOrThrow("title")));
                book.setAuthor(cursor.getString(cursor.getColumnIndexOrThrow("author")));
                book.setPrice(cursor.getString(cursor.getColumnIndexOrThrow("price")));
                book.setCategory(cursor.getString(cursor.getColumnIndexOrThrow("category")));
                book.setCover(cursor.getString(cursor.getColumnIndexOrThrow("cover")));
                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return books;
    }
    public void removeBookFromCart(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("cart", "book_id = ?", new String[]{String.valueOf(book.getId())});
        db.close();
    }


}
