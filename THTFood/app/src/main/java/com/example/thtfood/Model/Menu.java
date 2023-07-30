package com.example.thtfood.Model;

public class Menu {
    private String name;
    private String image;
    private String description;
    private double price;

    public Menu() {
    }

    private boolean active;

    public Menu(String name, String image, double price, String description,  boolean active) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.active = active;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
