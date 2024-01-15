package com.example.posts;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "posts.db";

    private static final int DATABASE_VERSION = 2;

    //post table
    // Add a new column for the image
    private static final String COLUMN_IMAGE = "image";
    // Table and column names
    private static final String TABLE_POSTS = "posts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_AUTHOR = "author";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_SUMMARY = "summary";
    private static final String COLUMN_OPINION = "opinion";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LIKES = "likes";

    //usertable

    private static final String TABLE_USERS = "users";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    private static final String COLUMN_IS_LOGGED_IN = "is_logged_in";



    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_POSTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_AUTHOR + " TEXT, " +
                COLUMN_GENRE + " TEXT, " +
                COLUMN_SUMMARY + " TEXT, " +
                COLUMN_OPINION + " TEXT, " +
                COLUMN_IMAGE_PATH + " TEXT, " +
                COLUMN_IMAGE + " BLOB, " +   // Add this line
                COLUMN_DATE + " TEXT, " +
                COLUMN_LIKES + " INTEGER" +
                ")";
        db.execSQL(createTable);

        //create user table
        String createUserTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT, " +
                COLUMN_EMAIL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_DATE_OF_BIRTH + " TEXT, " +
                COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0" +
                ")";
        db.execSQL(createUserTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_POSTS + " ADD COLUMN " + COLUMN_IMAGE + " BLOB");
        }
    }

    public long addPost(Post post,byte[] imageBytes) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, post.getTitle());
        values.put(COLUMN_AUTHOR, post.getAuthor());
        values.put(COLUMN_GENRE, post.getGenre());
        values.put(COLUMN_SUMMARY, post.getSummary());
        values.put(COLUMN_OPINION, post.getOpinion());
        values.put(COLUMN_IMAGE_PATH, post.getImagePath());
        values.put(COLUMN_DATE, post.getCurrentDateTime());
        values.put(COLUMN_LIKES, post.getLikes());
        //
        values.put(COLUMN_IMAGE, imageBytes);

        long id = db.insert(TABLE_POSTS, null, values);
        db.close();

        return id;
    }

    public ArrayList<Post> getAllPosts() {
        ArrayList<Post> posts = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_POSTS, null, null, null, null, null, COLUMN_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                @SuppressLint("Range") String title = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
                @SuppressLint("Range") String author = cursor.getString(cursor.getColumnIndex(COLUMN_AUTHOR));
                @SuppressLint("Range")  String genre = cursor.getString(cursor.getColumnIndex(COLUMN_GENRE));
                @SuppressLint("Range")  String summary = cursor.getString(cursor.getColumnIndex(COLUMN_SUMMARY));
                @SuppressLint("Range")  String opinion = cursor.getString(cursor.getColumnIndex(COLUMN_OPINION));
                @SuppressLint("Range")  String imagePath = cursor.getString(cursor.getColumnIndex(COLUMN_IMAGE_PATH));
                @SuppressLint("Range")  String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range")  int likes = cursor.getInt(cursor.getColumnIndex(COLUMN_LIKES));

                Post post = new Post(id, title, author, genre, summary, opinion, imagePath, date, likes);
                //
                @SuppressLint("Range") byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
                post.setImageBytes(imageBytes);
                //
                posts.add(post);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return posts;
    }
    public void deletePost(int postId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_POSTS, COLUMN_ID + "=?", new String[]{String.valueOf(postId)});
        db.close();
    }

    public void updateLikes(int postId, int likes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LIKES, likes);
        db.update(TABLE_POSTS, values, COLUMN_ID + "=?", new String[]{String.valueOf(postId)});
        db.close();
    }

}