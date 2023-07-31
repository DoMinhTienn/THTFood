// ProductModel.java
package com.example.thtfood.Model;
public class Product {
    private String name;
    private String image;
    private String description;
    private double price;

    public Product() {
    }


    public Product(String name, String image, double price, String description) {
        this.name = name;
        this.image = image;
        this.price = price;
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



    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
