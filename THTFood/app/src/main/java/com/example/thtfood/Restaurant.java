package com.example.thtfood;

public class Restaurant {
    private String name;
    private String avatar_path;

    private String address;

    public Restaurant(String name, String avatar_path, String address) {
        this.name = name;
        this.avatar_path = avatar_path;
        this.address = address;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar_path() {
        return avatar_path;
    }

    public void setAvatar_path(String avatar_path) {
        this.avatar_path = avatar_path;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
