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

    private Button buttonLogin;
    private ImageButton imagebuttonquit;

    private  String phone;
    FirebaseAuth mAuth;

    String vertificationId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edittextInputPhone = findViewById(R.id.textInputPhone);
        buttonLogin = findViewById(R.id.btnLogin);
        imagebuttonquit = findViewById(R.id.imageButtonQuit);
        mAuth = FirebaseAuth.getInstance();

        imagebuttonquit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        edittextInputPhone.addTextChangedListener(textWatcher);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone = edittextInputPhone.getText().toString().trim();
                sendverificationcode(phone);
            }
        });
    }
    private void sendverificationcode(String n){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84"+n)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // (optional) Activity for callback binding
                        // If no activity is passed, reCAPTCHA verification can not be used.
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks
    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {

            final String code = credential.getSmsCode();
            if (code != null){
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(LoginActivity.this,"Faild", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                @NonNull PhoneAuthProvider.ForceResendingToken token) {

            showOTPVerificationDialog(s);
        }
    };

    private void verifycode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(vertificationId,code);
        singinbyCredential(credential);
    }

    public void singinbyCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(LoginActivity.this,"Success",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
            }
        });
    }

    private void showOTPVerificationDialog(String verificationId){
        OTPVerificationDialog otpVerificationDialog = new OTPVerificationDialog(LoginActivity.this, verificationId, phone);
        otpVerificationDialog.setVerificationListener(new OTPVerificationDialog.OTPVerificationListener() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                singinbyCredential(credential);
            }
        });
        otpVerificationDialog.show();
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
                buttonLogin.setBackgroundResource(R.drawable.round_back_red_100);
            }
            else{
                buttonLogin.setBackgroundResource(R.drawable.round_back_brown_100);
            };
        }
    };
}