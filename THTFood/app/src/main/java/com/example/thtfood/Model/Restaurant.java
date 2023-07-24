package com.example.thtfood.Model;

import com.example.thtfood.Model.Address;

public class Restaurant {
    private String name;
    private String avatar_path;
    private Address address;
    private boolean isActive;



    public Restaurant(String name, String avatar_path, Address address, boolean isActive) {
        this.name = name;
        this.avatar_path = avatar_path;
        this.address = address;
        this.isActive = isActive;
    }

    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
