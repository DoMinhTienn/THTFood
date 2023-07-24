package com.example.thtfood.Controller;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.thtfood.Model.User;
import com.example.thtfood.Model.UserManager;
import com.example.thtfood.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth;
    private boolean isRestaurant = false;
    TextView tv1;
    Button logout;
    private ImageView avatar;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        mAuth = FirebaseAuth.getInstance();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logout = view.findViewById(R.id.buttonQuite);
        tv1 = view.findViewById(R.id.textView6);
        avatar = view.findViewById(R.id.profile_image);

        User user = UserManager.getInstance().getUser();
        if (user != null) {
            String userName = user.getName();
            String role = user.getRole();
           String userAvatarPath = user.getAvatar_path();

            RequestOptions options = new RequestOptions()
                    .override(40, 40)
                    .fitCenter()
            .skipMemoryCache(true)  // Thêm dòng này để vô hiệu hóa cache
                    .diskCacheStrategy(DiskCacheStrategy.NONE);  // Vô hiệu hóa cache trên ổ đĩa

            Glide.with(this).load(userAvatarPath).into(avatar);

            tv1.setText(userName);
        }
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAuth.signOut();
                    startActivity(new Intent(getContext(), MainActivity.class));
                }
            });
        return view;
        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handleDisplay();
    }

        private void handleDisplay() {
            View v = getView();
            Button button4 = v.findViewById(R.id.button4);
            Button button5 = v.findViewById(R.id.button5);
            Button button6 = v.findViewById(R.id.button6);
            Button button7 = v.findViewById(R.id.button7);
            Button buttonQuite = v.findViewById(R.id.buttonQuite);
            User user = UserManager.getInstance().getUser();
            if (user != null && "1".equals(user.getRole())) {
                {
                    RestaurantHelper.checkRestaurant(isRestaurant -> {
                        // Xử lý kết quả trả về tùy thuộc vào giá trị của isRestaurant
                        if (isRestaurant) {
                            // Đã là nhà hàng
                        } else {
                            button5.setVisibility(View.GONE);
                            button6.setVisibility(View.GONE);
                            button7.setVisibility(View.GONE);
                            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) buttonQuite.getLayoutParams();
                            layoutParams.topToBottom = R.id.button4; // Đặt buttonQuite nằm dưới button4
                            buttonQuite.setLayoutParams(layoutParams);
                            button4.setText("Tạo nhà hàng");
                            button4.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getActivity(), CreateRestaurantAcittivity.class));
                                }
                            });
                        }
                    });
                }
            }
        }
}