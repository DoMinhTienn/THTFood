package com.example.thtfood.Controller;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.thtfood.R;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText edittextInputPhone;

    private Button btnLogin;
    private ImageButton imagebuttonquit;

    private  String phone;
    FirebaseAuth mAuth;

    private AuthenticationManager authenticationManager;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edittextInputPhone = findViewById(R.id.textInputPhone);
        btnLogin = findViewById(R.id.btnLogin);
        imagebuttonquit = findViewById(R.id.imageButtonQuit);
        mAuth = FirebaseAuth.getInstance();
        authenticationManager = new AuthenticationManager(this);

        imagebuttonquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
        edittextInputPhone.addTextChangedListener(textWatcher);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = edittextInputPhone.getText().toString().trim();
                authenticationManager.sendverificationcode(phone);

            }
        });
    }

    private final TextWatcher  textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0){
                btnLogin.setBackgroundResource(R.drawable.round_back_red_100);
            }
            else{
                btnLogin.setBackgroundResource(R.drawable.round_back_brown_100);
            }
        }
    };
}