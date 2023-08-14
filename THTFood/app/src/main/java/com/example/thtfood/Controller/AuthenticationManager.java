package com.example.thtfood.Controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.thtfood.Model.User;
import com.example.thtfood.Model.UserManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.concurrent.TimeUnit;

public class AuthenticationManager {
    private FirebaseAuth mAuth;
    private Activity activity;
    private User user_register;
    private String phone;
    String vertificationId;
    private OTPVerificationDialog otpVerificationDialog;

    public AuthenticationManager(Activity activity) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
    }

    public AuthenticationManager(Activity activity, User user) {
        this.activity = activity;
        mAuth = FirebaseAuth.getInstance();
        this.user_register = user;
    }

    public void sendverificationcode(String n) {

        phone = n;
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+84" + n)       // Phone number to verify
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
            if (code != null) {
                verifycode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(activity, "Faild", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCodeSent(@NonNull String s,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {
            vertificationId = s;
            showOTPVerificationDialog(s, phone);
        }
    };

    private void verifycode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(vertificationId, code);
        singinbyCredential(credential);
    }

    public void singinbyCredential(PhoneAuthCredential credential) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = task.getResult().getUser();
                            String userId = user.getUid();

                            DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
                            DatabaseReference newUserRef = usersRef.child(userId); // Tạo một nút con mới

                            newUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        String name = snapshot.child("name").getValue(String.class);
                                        String email = snapshot.child("email").getValue(String.class);
                                        String role = snapshot.child("role").getValue(String.class);
                                        String avatar = snapshot.child("avatar").getValue(String.class);

                                        User user = new User(name, email, role, avatar);
                                        UserManager.getInstance().setUser(user);
                                        activity.startActivity(new Intent(activity, HomeActivity.class));
                                    } else {
                                        saveUser(newUserRef, userId);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(activity, "Database error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        otpVerificationDialog.handleAuthenticationFailed();
                        Log.d("Loi", "LOI DANG NHAP");

                    }
                });
    }

    private void saveUser(DatabaseReference userRef, String userId) {
        String name = user_register.getName();
        String email = user_register.getEmail();
        String role = user_register.getRole();

        StorageReference avatarRef = FirebaseStorage.getInstance().getReference().child("avatars").child("default.png");
        // Lấy URL của hình ảnh đã lưu trong Firebase Storage
        avatarRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String avatarUrl = uri.toString();

            // Lưu URL của hình ảnh vào Realtime Database
            userRef.child("avatar").setValue(avatarUrl)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Lưu các thông tin khác vào Realtime Database
                            userRef.child("name").setValue(name);
                            userRef.child("email").setValue(email);
                            userRef.child("role").setValue(role);
                            Toast.makeText(activity, "Authentication success", Toast.LENGTH_SHORT).show();
                            activity.startActivity(new Intent(activity, HomeActivity.class));
                        } else {
                            Toast.makeText(activity, "Failed to save user information", Toast.LENGTH_SHORT).show();
                        }
                    });
            User user = new User(name, email, role, avatarUrl);
            UserManager.getInstance().setUser(user);
        }).addOnFailureListener(exception -> {
            // Xử lý khi không lấy được URL của hình ảnh trong Firebase Storage
            Toast.makeText(activity, "Failed to get avatar URL", Toast.LENGTH_SHORT).show();
        });
    }


    private void showOTPVerificationDialog(String verificationId, String phone) {
        otpVerificationDialog = new OTPVerificationDialog(activity, verificationId, phone);
        otpVerificationDialog.setVerificationListener(new OTPVerificationDialog.OTPVerificationListener() {
            @Override
            public void onVerificationCompleted(String code) {
                verifycode(code);
            }

        });
        otpVerificationDialog.show();
    }
}
