// ProductModel.java
package com.example.thtfood;
public class ProductModel {
    private String name;
    private double price;
    // Thêm các thuộc tính khác nếu cần thiết

    public ProductModel(String name, double price) {
        this.name = name;
        this.price = price;
    }

    // Thêm các getter/setter và phương thức khác nếu cần thiết
    public String getName() {
        return name;
    }

    // Phương thức setter cho thuộc tính name
    public void setName(String name) {
        this.name = name;
    }

    // Phương thức getter cho thuộc tính price
    public double getPrice() {
        return price;
    }

    // Phương thức setter cho thuộc tính price
    public void setPrice(double price) {
        this.price = price;
    }
}
