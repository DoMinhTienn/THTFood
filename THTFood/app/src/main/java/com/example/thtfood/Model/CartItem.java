package com.example.thtfood.Model;

public class CartItem {
    private String productName;
    private int quantity;
    private double productPrice;

    public CartItem(String productName, int quantity, double productPrice) {
        this.productName = productName;
        this.quantity = quantity;
        this.productPrice = productPrice;
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


