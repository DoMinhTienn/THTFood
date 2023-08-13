// ProductModel.java
package com.example.thtfood.Model;
public class Product {
    private String id;
    private String name;
    private String image;
    private String description;
    private double price;
    private boolean active;
    private int quantity;
    public Product() {
    }

    public Product(String name, String image, double price, String description, boolean active) {
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.active = active;
    }
    public Product(String id, String name, String image, double price, String description, boolean active) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
        this.description = description;
        this.active = active;
    }

    public Product(String name, int quantity){
        this.name = name;
        this.quantity = quantity;

    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public String getId() {
        return id;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
