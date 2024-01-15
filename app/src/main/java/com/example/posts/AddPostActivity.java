package com.example.posts;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddPostActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText titleEditText;
    private EditText authorEditText;
    private EditText genreEditText;
    private EditText summaryEditText;
    private EditText opinionEditText;

    private ImageView imageView;
    private Bitmap selectedImage;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        dbHelper = new DBHelper(this);

        titleEditText = findViewById(R.id.titleEditText);
        authorEditText = findViewById(R.id.authorEditText);
        genreEditText = findViewById(R.id.genreEditText);
        summaryEditText = findViewById(R.id.summaryEditText);
        opinionEditText = findViewById(R.id.opinionEditText);

        imageView = findViewById(R.id.imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        Button addButton = findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString();
                String author = authorEditText.getText().toString();
                String genre = genreEditText.getText().toString();
                String summary = summaryEditText.getText().toString();
                String opinion = opinionEditText.getText().toString();

                createNewPost(title, author, genre, summary, opinion);
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();

            try {
                selectedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                imageView.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private void createNewPost(String title, String author, String genre, String summary, String opinion) {
        byte[] imageBytes = getBytes(selectedImage);
        String currentDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        // Assuming you have a constructor or a method to set all these values for the Post object
        Post post = new Post(title, author, genre, summary, opinion,currentDateTime);

        // Now add the post and its imageBytes to the database
        long newRowId = dbHelper.addPost(post, imageBytes);

        if (newRowId != -1) {
            Toast.makeText(this, "Post added successfully", Toast.LENGTH_SHORT).show();
            setResult(RESULT_OK);
            finish();

        } else {
            Toast.makeText(this, "Failed to add post", Toast.LENGTH_SHORT).show();
        }

    }

    private byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
