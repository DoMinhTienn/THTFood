package com.example.thtfood.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.thtfood.Model.Product;
import com.example.thtfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MenuRestaurantActivity extends AppCompatActivity {
    private static final int REQUEST_ADD_MENU = 1;
    private List<Product> products = new ArrayList<>();
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private Button btnConfirm;
    private  TextView tvNoData;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_restaurant);
        recyclerView = findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter(products);
        recyclerView.setAdapter(menuAdapter);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvNoData = findViewById(R.id.tvNoData);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMenuActivity();
            }
        });
        loadData();
    }

    private void loadData(){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String restaurantId = currentUser.getUid();
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference()
                .child("restaurants")
                .child(restaurantId)
                .child("menu");
        products.clear();
        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    recyclerView.setVisibility(View.VISIBLE);
                    tvNoData.setVisibility(View.GONE);
                    menuRef.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Product product = snapshot.getValue(Product.class);
                            products.add(product);
                            menuAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else{
                    if (products.size() == 0) {
                        recyclerView.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    } else{
                        recyclerView.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void openAddMenuActivity() {
        Intent intent = new Intent(MenuRestaurantActivity.this, AddMenuActivity.class);
        startActivityForResult(intent, REQUEST_ADD_MENU);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ADD_MENU && resultCode == RESULT_OK) {
            loadData();
        }
    }
}