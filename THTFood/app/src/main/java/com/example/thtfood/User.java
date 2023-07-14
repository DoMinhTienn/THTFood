package com.example.thtfood;

public class User {
    private String name;
    private String email;
    private String password;
    private String avatar_path;

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


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }



    public User(String name, String email, String phone, String password, String avatar_path) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.avatar_path = avatar_path;
    }

}
