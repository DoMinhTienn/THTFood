package com.example.thtfood.Controller;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RestaurantHelper {
    public interface RestaurantCheckListener {
        void onCheckRestaurant(boolean isRestaurant);
    }
    public static void checkRestaurant(RestaurantCheckListener listener) {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference restaurantsRef = FirebaseDatabase.getInstance().getReference().child("restaurants");

        restaurantsRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isRestaurant = snapshot.exists();
                if (listener != null) {
                    listener.onCheckRestaurant(isRestaurant);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (listener != null) {
                    listener.onCheckRestaurant(false);
                }
            }
        });
    }
}
