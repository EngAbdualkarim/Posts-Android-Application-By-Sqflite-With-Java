package com.example.posts;


public class Post {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String summary;
    private String opinion;
    private String imagePath;
    private String currentDateTime;
    private int likes;
    private byte[] imageBytes;

    public Post(int id, String title, String author, String genre, String summary, String opinion, String imagePath, String date, int likes) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.summary = summary;
        this.opinion = opinion;
        this.imagePath = imagePath;
        this.currentDateTime = date;
        this.likes = likes;
    }


    public Post(String title, String author, String genre, String summary, String opinion,String currentDateTime) {
        this.title=title;
        this.author=author;
        this.genre=genre;
        this.summary=summary;
        this.opinion=opinion;
        this.currentDateTime=currentDateTime;
    }


    public byte[] getImageBytes() {
        return this.imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }
    // Getters and setters

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;}

        public String getGenre() {
            return genre;
        }

        public String getSummary() {
            return summary;
        }

        public String getOpinion() {
            return opinion;
        }

        public String getImagePath() {
            return imagePath;
        }

        public String getCurrentDateTime() {
            return currentDateTime;
        }

        public int getLikes() {
            return likes;
        }

    public void incrementLikes() {
        this.likes += 1;
    }

}