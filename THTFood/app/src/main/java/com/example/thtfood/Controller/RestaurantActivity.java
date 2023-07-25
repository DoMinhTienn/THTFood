package com.example.thtfood.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thtfood.Controller.ProductAdapter;
import com.example.thtfood.Model.Product;
import com.example.thtfood.R;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;

    private TextView nameTextView;
    private TextView addressTextView;
    private ImageView imageRestarant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        imageRestarant = findViewById(R.id.imageRestarant);
        nameTextView = findViewById(R.id.nameRes);
        addressTextView = findViewById(R.id.adressRes);

        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String address = intent.getStringExtra("address");
            String avatar = intent.getStringExtra("avatar");
            nameTextView.setText(name);
            addressTextView.setText(address);
            Glide.with(this).load(avatar).into(imageRestarant);
        }

        recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(this, 2)); // Số cột là 2

        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Cơm sườn nướng", 30000.0));
        productList.add(new Product("Cháo lòng ngon", 50000.0));
        productList.add(new Product("Cháo lòng ngon", 50000.0));
        productList.add(new Product("Cháo lòng ngon", 50000.0));
        productList.add(new Product("Cháo lòng ngon", 50000.0));
        productList.add(new Product("Cháo lòng ngon", 50000.0));
        // Thêm các sản phẩm khác vào danh sách nếu cần thiết

        productAdapter = new ProductAdapter(productList);
        recyclerViewProducts.setAdapter(productAdapter);

        productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
            @Override
            public void onProductClick(Product product) {
                // Xử lý khi sản phẩm được click, chẳng hạn chuyển sang màn hình chi tiết sản phẩm và truyền dữ liệu sản phẩm
                // qua Intent hoặc Bundle.
                Intent intent = new Intent(RestaurantActivity.this, ProductDetailActivity.class);
                intent.putExtra("product_name", product.getName());
                intent.putExtra("product_price", product.getPrice());
                startActivity(intent);
            }
        });

    }
}