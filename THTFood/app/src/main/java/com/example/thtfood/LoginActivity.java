package com.example.thtfood;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.text.method.PasswordTransformationMethod;
import android.widget.CompoundButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

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