package com.example.thtfood.Controller;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thtfood.Model.CartItem;

import com.example.thtfood.R;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {
    private int quantity = 1;
    private String productId;
    private String productName;
    private  double productPrice;
    private String restaurantKey;
    private CartViewModel cartViewModel;
    private ImageButton imageButtonQuit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        cartViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getApplication())).get(CartViewModel.class);

        TextView textViewProductName = findViewById(R.id.productName);
        TextView textViewProductPrice = findViewById(R.id.productPrice);
        TextView sizecart = findViewById(R.id.sizecart);

        cartViewModel.getCartItemsLiveData().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                sizecart.setText(String.valueOf(cartViewModel.getCartItemCount()));
            }
        });

        imageButtonQuit = findViewById(R.id.imageButtonQuit);
        imageButtonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            restaurantKey = intent.getStringExtra("restaurantKey");
            productId =  intent.getStringExtra("product_Key");
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
                CartItem cartItem = new CartItem(productId, productName, quantity, productPrice);
                cartViewModel.addToCart(cartItem);
                cartViewModel.saveCartItemsToSharedPreferences(ProductDetailActivity.this);
                // Quan sát sự thay đổi trong LiveData và log danh sách sản phẩm khi có sự thay đổi
                Toast.makeText(ProductDetailActivity.this, "thêm thành công", Toast.LENGTH_SHORT).show();
            }
        });
        ImageView viewcart = findViewById(R.id.imageViewcart);
        viewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến CartActivity khi người dùng nhấn vào nút xem giỏ hàng
                Intent cartIntent = new Intent(ProductDetailActivity.this, CartActivity.class);
                cartIntent.putExtra("restaurantKey", restaurantKey);
                startActivity(cartIntent);
            }
        });
    }
}