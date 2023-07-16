package com.example.thtfood;

import android.net.Uri;

public class User {
    private String name;
    private String email;
    private String role;
    private Uri avatar_path;

    public User(String name, String email, String role, Uri avatar_path) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.avatar_path = avatar_path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


    public Uri getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(Uri avatar_path) {
        this.avatar_path = avatar_path;
    }
}
