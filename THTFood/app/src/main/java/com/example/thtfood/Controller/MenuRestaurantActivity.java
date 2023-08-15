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
import android.widget.ImageButton;
import android.widget.SearchView;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MenuRestaurantActivity extends AppCompatActivity implements MenuAdapter.OnMenuItemDeleteListener {
    private static final int REQUEST_ADD_MENU = 1;
    private List<Product> products = new ArrayList<>();
    private List<String> productsKey = new ArrayList<>();
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private Button btnConfirm;
    private ImageButton imageButtonQuit;
    private  TextView tvNoData;
    private SearchView searchView;
    private List<Product> filteredProducts = new ArrayList<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    String restaurantId = currentUser.getUid();
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_restaurant);
        searchView = findViewById(R.id.searchView);
        recyclerView = findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter(products);
        menuAdapter.setOnMenuItemDeleteListener(this);
        recyclerView.setAdapter(menuAdapter);
        btnConfirm = findViewById(R.id.btnConfirm);
        tvNoData = findViewById(R.id.tvNoData);
        imageButtonQuit = findViewById(R.id.imageButtonQuit);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterProducts(newText);
                return false;
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddMenuActivity();
            }
        });
        loadData();
        imageButtonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onMenuItemDelete(int position){

        showDeleteConfirmationDialog(position);
    }

    private void loadData(){
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
                            String key = snapshot.getKey();
                            productsKey.add(key);
                            products.add(product);
                            filteredProducts.add(product);
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
                    handleNoData();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void handleNoData(){
        if (products.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        } else{
            recyclerView.setVisibility(View.VISIBLE);
            tvNoData.setVisibility(View.GONE);
        }
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

    private void filterProducts(String query){
        filteredProducts.clear();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }
        menuAdapter.setData(filteredProducts);
        handleNoData();
    }
    private void showDeleteConfirmationDialog(int position) {
        CustomDialog dialog = new CustomDialog(this);
        dialog.setDialogMessage("Bạn có chắc chắn muốn xóa mục này?");
        dialog.setCustomDialogListener(new CustomDialog.CustomDialogListener() {
            @Override
            public void onOkButtonClicked() {
                if (position >= 0 && position < products.size()) {
                    Product product = products.get(position);
                    String productkey = productsKey.get(position);
                    DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference()
                            .child("restaurants")
                            .child(restaurantId)
                            .child("menu")
                            .child(productkey);
                    String imageUrl = product.getImage();

                    menuRef.removeValue();
                    StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(imageUrl);
                    storageReference.delete();

                    products.remove(position);
                    productsKey.remove(position);
                    handleNoData();
                    menuAdapter.notifyItemRemoved(position);
                }
            }
        });
        dialog.show();
    }

}