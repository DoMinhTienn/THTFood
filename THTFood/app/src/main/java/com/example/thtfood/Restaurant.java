package com.example.thtfood;

public class Restaurant {
    private boolean active;
    private String address;
    private String avatar;
    private String name;

    // Constructor
    public Restaurant(boolean active, String address, String avatar, String name) {
        this.active = active;
        this.address = address;
        this.avatar = avatar;
        this.name = name;
    }

    // Phương thức getter cho thuộc tính "active"
    public boolean isActive() {
        return active;
    }

    // Phương thức setter cho thuộc tính "active"
    public void setActive(boolean active) {
        this.active = active;
    }

    // Phương thức getter cho thuộc tính "address"
    public String getAddress() {
        return address;
    }

    // Phương thức setter cho thuộc tính "address"
    public void setAddress(String address) {
        this.address = address;
    }

    // Phương thức getter cho thuộc tính "avatar"
    public String getAvatar() {
        return avatar;
    }

    // Phương thức setter cho thuộc tính "avatar"
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    // Phương thức getter cho thuộc tính "name"
    public String getName() {
        return name;
    }

    // Phương thức setter cho thuộc tính "name"
    public void setName(String name) {
        this.name = name;
    }
}
