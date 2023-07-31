package com.example.thtfood.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.thtfood.Model.CartItem;
import com.example.thtfood.R;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    private CartViewModel cartViewModel;
    private RecyclerView recyclerView;
    private TextView totalPriceTextView;

    private CartAdapter cartAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getApplication())).get(CartViewModel.class);
        cartViewModel.loadCartItemsFromSharedPreferences(this);

        recyclerView = findViewById(R.id.recyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);
        // Khởi tạo CartAdapter với một danh sách rỗng ban đầu
        cartAdapter = new CartAdapter(cartViewModel.getCartItems());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        cartAdapter.setOnRemoveItemClickListener(new CartAdapter.OnRemoveItemClickListener() {
            @Override
            public void onRemoveItemClick(int position) {
                cartViewModel.removeFromCart(position);
                cartViewModel.saveCartItemsToSharedPreferences(CartActivity.this);

                double total = cartViewModel.calculateTotal();
                totalPriceTextView.setText("Tổng tiền: " + total + " VND");
            }
        });
        // Quan sát LiveData và cập nhật giao diện khi dữ liệu thay đổi
        cartViewModel.getCartItemsLiveData().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                cartAdapter.updateCart(cartItems);
                double total = cartViewModel.calculateTotal();
                totalPriceTextView.setText("Tổng tiền: " + total + " VND");
            }
        });
    }
}

