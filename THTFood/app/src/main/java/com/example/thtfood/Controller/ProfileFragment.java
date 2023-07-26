package com.example.thtfood.Controller;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.thtfood.Model.User;
import com.example.thtfood.Model.UserManager;
import com.example.thtfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    FirebaseAuth mAuth;
    private boolean isRestaurant = false;

    public static final int REQUEST_IMAGE_GET = 1;
    TextView tv1;
    Button logout;
    ImageButton chooseImageButton;
    private ImageView avatar;

    User user = UserManager.getInstance().getUser();

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
        chooseImageButton = view.findViewById(R.id.choose_image_button);

        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

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

        //btn logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog;
                alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setMessage(getString(R.string.LogoutDialog));
                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAuth.signOut();
                        startActivity(new Intent(getContext(), MainActivity.class));
                    }
                });
                alertDialog.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog alert=alertDialog.create();
                alert.show();
            }
        });
        return view;
    }

//update avt
    private void openImagePicker() {
        // Mở hộp thoại để chọn hình ảnh từ thư viện
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GET);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                Uri selectedImageUri = data.getData();
                updateProfileImage(selectedImageUri);
            }
        }
    }
    private void updateProfileImage(Uri imageUri) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String uid = currentUser.getUid();
        // TODO: Thêm mã để cập nhật ảnh lên Firebase Storage và lưu đường dẫn vào Realtime Database
        if (imageUri != null) {
            // Trích xuất đuôi file từ Uri của ảnh
            ContentResolver contentResolver = requireActivity().getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String fileExtension = mime.getExtensionFromMimeType(contentResolver.getType(imageUri));

            // Kiểm tra xem có đuôi file hay không, nếu không thì mặc định là jpg
            if (fileExtension == null || fileExtension.isEmpty()) {
                fileExtension = "jpg";
            }

            // Tạo đường dẫn trong Firebase Storage với đuôi file tương ứng
            String filename = "avatar_" + uid; // Thay YOUR_USER_ID bằng ID của người dùng hiện tại
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("avatars")
                    .child(filename + "." + fileExtension);

            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Hiển thị hình ảnh từ Firebase lên ImageView và áp dụng circleCropTransform() để làm hình ảnh trở thành hình tròn
                        Glide.with(requireContext()).load(R.drawable.circle_bg).apply(RequestOptions.circleCropTransform()).into(avatar);
                        // Tải ảnh lên Storage thành công, lấy đường dẫn của ảnh
                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Lấy đường dẫn của ảnh từ Storage
                                String avatarPath = uri.toString();
                                // Cập nhật đường dẫn ảnh vào nhà hàng và lưu lại vào Realtime Database
                                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference()
                                        .child("users")
                                        .child(uid);
                                usersRef.child("avatar").setValue(avatarPath);
                                    Toast.makeText(requireContext(), "Cập nhật ảnh thành công", Toast.LENGTH_SHORT).show();
                                    // Update lại ImageView
                                    Glide.with(requireContext())
                                            .load(avatarPath)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(avatar);
                            }
                        });
                    }
                }
            });
        }
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