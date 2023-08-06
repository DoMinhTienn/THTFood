package com.example.thtfood.Controller;
import android.app.Application;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.thtfood.Model.CartItem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends AndroidViewModel  {
    private MutableLiveData<List<CartItem>> cartItemsLiveData = new MutableLiveData<>();
    private String ResId;
    private List<CartItem> cartItems = new ArrayList<>();

    public CartViewModel(@NonNull Application application) {
        super(application);
        loadCartItemsFromSharedPreferences(application.getApplicationContext());
    }
    public void SetResId(String resId){
        this.ResId = resId;
    }
    public String getResId(){
        return this.ResId;
}
    public LiveData<List<CartItem>> getCartItemsLiveData() {
        return cartItemsLiveData;
    }

    public void addToCart(CartItem cartItem) {
        boolean isProductExist = false;
        for (int i = 0; i < cartItems.size(); i++) {
            CartItem existingItem = cartItems.get(i);
            if (existingItem.getProductName().equals(cartItem.getProductName())) {
                // Sản phẩm đã tồn tại trong giỏ hàng, cập nhật số lượng
                existingItem.setQuantity(existingItem.getQuantity() + cartItem.getQuantity());
                isProductExist = true;
                break;
            }
        }
        if (!isProductExist) {
            // Sản phẩm chưa tồn tại trong giỏ hàng, thêm sản phẩm mới
            cartItems.add(cartItem);
        }
        cartItemsLiveData.setValue(new ArrayList<>(cartItems)); // Cập nhật LiveData
    }

    public void removeFromCart(int position) {
        if (position >= 0 && position < cartItems.size()) {
            cartItems.remove(position);
            cartItemsLiveData.setValue(new ArrayList<>(cartItems)); // Tạo danh sách mới và gán giá trị mới cho LiveData
        }
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public int getCartItemCount() {
        return cartItems.size();
    }

    public double calculateTotal() {
        double total = 0;
        for (CartItem cartItem : cartItems) {
            total += cartItem.getQuantity() * cartItem.getProductPrice();
        }
        return total;
    }

    public void saveCartItemsToSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String jsonCartItems = gson.toJson(cartItems);
        editor.putString("cartItems", jsonCartItems);
        editor.putString("ResId", ResId);
        editor.apply();
    }

    public void loadCartItemsFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String jsonCartItems = sharedPreferences.getString("cartItems", "");
        Type type = new TypeToken<List<CartItem>>() {}.getType();
        cartItems = gson.fromJson(jsonCartItems, type);
        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
        ResId = sharedPreferences.getString("ResId", null);
        cartItemsLiveData.setValue(new ArrayList<>(cartItems));
    }
    public void clearCart() {
        cartItems.clear();
    }
    public void clearCartItemsFromSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("cart", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
