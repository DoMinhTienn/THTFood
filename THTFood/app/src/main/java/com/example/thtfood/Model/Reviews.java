package com.example.thtfood.Model;

public class Reviews {
    private String image;
    private String username;
    private float rating;
    private String comment;

    public Reviews() {
        // Default constructor required for Firebase
    }

    public Reviews(String image, String username, float rating, String comment) {
        this.image = image;
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String name) {
         this.username = name;
    }
    public float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

