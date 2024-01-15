package com.example.posts;

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

public class SignUpActivity extends AppCompatActivity {
    private EditText usernameEditText;
    private EditText dobEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(this);

        // Check if the user is already logged in (COLUMN_IS_LOGGED_IN == 1)
        if (isUserLoggedIn()) {
            goToMainActivity();
            return;
        }

        setContentView(R.layout.activity_signup);

        usernameEditText = findViewById(R.id.usernameEditText);
        dobEditText = findViewById(R.id.dobEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);

        Button signUpButton = findViewById(R.id.signUpButton);
        Button loginButton = findViewById(R.id.loginButton);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void signUp() {
        String username = usernameEditText.getText().toString();
        String dob = dobEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if (username.isEmpty() || dob.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (isUserExists(email)) {
            Toast.makeText(this, "User already exists. Please log in.", Toast.LENGTH_SHORT).show();
            return;
        }

        long userId = createUser(username, dob, email, password);
        if (userId != -1) {
            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
            // Pass the user ID to MainActivity
            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
            intent.putExtra("user_id", userId);
            startActivity(intent);
            finish();
        }  else {
            Toast.makeText(this, "Failed to register. Please try again.", Toast.LENGTH_SHORT).show();
        }
    }


    private long createUser(String username, String dob, String email, String password) {
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("date_of_birth", dob);
        values.put("email", email);
        values.put("password", password);
        values.put("is_logged_in",1);

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        long newRowId = db.insert("users", null, values);
        db.close();

        return newRowId; // Return the ID of the newly created user
    }

    private boolean isUserExists(String email) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {"email"};
        String selection = "email = ?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query("users", projection, selection, selectionArgs, null, null, null);

        boolean userExists = cursor.getCount() > 0;
        cursor.close();
        return userExists;
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
