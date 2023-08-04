package com.example.thtfood.Controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.thtfood.Controller.ProductAdapter;
import com.example.thtfood.Model.CartItem;
import com.example.thtfood.Model.Product;
import com.example.thtfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantActivity extends AppCompatActivity {
    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;

    private TextView nameTextView;
    private TextView addressTextView;
    private ImageView imageRestarant;
    private String restaurantKey;
    private ArrayList<Product> productList;
    private CartViewModel cartViewModel;

    private ImageButton imageButtonQuit;
    private TextView sizecart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        cartViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getApplication())).get(CartViewModel.class);
        imageRestarant = findViewById(R.id.imageRestarant);
        nameTextView = findViewById(R.id.nameRes);
        addressTextView = findViewById(R.id.adressRes);
        imageButtonQuit = findViewById(R.id.imageButtonQuit);
        sizecart = findViewById(R.id.sizecart);
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("name");
            String address = intent.getStringExtra("address");
            String avatar = intent.getStringExtra("avatar");
            restaurantKey = intent.getStringExtra("restaurantKey");
            nameTextView.setText(name);
            addressTextView.setText(address);
            Glide.with(this).load(avatar).into(imageRestarant);
        }
        productList = new ArrayList<>();

        imageButtonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if(cartViewModel.getResId() == null || !cartViewModel.getResId().equals(restaurantKey))
        {

            cartViewModel.SetResId(restaurantKey);
            cartViewModel.clearCartItemsFromSharedPreferences(RestaurantActivity.this);
            cartViewModel.clearCart();
            cartViewModel.saveCartItemsToSharedPreferences(RestaurantActivity.this);

        }
        else{

        }
        DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(restaurantKey);
        // Lấy restaurant thông qua restaurantKey
        restaurantRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Kiểm tra xem nút "menu" có tồn tại trong restaurantkey không
                if (dataSnapshot.hasChild("menu")) {
                    DataSnapshot menuSnapshot = dataSnapshot.child("menu");
                    // Lặp qua từng node con trong nút "menu"
                    int numItemsToShow = 0;
                    for (DataSnapshot childSnapshot : menuSnapshot.getChildren()) {
                        numItemsToShow += 1;
                        String menuKey = childSnapshot.getKey();
                        // Lấy giá trị của thuộc tính "active"
                        Boolean isActive = childSnapshot.child("active").getValue(Boolean.class);

                        if (isActive != null && isActive) {
                            String namemenu = childSnapshot.child("name").getValue(String.class);
                            String descriptionmenu = childSnapshot.child("description").getValue(String.class);
                            String imagemenu = childSnapshot.child("image").getValue(String.class);
                            Double price = childSnapshot.child("price").getValue(Double.class);
                            Product product = new Product(menuKey, namemenu, imagemenu, price,descriptionmenu, true);
                            productList.add(product);
                        }
                    }

                    productAdapter = new ProductAdapter(productList);
                    recyclerViewProducts = findViewById(R.id.recyclerViewProducts);
                    recyclerViewProducts.setLayoutManager(new GridLayoutManager(RestaurantActivity.this, 2)); // Số cột là 2
                    recyclerViewProducts.setAdapter(productAdapter);

                    productAdapter.setOnProductClickListener(new ProductAdapter.OnProductClickListener() {
                        @Override
                        public void onProductClick(Product product) {

                            Intent intent = new Intent(RestaurantActivity.this, ProductDetailActivity.class);
                            intent.putExtra("restaurantKey", restaurantKey);
                            intent.putExtra("product_Key", product.getId());
                            intent.putExtra("product_name", product.getName());
                            intent.putExtra("product_price", product.getPrice());
                            startActivity(intent);
                        }
                    });

                } else {
                    // Xử lý khi không tìm thấy nút "menu" trong restaurant1
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra
            }
        });

        ImageView viewcart = findViewById(R.id.imageViewcart);
        viewcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến CartActivity khi người dùng nhấn vào nút xem giỏ hàng
                Intent cartIntent = new Intent( RestaurantActivity.this, CartActivity.class);
                cartIntent.putExtra("restaurantKey", restaurantKey);
                startActivity(cartIntent);
            }
        });


        cartViewModel.getCartItemsLiveData().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                sizecart.setText(String.valueOf(cartViewModel.getCartItemCount()));
            }
        });

    }
    @Override
    protected void onResume() {
        super.onResume();
        // Reload or refresh your activity here
        cartViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getApplication())).get(CartViewModel.class);
        cartViewModel.getCartItemsLiveData().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                sizecart.setText(String.valueOf(cartViewModel.getCartItemCount()));
            }
        });

    }

}