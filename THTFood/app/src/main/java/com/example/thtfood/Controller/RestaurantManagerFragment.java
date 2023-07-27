package com.example.thtfood.Controller;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thtfood.Model.User;
import com.example.thtfood.Model.UserManager;
import com.example.thtfood.R;
import com.google.android.play.core.integrity.v;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantManagerFragment extends Fragment {

    User user = UserManager.getInstance().getUser();
    DatabaseReference restaurantsRef = FirebaseDatabase.getInstance().getReference().child("restaurants");

    ImageButton imageButtonMenu;

    private ImageView restaurantAvatar;
    private TextView restaurantName;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RestaurantManagerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RestaurantManagerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RestaurantManagerFragment newInstance(String param1, String param2) {
        RestaurantManagerFragment fragment = new RestaurantManagerFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_manager, container, false);
        restaurantAvatar = view.findViewById(R.id.imageViewRestaurant);
        restaurantName = view.findViewById(R.id.textViewRestaurantName);
        imageButtonMenu = view.findViewById(R.id.imageButtonMenu);

        imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MenuRestaurantActivity.class));

            }
        });

        restaurantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot restaurantSnapshot : snapshot.getChildren()) {
                    // Lấy thông tin của nhà hàng từ dataSnapshot
                    String Name = restaurantSnapshot.child("name").getValue(String.class);
                    String ImageURL = restaurantSnapshot.child("avatar_path").getValue(String.class);

                    boolean restaurantIsActive = restaurantSnapshot.child("active").getValue(Boolean.class);

                    Glide.with(getActivity()).load(ImageURL).into(restaurantAvatar);
                    restaurantName.setText(Name);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}