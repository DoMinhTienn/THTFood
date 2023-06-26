package com.example.thtfood;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.method.PasswordTransformationMethod;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextPhone;
    private EditText editTextPassword;
    private Button buttonLogin;
    private ToggleButton toggleButtonPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextPhone = findViewById(R.id.editTextPhone);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        toggleButtonPassword = findViewById(R.id.toggleButtonPassword);


        toggleButtonPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Hiển thị mật khẩu
                    editTextPassword.setTransformationMethod(null);
                } else {
                    // Ẩn mật khẩu
                    editTextPassword.setTransformationMethod(new PasswordTransformationMethod());
                }
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextPhone.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

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