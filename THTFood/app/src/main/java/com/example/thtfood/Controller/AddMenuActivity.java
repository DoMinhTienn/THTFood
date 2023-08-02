package com.example.thtfood.Controller;

import static com.example.thtfood.Controller.CreateRestaurantAcittivity.REQUEST_IMAGE_GET;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.thtfood.Model.Product;
import com.example.thtfood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.Nullable;

public class AddMenuActivity extends AppCompatActivity {

    private ImageView menuAvatar;
    private TextView plusSign;
    private ImageButton btnchooseImage, imageButtonQuit;
    private Button btnConfirm;
    private Uri imageUri;

    private EditText eTname;
    private EditText eTprice;
    private EditText eTdescription;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        btnchooseImage = findViewById(R.id.btnChooseImage);
        plusSign = findViewById(R.id.plus_sign);
        menuAvatar = findViewById(R.id.menu_image);
        eTname = findViewById(R.id.editTextProductName);
        eTprice = findViewById(R.id.editTextProductPrice);
        eTdescription = findViewById(R.id.editTextProductDiscription);
        btnConfirm = findViewById(R.id.btnConfirm);
        imageButtonQuit = findViewById(R.id.imageButtonQuit);

        plusSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseImageClick(v);
            }
        });

        btnchooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChooseImageClick(v);
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = eTname.getText().toString();
                double price = Double.parseDouble(eTprice.getText().toString());
                String description = eTdescription.getText().toString();
                addMenu(name, price, description);

            }
        });
        imageButtonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void onChooseImageClick(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_IMAGE_GET);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getData() != null) {
                imageUri = data.getData();

                Glide.with(this).load(imageUri).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(menuAvatar);
                plusSign.setVisibility(View.GONE);
                btnchooseImage.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addMenu(String name, double price, String description) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        String restaurantId = currentUser.getUid();
        Product product = new Product(name, "", price, description, true);

        DatabaseReference restaurantsRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(restaurantId);
        DatabaseReference menuRef = restaurantsRef.child("menu");
        menuRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String newMenuId = menuRef.push().getKey();
                menuRef.child(newMenuId).setValue(product).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        uploadImageToStorage(restaurantId, name, newMenuId);
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void uploadImageToStorage(String restaurantId, String name, String menuId) {
        if (imageUri != null) {
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String fileExtension = mime.getExtensionFromMimeType(contentResolver.getType(imageUri));
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("restaurant_menu")
                    .child(restaurantId)
                    .child(name + "." + fileExtension);
            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        // Tải ảnh lên Storage thành công, lấy đường dẫn của ảnh
                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Lấy đường dẫn của ảnh từ Storage
                                String imagePath = uri.toString();

                                DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference()
                                        .child("restaurants")
                                        .child(restaurantId)
                                        .child("menu")
                                        .child(menuId);
                                menuRef.child("image").setValue(imagePath);
                            }
                        });
                    }
                }
            });
        }
    }
}
