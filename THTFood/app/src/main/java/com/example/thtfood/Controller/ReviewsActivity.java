package com.example.thtfood.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import com.example.thtfood.Model.Reviews;
import com.example.thtfood.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ReviewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewAdapter adapter;
    private ArrayList<Reviews> reviewList;
    private String restaurantKey;
    private ImageButton imageButtonQuit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        Intent intent = getIntent();
        if (intent != null) {
            restaurantKey = intent.getStringExtra("restaurantKey");
        }
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        imageButtonQuit = findViewById(R.id.imageButtonQuit);
        imageButtonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        // Tạo danh sách đánh giá và bình luận (Review objects)
        DatabaseReference ordertRef = FirebaseDatabase.getInstance().getReference().child("orders").child(restaurantKey);
        ordertRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                reviewList = new ArrayList<>(); // Tạo danh sách đánh giá


                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    if (childSnapshot.hasChild("reviews")) {
                        DataSnapshot reviewSnapshot = childSnapshot.child("reviews");
                        String comment = reviewSnapshot.child("comment").getValue(String.class);
                        Float rating = reviewSnapshot.child("rating").getValue(Float.class);
                        String userId = childSnapshot.child("userId").getValue(String.class);

                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot userSnapshot) {
                                if (userSnapshot.exists()) {
                                    String avataUrl = userSnapshot.child("avatar").getValue(String.class);
                                    String username = userSnapshot.child("name").getValue(String.class);

                                    reviewList.add(new Reviews(avataUrl, username, rating, comment));
                                }
                                float totalRating = 0;
                                int totalReviews = reviewList.size();

                                for (Reviews review : reviewList) {
                                    totalRating += review.getRating();
                                }

                                float averageRating = totalRating / totalReviews;
                                double roundedNumber = Math.round(averageRating * 10.0) / 10.0;
                                TextView averageRatingTextView = findViewById(R.id.textReview);
                                averageRatingTextView.setText(("( " +roundedNumber +  " ★ và " +  totalReviews +  " nhận xét)"));
                                adapter = new ReviewAdapter(reviewList);
                                recyclerView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    } else {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi xảy ra trong truy vấn ordertRef
            }
        });
    }
}
