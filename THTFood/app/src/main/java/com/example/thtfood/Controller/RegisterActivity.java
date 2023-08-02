package com.example.thtfood.Controller;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.thtfood.Model.User;
import com.example.thtfood.R;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    private User user;
    private FirebaseAuth mAuth;
    private String name;
    private String email;
    private String role;
    private EditText edittextInputPhone;
    private Button buttonLogin;
    private AuthenticationManager authenticationManager;
    private ImageButton imagebuttonquit;
    private  String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getIntent().getExtras() != null) {
            // Lấy các giá trị từ Intent
            name = getIntent().getStringExtra("name");
            email = getIntent().getStringExtra("email");
            role = getIntent().getStringExtra("role");

            user = new User(name, email, role, null);
            TextView textView = findViewById(R.id.textView2);

            textView.setText("Xin chào " + name);
            edittextInputPhone = findViewById(R.id.textInputPhone);
            buttonLogin = findViewById(R.id.btnLogin);
            imagebuttonquit = findViewById(R.id.imageButtonQuit);
            authenticationManager = new AuthenticationManager(this, user);

            imagebuttonquit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            edittextInputPhone.addTextChangedListener(textWatcher);
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phone = edittextInputPhone.getText().toString().trim();
                    authenticationManager.sendverificationcode(phone);

                }
            });

        }
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0){
                buttonLogin.setBackgroundResource(R.drawable.round_back_red_10);
            }
            else{
                buttonLogin.setBackgroundResource(R.drawable.round_back_brown_10);
            }
        }
    };
}