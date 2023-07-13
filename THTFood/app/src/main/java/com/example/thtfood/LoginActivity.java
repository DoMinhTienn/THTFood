package com.example.thtfood;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.text.method.PasswordTransformationMethod;
import android.widget.CompoundButton;

public class LoginActivity extends AppCompatActivity {

    private EditText edittextInputPhone;
    private Button buttonLogin;
    private ImageButton imagebuttonquit;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edittextInputPhone = findViewById(R.id.textInputPhone);
        buttonLogin = findViewById(R.id.buttonLogin);
        imagebuttonquit = findViewById(R.id.imageButtonQuit);

        imagebuttonquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edittextInputPhone.getText().toString().trim();
                String password = edittextInputPhone.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Vui lòng nhập Email/Số Điện Thoại", Toast.LENGTH_SHORT).show();
                } else {
                    // Thực hiện kiểm tra đăng nhập tại đây (ví dụ: gửi yêu cầu đăng nhập đến máy chủ)
                    // Nếu đăng nhập thành công, chuyển sang màn hình chính của ứng dụng
                    // Nếu đăng nhập không thành công, hiển thị thông báo lỗi
                }
            }
        });
    }
}