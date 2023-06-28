package com.example.thtfood;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.text.method.PasswordTransformationMethod;
import android.widget.CompoundButton;

public class LoginActivity extends AppCompatActivity {

    private EditText edittextInputPhone;
    private EditText edittextInputPassword;
    private Button buttonLogin;
    private CheckBox checkBoxShowPassword;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edittextInputPhone = findViewById(R.id.textInputPhone);
        edittextInputPassword = findViewById(R.id.textInputPassword);
        edittextInputPassword.setTransformationMethod(new PasswordTransformationMethod()); //auto ẩn mật khẩu
        buttonLogin = findViewById(R.id.buttonLogin);
        checkBoxShowPassword = findViewById(R.id.checkBoxShowPassword);


        checkBoxShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Hiển thị mật khẩu
                    edittextInputPassword.setTransformationMethod(null);
                } else {
                    // Ẩn mật khẩu
                    edittextInputPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edittextInputPhone.getText().toString().trim();
                String password = edittextInputPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter phone and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Thực hiện kiểm tra đăng nhập tại đây (ví dụ: gửi yêu cầu đăng nhập đến máy chủ)
                    // Nếu đăng nhập thành công, chuyển sang màn hình chính của ứng dụng
                    // Nếu đăng nhập không thành công, hiển thị thông báo lỗi
                }
            }
        });
    }
}