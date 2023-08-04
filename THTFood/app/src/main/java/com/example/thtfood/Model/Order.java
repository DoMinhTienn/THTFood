package com.example.thtfood.Model;

import java.util.List;

public class Order {
    private String UserId;
    private String orderDate;
    private double totalAmount;
    private List<CartItem> products;

    public Order() {
        // Empty constructor required for Firebase
    }

    public Order(String UserId, String orderDate, double totalAmount, List<CartItem> products) {
        this.UserId = UserId;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.products = products;
    }

    public String getOrderId() {
        return UserId;
    }

    public void setOrderId(String orderId) {
        this.UserId = orderId;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<CartItem> getproducts() {
        return products;
    }

    public void setproducts(List<CartItem> products) {
        this.products = products;
    }
}
