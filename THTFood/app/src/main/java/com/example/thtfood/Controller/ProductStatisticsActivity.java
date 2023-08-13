package com.example.thtfood.Controller;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.thtfood.Model.Product;
import com.example.thtfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.O)
public class ProductStatisticsActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    String restaurantId = currentUser.getUid();
    private List<Product> products = new ArrayList<>();
    HashMap<String, Integer> productStatistics = new HashMap<>();
    DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child(restaurantId);
    AutoCompleteTextView dropdownTime;
    LocalDate date = LocalDate.now();
    private ProductStatisticsAdapter adapterProduct;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_statistics);
        dropdownTime = findViewById(R.id.dropdown_time);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String[] items = {"Tháng hiện tại", "Năm hiện tại", "Tất cả"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
        adapterProduct = new ProductStatisticsAdapter(productStatistics);
        recyclerView.setAdapter(adapterProduct);
        dropdownTime.setAdapter(adapter);

        dropdownTime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                productStatistics.clear();
                handleData(position);
            }
        });
    }

    private void handleData(int position){
        products.clear();
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for (DataSnapshot orderSnapshot : snapshot.getChildren()){
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        LocalDate currentDate = LocalDate.parse(orderDate, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault()));
                        DataSnapshot productsSnapshot = orderSnapshot.child("products");
                        for (DataSnapshot productSnapshot : productsSnapshot.getChildren()){
                            // Đây là một ví dụ về cách lấy thông tin của mỗi sản phẩm
                            String productId = productSnapshot.child("productId").getValue(String.class);
                            String productName = productSnapshot.child("productName").getValue(String.class);

                            int quantity = productSnapshot.child("quantity").getValue(Integer.class);
                            switch (position){
                                case 0:
                                    if (currentDate.getMonthValue() == date.getMonthValue() && currentDate.getYear() == date.getYear()) {
                                        if(productStatistics.containsKey(productId)){
                                            int q = productStatistics.get(productId);
                                            productStatistics.put(productId, quantity + q);
                                        } else{
                                            productStatistics.put(productId, quantity);
                                            adapterProduct.setProductIds(new ArrayList<>(productStatistics.keySet()));
                                        }
                                        }
                                    break;
                                case 1:
                                    if (currentDate.getYear() == date.getYear()) {
                                        if(productStatistics.containsKey(productId)){
                                            int q = productStatistics.get(productId);
                                            productStatistics.put(productId, quantity + q);
                                        } else{
                                            productStatistics.put(productId, quantity);
                                            adapterProduct.setProductIds(new ArrayList<>(productStatistics.keySet()));
                                        }
                                    }
                                    break;
                                case 2:
                                    if(productStatistics.containsKey(productId)){
                                        int q = productStatistics.get(productId);
                                        productStatistics.put(productId, quantity + q);
                                    } else{
                                        productStatistics.put(productId, quantity);
                                        adapterProduct.setProductIds(new ArrayList<>(productStatistics.keySet()));
                                    }
                                    break;
                            }
                        }
                        adapterProduct.notifyDataSetChanged();

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}