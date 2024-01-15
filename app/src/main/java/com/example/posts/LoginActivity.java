package com.example.posts;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText;
    private EditText passwordEditText;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DBHelper(this);

        // Check if the user is already logged in (COLUMN_IS_LOGGED_IN == 1)
        if (isUserLoggedIn()) {
            goToMainActivity();
            return;
        }

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button signInButton = findViewById(R.id.signInButton);
        Button signUpButton = findViewById(R.id.signUpButton);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signIn() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        long userId = loginUser(email, password);

        if (userId != -1) {
            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

            // Set is_logged_in to 1 for the logged-in user
            updateUserLoginStatus(userId, 1);

            // Pass the user ID to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Login failed. Please check your email and password.", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("Range")
    private long loginUser(String email, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"user_id"};
        String selection = "email = ? AND password = ?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);

        long userId = -1;

        if (cursor.moveToFirst()) {
            userId = cursor.getLong(cursor.getColumnIndex("user_id"));
        }

        cursor.close();
        return userId;
    }

    private void updateUserLoginStatus(long userId, int status) {
        ContentValues values = new ContentValues();
        values.put("is_logged_in", status);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String whereClause = "user_id = ?";
        String[] whereArgs = {String.valueOf(userId)};
        db.update("users", values, whereClause, whereArgs);
    }




    private boolean isUserLoggedIn() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"is_logged_in"};
        String selection = "is_logged_in = ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);

        boolean userLoggedIn = cursor.getCount() > 0;
        cursor.close();
        return userLoggedIn;
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
