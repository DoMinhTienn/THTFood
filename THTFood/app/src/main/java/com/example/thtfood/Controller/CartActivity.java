package com.example.thtfood.Controller;

import androidx.annotation.NonNull;
import android.app.AlertDialog;
import android.content.DialogInterface;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thtfood.Model.CartItem;
import com.example.thtfood.Model.Order;
import com.example.thtfood.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CartActivity extends AppCompatActivity {
    private CartViewModel cartViewModel;
    private RecyclerView recyclerView;
    private TextView totalPriceTextView;

    private CartAdapter cartAdapter;
    private ImageButton imageButtonQuit;
    private String restaurantKey;

    private Button saveCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Intent intent = getIntent();
        if (intent != null) {
            restaurantKey = intent.getStringExtra("restaurantKey");
        }

        cartViewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(getApplication())).get(CartViewModel.class);
        cartViewModel.loadCartItemsFromSharedPreferences(this);

        recyclerView = findViewById(R.id.recyclerView);
        totalPriceTextView = findViewById(R.id.totalPriceTextView);

        imageButtonQuit = findViewById(R.id.imageButtonQuit);
        imageButtonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });

        cartViewModel.getCartItemsLiveData().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                if(cartItems.size() == 0)
                    saveCart.setEnabled(false);
                else
                    saveCart.setEnabled(true);
            }
        });
        // Khởi tạo CartAdapter với một danh sách rỗng ban đầu
        cartAdapter = new CartAdapter(cartViewModel.getCartItems());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(cartAdapter);

        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        cartAdapter.setOnRemoveItemClickListener(new CartAdapter.OnRemoveItemClickListener() {
            @Override
            public void onRemoveItemClick(int position) {
                cartViewModel.removeFromCart(position);
                cartViewModel.saveCartItemsToSharedPreferences(CartActivity.this);

                double total = cartViewModel.calculateTotal();
                totalPriceTextView.setText("Tổng tiền: " + (vndFormat.format(total)));

            }
        });
        // Quan sát LiveData và cập nhật giao diện khi dữ liệu thay đổi

        cartViewModel.getCartItemsLiveData().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                cartAdapter.updateCart(cartItems);
                double total = cartViewModel.calculateTotal();
                totalPriceTextView.setText("Tổng tiền: " + (vndFormat.format(total)) );
            }
        });

        saveCart = findViewById(R.id.dathangBtn);

        saveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog;
                alertDialog = new AlertDialog.Builder(CartActivity.this);
                alertDialog.setMessage("Xác nhận đặt hàng");
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference().child("orders");

                        ordersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(restaurantKey)) {

                                } else {
                                    ordersRef.setValue(restaurantKey);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                // Xử lý lỗi nếu có
                            }
                        });
                        DatabaseReference billRef = FirebaseDatabase.getInstance().getReference().child("orders").child(restaurantKey);
                        // Lấy danh sách các sản phẩm trong giỏ hàng từ CartViewModel
                        List<CartItem> products = cartViewModel.getCartItems();

                        // Tính tổng tiền của giỏ hàng
                        double totalAmount = cartViewModel.calculateTotal();

                        // Lấy ngày đặt hàng
                        String orderDate = getCurrentDate();
                        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                        // Tạo một đối tượng Order
                        String BillId = billRef.push().getKey();
                        Order order = new Order(currentUser.getUid(),orderDate, totalAmount, products);

                        // Lưu thông tin đơn hàng vào Firebase

                        billRef.child(BillId).setValue(order)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(CartActivity.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                            // Xóa giỏ hàng sau khi đã lưu đơn hàng thành công
                                            cartViewModel.clearCartItemsFromSharedPreferences(CartActivity.this);
                                            cartViewModel.clearCart();
                                            cartViewModel.saveCartItemsToSharedPreferences(CartActivity.this);
                                            cartAdapter.updateCart(new ArrayList<>());
                                            totalPriceTextView.setText("Tổng tiền: 0 VND");
                                            saveCart.setEnabled(false);
                                            finish();
                                            Intent cartIntent = new Intent( CartActivity.this, RatingActivity.class);
                                            cartIntent.putExtra("restaurantKey", restaurantKey);
                                            cartIntent.putExtra("billId", BillId);
                                            startActivity(cartIntent);
                                        } else {
                                            Toast.makeText(CartActivity.this, "Lỗi khi lưu đơn hàng", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
                alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert=alertDialog.create();
                alert.show();
            }
        });
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

}

