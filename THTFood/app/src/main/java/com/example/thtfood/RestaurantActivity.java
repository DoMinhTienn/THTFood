package com.example.thtfood.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.thtfood.Controller.ProductAdapter;
import com.example.thtfood.Model.Product;
import com.example.thtfood.R;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        imageView.setImageResource(R.drawable.bread);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2)); // Số cột là 2

        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Product 1", 10.99));
        productList.add(new Product("Product 2", 15.49));
        productList.add(new Product("Product 3", 15.49));
        productList.add(new Product("Product 4", 15.49));
        productList.add(new Product("Product 5", 15.49));
        productList.add(new Product("Product 6", 15.49));
        // Thêm các sản phẩm khác vào danh sách nếu cần thiết

        productAdapter = new ProductAdapter(productList);
        recyclerViewProducts.setAdapter(productAdapter);
    }
}