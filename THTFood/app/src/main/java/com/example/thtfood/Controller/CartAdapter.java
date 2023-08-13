package com.example.thtfood.Controller;
import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.thtfood.Model.CartItem;
import com.example.thtfood.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private List<CartItem> cartItems;
    private OnRemoveItemClickListener onRemoveItemClickListener;
    public CartAdapter(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    public void updateCart(List<CartItem> cartItems) {
        this.cartItems = cartItems;
        notifyDataSetChanged();
    }
    public interface OnRemoveItemClickListener {
        void onRemoveItemClick(int position);
    }
    public void setOnRemoveItemClickListener(OnRemoveItemClickListener listener) {
        this.onRemoveItemClickListener = listener;
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, @SuppressLint("RecyclerView") int position) {
        NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        CartItem cartItem = cartItems.get(position);
        holder.productNameTextView.setText(cartItem.getProductName());
        holder.quantityTextView.setText("Số lượng: " + String.valueOf(cartItem.getQuantity()));
        holder.priceTextView.setText("Giá: " + String.valueOf(vndFormat.format(cartItem.getProductPrice())));
        Glide.with(holder.imageFood.getContext())
                .load(cartItem.getImage())
                .into(holder.imageFood);
        holder.removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onRemoveItemClickListener != null) {
                    onRemoveItemClickListener.onRemoveItemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        TextView quantityTextView;
        TextView priceTextView;
        ImageButton removeButton;

        ImageView imageFood;
        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.orderInfoTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            imageFood = itemView.findViewById(R.id.productImageView);
            removeButton = itemView.findViewById(R.id.removeButton);
        }
    }
}

