package com.example.thtfood.Model;

public class CartItem {
    private  String productId;
    private String productName;
    private int quantity;
    private double productPrice;

    public CartItem(String productId ,String productName, int quantity, double productPrice) {
        this.productId = productId;
        this.productName = productName;
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
}


