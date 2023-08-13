package com.example.thtfood.Model;

public class CartItem {
    private  String productId;
    private String productName;
    private String image;
    private int quantity;
    private double productPrice;

    public CartItem(String productId ,String productName,String image, int quantity, double productPrice) {
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.quantity = quantity;
        this.productPrice = productPrice;
    }
    public String getProductId() {
        return productId;
    }
    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity =  quantity;
    }
    public double getProductPrice() {
        return productPrice;
    }
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}


