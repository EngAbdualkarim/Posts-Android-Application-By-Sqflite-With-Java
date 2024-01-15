package com.example.posts;


import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso; // Import Picasso

import java.util.ArrayList;
import android.graphics.BitmapFactory;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<Post> posts;

    public PostAdapter(ArrayList<Post> posts) {
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);

        // Set the post details in the ViewHolder
        holder.titleTextView.setText(post.getTitle());
        holder.authorTextView.setText(post.getAuthor());
        holder.genreTextView.setText(post.getGenre());
        holder.summaryTextView.setText(post.getSummary());
        holder.opinionTextView.setText(post.getOpinion());
        holder.dateTextView.setText(post.getCurrentDateTime());
        holder.likesTextView.setText(String.valueOf(post.getLikes()));

        Picasso.get().load(post.getImagePath()).into(holder.imageView);

        //
        byte[] imageBytes = post.getImageBytes();
        if (imageBytes != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            holder.imageView.setImageBitmap(bitmap);
        } else {

        }
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                post.incrementLikes(); // نضيف هذه الوظيفة في كلاس Post
                holder.likesTextView.setText(String.valueOf(post.getLikes()));
                DBHelper dbHelper = new DBHelper(holder.itemView.getContext());
                dbHelper.updateLikes(post.getId(), post.getLikes());
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) holder.itemView.getContext()).deletePost(post.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView authorTextView;
        TextView genreTextView;
        TextView summaryTextView;
        TextView opinionTextView;
        TextView dateTextView;
        TextView likesTextView;
        ImageView imageView;
        ImageButton deleteButton;
        ImageButton likeButton;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            authorTextView = itemView.findViewById(R.id.authorTextView);
            genreTextView = itemView.findViewById(R.id.genreTextView);
            summaryTextView = itemView.findViewById(R.id.summaryTextView);
            opinionTextView = itemView.findViewById(R.id.opinionTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            likesTextView = itemView.findViewById(R.id.likesTextView);
            imageView = itemView.findViewById(R.id.imageView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            likeButton = itemView.findViewById(R.id.likeButton);
        }
    }
}