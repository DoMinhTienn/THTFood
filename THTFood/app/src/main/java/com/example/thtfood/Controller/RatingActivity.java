package com.example.thtfood.Controller;

import androidx.appcompat.app.AppCompatActivity;


import com.example.thtfood.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private EditText commentEditText;
    private Button submitButton;
    private String restaurantKey;
    private String billId;
    private ImageButton imageButtonQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        Intent intent = getIntent();
        if (intent != null) {
            restaurantKey = intent.getStringExtra("restaurantKey");
            billId = intent.getStringExtra("billId");
        }
        ratingBar = findViewById(R.id.ratingBar);
        commentEditText = findViewById(R.id.commentEditText);
        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                String comment = commentEditText.getText().toString();

                // Lưu đánh giá và bình luận vào cơ sở dữ liệu hoặc xử lý theo nhu cầu của bạn.
                DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                DatabaseReference billRef = databaseRef.child("orders").child(restaurantKey). child(billId);

                HashMap<String, Object> reviewData = new HashMap<>();
                reviewData.put("rating", rating);
                reviewData.put("comment", comment);
                billRef.child("reviews").setValue(reviewData);

                Toast.makeText(RatingActivity.this, "Đánh giá thành công", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        imageButtonQuit = findViewById(R.id.imageButtonQuit);
        imageButtonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}