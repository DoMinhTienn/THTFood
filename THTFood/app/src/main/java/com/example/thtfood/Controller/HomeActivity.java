package com.example.thtfood.Controller;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.thtfood.Model.User;
import com.example.thtfood.Model.UserManager;
import com.example.thtfood.R;
import com.example.thtfood.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    ActivityHomeBinding binding;
    User user = UserManager.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(user != null && "1".equals(user.getRole()) ){
            replaceFragment(new RestaurantManagerFragment());
        }
        else {
            replaceFragment(new HomeFragment());
        }
        setDefaultMenuItem();

        binding.botomNavigationView.setOnItemSelectedListener(item -> {

            if(item.getItemId() == R.id.home){
                if(user != null && "1".equals(user.getRole()) ){
                    replaceFragment(new RestaurantManagerFragment());
                }
                else {
                    replaceFragment(new HomeFragment());
                }
            }
            else if ( item.getItemId() == R.id.search) {
                replaceFragment(new SearchFragment());
            }
            else if (item.getItemId() == R.id.profile){
                replaceFragment(new ProfileFragment());
            }

            return true;
        });

    }
    private void setDefaultMenuItem() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.botomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }
    public void openResActivity(View view) {
        Intent intent = new Intent(this, RestaurantActivity.class);
        startActivity(intent);
    }
}