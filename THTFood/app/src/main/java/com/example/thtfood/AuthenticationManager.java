package com.example.thtfood;

import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.play.integrity.internal.a;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthencationManager {
    private FirebaseAuth mAuth;
    private Activity activity;
    private String phone;
    String vertificationId;

    public  AuthencationManager(Activity activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
    }
    private void sendverificationcode(String n){
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84"+n)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // (optional) Activity for callback binding
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
            Toast.makeText(activity,"Faild", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {

            showOTPVerificationDialog(s, phone);
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
                    Toast.makeText(activity,"Success",Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(activity, HomeActivity.class));
                }
            }
        });
    }

    private void showOTPVerificationDialog(String verificationId, String phone){
        OTPVerificationDialog otpVerificationDialog = new OTPVerificationDialog(activity, verificationId, phone);
        otpVerificationDialog.setVerificationListener(new OTPVerificationDialog.OTPVerificationListener() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                singinbyCredential(credential);
            }
        });
        otpVerificationDialog.show();
    }
}
