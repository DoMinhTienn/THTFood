package com.example.thtfood.Controller;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thtfood.Model.Restaurant;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import androidx.lifecycle.ViewModelProvider;

import java.util.ArrayList;
import java.util.List;

import com.example.thtfood.R;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    LinearLayout horizontalLayout;

    DatabaseReference restaurantsRef = FirebaseDatabase.getInstance().getReference().child("restaurants");
    List<Restaurant> restaurantList = new ArrayList<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        LinearLayout horizontalLayout = view.findViewById(R.id.horizontal_layout);
        LinearLayout verticalLayout = view.findViewById(R.id.vertical_layout);


        restaurantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                horizontalLayout.removeAllViews();
                verticalLayout.removeAllViews();
                for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                    // Lấy thông tin của nhà hàng từ dataSnapshot
                    String restaurantName = restaurantSnapshot.child("name").getValue(String.class);
                    String restaurantImageURL = restaurantSnapshot.child("avatar_path").getValue(String.class);
                    DataSnapshot addressSnapshot = restaurantSnapshot.child("address");
                    String city = addressSnapshot.child("city").getValue(String.class);
                    String district = addressSnapshot.child("district").getValue(String.class);
                    String ward = addressSnapshot.child("ward").getValue(String.class);
                    String address = ward + " " + district + " " + city;
                    String restaurantKey = restaurantSnapshot.getKey();

                    boolean restaurantIsActive = restaurantSnapshot.child("active").getValue(Boolean.class);

                    // Tạo cardView từ reshor_card layout
                    View cardView = getLayoutInflater().inflate(R.layout.reshor_card, horizontalLayout, false);
                    View cardView2 = getLayoutInflater().inflate(R.layout.resver_card, verticalLayout, false);

                    ImageView imageView = cardView.findViewById(R.id.image_view);
                    TextView titleTextView = cardView.findViewById(R.id.title_text_view);
                    TextView subtitleTextView = cardView.findViewById(R.id.subtitle_text_view);

                    ImageView imageView2 = cardView2.findViewById(R.id.image_view2);
                    TextView titleTextView2 = cardView2.findViewById(R.id.title_text_view2);
                    TextView subtitleTextView2 = cardView2.findViewById(R.id.subtitle_text_view2);


                    Glide.with(getActivity()).load(restaurantImageURL).into(imageView);
                    titleTextView.setText(restaurantName);
                    subtitleTextView.setText(address);

                    Glide.with(getActivity()).load(restaurantImageURL).into(imageView2);
                    titleTextView2.setText(restaurantName);
                    subtitleTextView2.setText(address);
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Tạo Intent và truyền dữ liệu của nhà hàng qua Intent
                            Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                            intent.putExtra("name", restaurantName);
                            intent.putExtra("address", address);
                            intent.putExtra("avatar", restaurantImageURL);
                            intent.putExtra("restaurantKey", restaurantKey);
                            startActivity(intent);
                        }
                    });
                    cardView2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Tạo Intent và truyền dữ liệu của nhà hàng qua Intent
                            Intent intent = new Intent(getActivity(), RestaurantActivity.class);
                            intent.putExtra("name", restaurantName);
                            intent.putExtra("address", address);
                            intent.putExtra("avatar", restaurantImageURL);
                            intent.putExtra("restaurantKey", restaurantKey);
                            startActivity(intent);
                        }
                    });
                    // Thêm cardView vào horizontalLayout
                    horizontalLayout.addView(cardView);
                    verticalLayout.addView(cardView2);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return view;
    }
}