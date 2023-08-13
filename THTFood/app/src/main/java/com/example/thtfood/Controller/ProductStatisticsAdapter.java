package com.example.thtfood.Controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.thtfood.Model.Product;
import com.example.thtfood.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductStatisticsAdapter extends RecyclerView.Adapter<ProductStatisticsAdapter.ViewHolder> {
    HashMap<String, Integer> productStatistics = new HashMap<>();
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    String restaurantId = currentUser.getUid();
    private List<String> productIds = new ArrayList<>();
    DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference()
            .child("restaurants")
            .child(restaurantId)
            .child("menu");
    public ProductStatisticsAdapter(HashMap<String, Integer> productStatistics) {
        this.productStatistics = productStatistics;
    }
    public void setProductIds(List<String> productIds) {
        this.productIds = productIds;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_statistics, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String productId = productIds.get(position);
        int quantity = productStatistics.get(productId);
        menuRef.child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String productName = snapshot.child("name").getValue(String.class);
                    String productImage = snapshot.child("image").getValue(String.class);

                    // Cập nhật tên và hình ảnh của sản phẩm trong giao diện
                    holder.productNameTextView.setText(productName);
                    Glide.with(holder.itemView.getContext()).load(productImage).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(holder.ImageViewProduct);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.quantityTextView.setText(String.valueOf(quantity));
    }

    @Override
    public int getItemCount() {
        return productIds.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView quantityTextView;
        ImageView ImageViewProduct;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.textViewName);
            quantityTextView = itemView.findViewById(R.id.textViewCount);
            ImageViewProduct = itemView.findViewById(R.id.ImageViewProduct);
        }
    }
}