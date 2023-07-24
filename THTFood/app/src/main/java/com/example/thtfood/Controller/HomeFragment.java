package com.example.thtfood.Controller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.thtfood.R;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Tạo View cho Fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Lấy đối tượng LinearLayout từ View
        LinearLayout horizontalLayout = view.findViewById(R.id.horizontal_layout);
        LinearLayout verticalLayout = view.findViewById(R.id.vertical_layout);
        // Thêm các card vào horizontalLayout
        LayoutInflater cardInflater = LayoutInflater.from(getActivity());
        for (int i = 0; i < 5; i++) {
            View cardView = cardInflater.inflate(R.layout.reshor_card, horizontalLayout, false);
            // Thực hiện việc thiết lập dữ liệu cho cardView tại đây (như ảnh, tiêu đề, nội dung, v.v.)
//            ImageView imageView = cardView.findViewById(R.id.imageView);
//            TextView titleTextView = cardView.findViewById(R.id.titleTextView);
//
////            Thiết lập dữ liệu cho imageView và titleTextView
//            imageView.setImageResource(R.drawable.your_image);
//            titleTextView.setText("Your Title");
            horizontalLayout.addView(cardView);
        }
        LayoutInflater cardInflater2 = LayoutInflater.from(getActivity());
        for (int i = 0; i < 6; i++) {
            View cardView = cardInflater2.inflate(R.layout.resver_card, verticalLayout, false);
            // Thực hiện việc thiết lập dữ liệu cho cardView tại đây (như ảnh, tiêu đề, nội dung, v.v.)
            verticalLayout.addView(cardView);
        }


        // Inflate the layout for this fragment
        return view;
    }


}