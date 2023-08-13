package com.example.thtfood.Controller;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.thtfood.R;

public class StatisticsMainActivity extends AppCompatActivity {
    private Button btnRevenue, btnProduct;
    private ImageButton imageButtonQuit;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_main);
        btnRevenue = findViewById(R.id.btnRevenue);
        btnProduct = findViewById(R.id.btnProduct);
        imageButtonQuit = findViewById(R.id.imageButtonQuit);
        imageButtonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnRevenue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticsMainActivity.this, RevenueStatisticsActivity.class));
            }
        });

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StatisticsMainActivity.this, ProductStatisticsActivity.class));
            }
        });
    }
}