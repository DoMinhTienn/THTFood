package com.example.thtfood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;

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

        imageView = findViewById(R.id.imageRestarant); // Thay "imageView" bằng ID của ImageView trong layout của bạn
        imageView.setImageResource(R.drawable.bread);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2)); // Số cột là 2

        List<ProductModel> productList = new ArrayList<>();
        productList.add(new ProductModel("Product 1", 10.99));
        productList.add(new ProductModel("Product 2", 15.49));
        productList.add(new ProductModel("Product 3", 15.49));
        productList.add(new ProductModel("Product 4", 15.49));
        productList.add(new ProductModel("Product 5", 15.49));
        productList.add(new ProductModel("Product 6", 15.49));
        // Thêm các sản phẩm khác vào danh sách nếu cần thiết

        productAdapter = new ProductAdapter(productList);
        recyclerViewProducts.setAdapter(productAdapter);
    }
}