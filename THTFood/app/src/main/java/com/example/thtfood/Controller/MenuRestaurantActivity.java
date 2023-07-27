package com.example.thtfood.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.thtfood.Model.Menu;
import com.example.thtfood.R;

import java.util.ArrayList;
import java.util.List;

public class MenuRestaurantActivity extends AppCompatActivity {
    private List<Menu> menuItems = new ArrayList<>();
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_restaurant);
        bindingData();
        recyclerView = findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        menuAdapter = new MenuAdapter(menuItems);
        recyclerView.setAdapter(menuAdapter);

    }

    private void bindingData(){
        menuItems.add(new Menu("Món 1", "",100000, true));
        menuItems.add(new Menu("Món 2", "",50000, true));
        menuItems.add(new Menu("Món 3", "",80000, true));
    }
}