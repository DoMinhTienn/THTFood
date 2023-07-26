package com.example.thtfood.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thtfood.R;

public class ProductDetailActivity extends AppCompatActivity {
    private int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        TextView textViewProductName = findViewById(R.id.productName);
        TextView textViewProductPrice = findViewById(R.id.productPrice);

        Intent intent = getIntent();
        if (intent != null) {
            String productName = intent.getStringExtra("product_name");
            double productPrice = intent.getDoubleExtra("product_price", 0);

            textViewProductName.setText(productName);
            textViewProductPrice.setText(String.valueOf(productPrice));
        }

        Button decreaseQuantityBtn = findViewById(R.id.decreaseQuantityBtn);
        Button increaseQuantityBtn = findViewById(R.id.increaseQuantityBtn);
        Button addToCartBtn = findViewById(R.id.addToCartBtn);

        final TextView quantityTextView = findViewById(R.id.quantity);
        decreaseQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantity > 1) {
                    quantity--;
                    quantityTextView.setText(String.valueOf(quantity));
                }
            }
        });

        increaseQuantityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity++;
                quantityTextView.setText(String.valueOf(quantity));
            }
        });

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thêm mã xử lý thêm món ăn vào giỏ hàng tại đây
                // Ví dụ: gửi yêu cầu đến máy chủ hoặc lưu thông tin vào cơ sở dữ liệu
            }
        });
    }
}