package com.example.thtfood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth;

    TextView tv1, tv2, tv3, tv4;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        logout = view.findViewById(R.id.button);
        tv1 = view.findViewById(R.id.textView6);
        tv2 = view.findViewById(R.id.textView8);
        tv3 = view.findViewById(R.id.textView7);
        tv4 = view.findViewById(R.id.textView9);
        avatar = view.findViewById(R.id.imageView2);

        User user = UserManager.getInstance().getUser();
        if (user != null) {
            String userName = user.getName();
            String userEmail = user.getEmail();
            String userRole = user.getRole();
           String userAvatarPath = user.getAvatar_path();

            RequestOptions options = new RequestOptions()
                    .override(40, 40)
                    .fitCenter()
            .skipMemoryCache(true)  // Thêm dòng này để vô hiệu hóa cache
                    .diskCacheStrategy(DiskCacheStrategy.NONE);  // Vô hiệu hóa cache trên ổ đĩa

            Glide.with(this).load(userAvatarPath).into(avatar);

            tv1.setText(userName);
            tv2.setText(userEmail);
            tv3.setText(userRole);
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
}