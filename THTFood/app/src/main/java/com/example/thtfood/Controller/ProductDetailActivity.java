package com.example.thtfood.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thtfood.Model.CartItem;

import com.example.thtfood.R;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private int quantity = 1;
    private String productName;
    private  double productPrice;
    private CartViewModel cartViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        cartViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getApplication())).get(CartViewModel.class);

        TextView textViewProductName = findViewById(R.id.productName);
        TextView textViewProductPrice = findViewById(R.id.productPrice);

        Intent intent = getIntent();
        if (intent != null) {
            productName = intent.getStringExtra("product_name");
            productPrice = intent.getDoubleExtra("product_price", 0);

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
                CartItem cartItem = new CartItem(productName, quantity, productPrice);
                cartViewModel.addToCart(cartItem);
                cartViewModel.saveCartItemsToSharedPreferences(ProductDetailActivity.this);
                // Quan sát sự thay đổi trong LiveData và log danh sách sản phẩm khi có sự thay đổi
                Toast.makeText(ProductDetailActivity.this, "them thanh cong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}