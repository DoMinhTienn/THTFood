package com.example.thtfood.Controller;

import static com.example.thtfood.Controller.CreateRestaurantAcittivity.REQUEST_IMAGE_GET;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.thtfood.Model.Address;
import com.example.thtfood.Model.Restaurant;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfoRestaurantActivity extends AppCompatActivity {


    public static final int REQUEST_IMAGE_GET = 1;
    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = firebaseAuth.getCurrentUser();
    String uid = currentUser.getUid();

    DatabaseReference restaurantsRef = FirebaseDatabase.getInstance().getReference().child("restaurants").child(uid);
    private ImageButton imageButtonQuit, btnChooseImage;
    private ImageView restaurantImage;
    private Button btnConfirm;
    private ToggleButton toggleButtonEditData;
    private Map<String, List<String>> districtMap = new HashMap<>();
    private Map<String, List<String>> wardMap = new HashMap<>();
    private List<String> citiesList = new ArrayList<>();
    AutoCompleteTextView dropdownCity, dropdownDistrict, dropdownWard;
    ArrayAdapter<String> adapterCity, adapterDistrict, adapterWard;
    private StorageReference storageRef;
    private Uri imageUri;
    private EditText etNumber, etStreet, etNameRestaurant;
    private String ward, district, city;
    private AutoCompleteTextView autoCompleteWard, autoCompleteDistrict, autoCompleteCity;
    String avatarPath;

    @SuppressLint({"MissingInflatedId", "CutPasteId"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_restaurant);

        imageButtonQuit = findViewById(R.id.imageButtonQuit);
        restaurantImage = findViewById(R.id.restaurantImage);

        etNameRestaurant = findViewById(R.id.textInputNameRestaurant);
        etNumber = findViewById(R.id.textInputNumber);
        etStreet = findViewById(R.id.textInputStreet);

        autoCompleteWard = findViewById(R.id.dropdown_ward);
        autoCompleteDistrict = findViewById(R.id.dropdown_district);
        autoCompleteCity = findViewById(R.id.dropdown_city);

        btnChooseImage = findViewById(R.id.btnChooseImage);
        ;

        storageRef = FirebaseStorage.getInstance().getReference();
        btnConfirm = findViewById(R.id.btnConfirm);
        toggleButtonEditData = findViewById(R.id.toggleButtonEditData);

        dropdownCity = findViewById(R.id.dropdown_city);
        dropdownDistrict = findViewById(R.id.dropdown_district);
        dropdownWard = findViewById(R.id.dropdown_ward);

        adapterCity = new ArrayAdapter<>(InfoRestaurantActivity.this, R.layout.list, citiesList);
        dropdownCity.setAdapter(adapterCity);

        adapterDistrict = new ArrayAdapter<>(InfoRestaurantActivity.this, R.layout.list);
        dropdownDistrict.setAdapter(adapterDistrict);

        adapterWard = new ArrayAdapter<>(InfoRestaurantActivity.this, R.layout.list);
        dropdownWard.setAdapter(adapterWard);
        disableEdit();
        loadData();
        dropdownCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = parent.getItemAtPosition(position).toString();
                updateDistricts(selectedCity);
            }
        });

        dropdownDistrict.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedDistrict = parent.getItemAtPosition(position).toString();
                updateWards(selectedDistrict);
            }
        });

        btnChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi phương thức onChooseImageClick khi nút được nhấn
                onChooseImageClick(v);
            }
        });

        imageButtonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveRestaurantInfo();
            }
        });
        toggleButtonEditData.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    enableEdit();
                } else {
                    disableEdit();
                }
            }
        });
        // Lấy dữ liệu từ Firebase và hiển thị lên các trường
        DatabaseReference restaurantRef = FirebaseDatabase.getInstance().getReference()
                .child("restaurants")
                .child(uid); // uid là id của nhà hàng cần lấy thông tin

        restaurantRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Kiểm tra nếu nhà hàng có trong database
                if (snapshot.exists()) {
//                    //Lấy dữ liệu từ snapshot và đổ vào các trường
                    Restaurant restaurant = snapshot.getValue(Restaurant.class);
                    etNameRestaurant.setText(restaurant.getName());
                    etNumber.setText(restaurant.getAddress().getNumber());
                    etStreet.setText(restaurant.getAddress().getStreet());

                    // Đặt giá trị cho AutoCompleteTextView
                    autoCompleteCity.setHint(restaurant.getAddress().getCity());
                    autoCompleteDistrict.setHint(restaurant.getAddress().getDistrict());
                    autoCompleteWard.setHint(restaurant.getAddress().getWard());
                    avatarPath = restaurant.getAvatar_path();
                    Glide.with(InfoRestaurantActivity.this).load(restaurant.getAvatar_path()).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(restaurantImage);

                    // Hiển thị nút "Chọn ảnh"
                    btnChooseImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi truy vấn dữ liệu
                Toast.makeText(InfoRestaurantActivity.this, "Lỗi truy vấn dữ liệu từ Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {
        try {
            InputStream inputStream = getAssets().open("country.json");
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            String jsonString = new String(buffer, StandardCharsets.UTF_8);

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray citiesArray = jsonObject.getJSONArray("cities");

            for (int i = 0; i < citiesArray.length(); i++) {
                JSONObject cityObject = citiesArray.getJSONObject(i);
                String cityName = cityObject.getString("name");
                citiesList.add(cityName);

                JSONArray districtsArray = cityObject.getJSONArray("districts");
                List<String> districtsList = new ArrayList<>();
                for (int j = 0; j < districtsArray.length(); j++) {
                    JSONObject districtObject = districtsArray.getJSONObject(j);
                    String districtName = districtObject.getString("name");
                    districtsList.add(districtName);

                    JSONArray wardsArray = districtObject.getJSONArray("wards");
                    List<String> wardsList = new ArrayList<>();
                    for (int k = 0; k < wardsArray.length(); k++) {
                        String wardName = wardsArray.getString(k);
                        wardsList.add(wardName);
                    }
                    wardMap.put(districtName, wardsList);
                }
                districtMap.put(cityName, districtsList);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateDistricts(String selectedCity) {
        // Filter districts based on the selected city
        List<String> filteredDistricts = districtMap.get(selectedCity);
        if (filteredDistricts == null) {
            filteredDistricts = new ArrayList<>();
        }
        adapterDistrict.clear();
        adapterDistrict.addAll(filteredDistricts);
    }

    private void updateWards(String selectedDistrict) {
        List<String> filteredWards = wardMap.get(selectedDistrict);
        if (filteredWards == null) {
            filteredWards = new ArrayList<>();
        }
        adapterWard.clear();
        adapterWard.addAll(filteredWards);
    }

    private void onChooseImageClick(View view) {
        // Mở hộp thoại để chọn hình ảnh từ thư viện
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
                // Load hình ảnh đã chọn lên ImageView
                Glide.with(this).load(imageUri).apply(RequestOptions.bitmapTransform(new RoundedCorners(20))).into(restaurantImage);

                // Hiển thị nút "Lưu" thay vì nút "Chọn ảnh"
                btnChooseImage.setVisibility(View.VISIBLE);
            }
        }
    }

    public void uploadImageToStorage(final String restaurantId) {
        if (imageUri != null) {
            ContentResolver contentResolver = getContentResolver();
            MimeTypeMap mime = MimeTypeMap.getSingleton();
            String fileExtension = mime.getExtensionFromMimeType(contentResolver.getType(imageUri));

            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("restaurant_avatars")
                    .child(restaurantId + "." + fileExtension);

            storageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                avatarPath = uri.toString();
                                onSaveRestaurantInfo();
                            }
                        });
                    }
                }
            });
        }
    }

    private void enableEdit() {
        etNameRestaurant.setEnabled(true);
        etNumber.setEnabled(true);
        etStreet.setEnabled(true);
        autoCompleteWard.setEnabled(true);
        autoCompleteDistrict.setEnabled(true);
        autoCompleteCity.setEnabled(true);
        btnChooseImage.setEnabled(true);
        btnConfirm.setEnabled(true);
        btnConfirm.setBackgroundResource(R.drawable.button_background);
    }

    private void disableEdit() {
        etNameRestaurant.setEnabled(false);
        etNumber.setEnabled(false);
        etStreet.setEnabled(false);
        autoCompleteWard.setEnabled(false);
        autoCompleteDistrict.setEnabled(false);
        autoCompleteCity.setEnabled(false);
        btnChooseImage.setEnabled(false);
        btnConfirm.setEnabled(false);
        btnConfirm.setBackgroundResource(R.drawable.button_background_opacity);
    }

    public void onSaveRestaurantInfo() {
        uploadImageToStorage(uid);
        String name = etNameRestaurant.getText().toString();
        String number = etNumber.getText().toString();
        String street = etStreet.getText().toString();
        ward = autoCompleteWard.getText().toString();
        district = autoCompleteDistrict.getText().toString();
        city = autoCompleteCity.getText().toString();
        Address address = new Address(number, street, ward, district, city);

        restaurantsRef.child("avatar_path").setValue(avatarPath);
        restaurantsRef.child("name").setValue(name);
        if (!city.isEmpty())
        {
            restaurantsRef.child("address").setValue(address);
        }
        finish();
        Toast.makeText(getApplicationContext(), "Cập nhật thành công", Toast.LENGTH_SHORT).show();
    }
}
