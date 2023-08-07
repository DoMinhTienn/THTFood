package com.example.thtfood.Controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.thtfood.Model.Order;
import com.example.thtfood.Model.User;
import com.example.thtfood.Model.UserManager;
import com.example.thtfood.R;
import com.google.android.play.core.integrity.v;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RestaurantManagerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RestaurantManagerFragment extends Fragment {

    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    String restaurantId = currentUser.getUid();
    DatabaseReference restaurantsRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(restaurantId);
    ImageButton imageButtonMenu, imageButtonInfo, imageButtonStatistics;
    private ImageView restaurantAvatar;

    private TextView restaurantName;
    private TextView textViewRevenueToday;
    Switch switchActiveRestaurant;
    private TextView textViewStateAcitve;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant_manager, container, false);
        restaurantAvatar = view.findViewById(R.id.imageViewRestaurant);
        restaurantName = view.findViewById(R.id.textViewRestaurantName);
        imageButtonMenu = view.findViewById(R.id.imageButtonMenu);
        imageButtonInfo = view.findViewById(R.id.imageButtonInfo);
        imageButtonStatistics = view.findViewById(R.id.imageButtonStatistics);
        switchActiveRestaurant = view.findViewById(R.id.switchActiveRestaurant);
        textViewStateAcitve = view.findViewById(R.id.textViewStateAcitve);
        textViewRevenueToday = view.findViewById(R.id.textViewRevenueToday);
        LocalDate today = LocalDate.now();

        imageButtonStatistics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StatisticsActivity.class));
            }
        });
        switchActiveRestaurant.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                restaurantsRef.child("active").setValue(isChecked);
                handleActiveRestaurant(isChecked);
            }
        });

        imageButtonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), MenuRestaurantActivity.class));
            }
        });

        imageButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), InfoRestaurantActivity.class));
            }
        });


        DatabaseReference orderRef = FirebaseDatabase.getInstance().getReference().child("orders").child(restaurantId);
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                double total = 0;
                NumberFormat vndFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                if (snapshot.exists()) {


                    for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                        String orderDate = orderSnapshot.child("orderDate").getValue(String.class);
                        int totalAmount = orderSnapshot.child("totalAmount").getValue(Integer.class);
                        LocalDate Date = LocalDate.parse(orderDate, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm", Locale.getDefault()));
                        if (Date.equals(today)) {
                            total += totalAmount;
                        }
                    }
                }
                textViewRevenueToday.setText(vndFormat.format(total));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        restaurantsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String Name = snapshot.child("name").getValue(String.class);
                String ImageURL = snapshot.child("avatar_path").getValue(String.class);
                boolean restaurantIsActive = snapshot.child("active").getValue(Boolean.class);
                restaurantName.setText(Name);
                Glide.with(getActivity()).load(ImageURL).into(restaurantAvatar);
                handleActiveRestaurant(restaurantIsActive);
                switchActiveRestaurant.setChecked(restaurantIsActive);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void handleActiveRestaurant(boolean active) {
        if (active) {

            textViewStateAcitve.setText("Đang hoạt động");
            textViewStateAcitve.setTextColor(Color.parseColor("#34c759"));
        } else {
            textViewStateAcitve.setText("Ngưng hoạt động");
            textViewStateAcitve.setTextColor(Color.parseColor("#FF1744"));
        }
    }

}