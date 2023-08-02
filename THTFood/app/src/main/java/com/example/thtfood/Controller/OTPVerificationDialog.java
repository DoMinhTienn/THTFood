package com.example.thtfood.Controller;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.thtfood.R;

public class OTPVerificationDialog extends Dialog {
    private EditText otp1, otp2, otp3, otp4, otp5, otp6;
    private TextView btnresend;
    private Button btnverify;

    private int resendTime = 60;
    private boolean resendEnabled = false;

    private  int selectedOTPposition = 0;

   private final String phone;
    private final String verificationId;

    public interface OTPVerificationListener {
        void onVerificationCompleted(String code);
    }
    private OTPVerificationListener verificationListener;

    public void setVerificationListener(OTPVerificationListener listener) {
        this.verificationListener = listener;
    }
    public OTPVerificationDialog(@NonNull Context context, String verificationId, String phone) {
        super(context);
        this.verificationId = verificationId;
        this.phone = phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(android.R.color.transparent)));
        setContentView(R.layout.otp_dialog_layout);

        otp1 = findViewById(R.id.OTP1);
        otp2 = findViewById(R.id.OTP2);
        otp3 = findViewById(R.id.OTP3);
        otp4 = findViewById(R.id.OTP4);
        otp5 = findViewById(R.id.OTP5);
        otp6 = findViewById(R.id.OTP6);

        btnresend = findViewById(R.id.btnResend);
        btnverify = findViewById(R.id.btnVerify);
        final TextView phone = findViewById(R.id.phone);
        otp1.addTextChangedListener(textWatcher);
        otp2.addTextChangedListener(textWatcher);
        otp3.addTextChangedListener(textWatcher);
        otp4.addTextChangedListener(textWatcher);
        otp5.addTextChangedListener(textWatcher);
        otp6.addTextChangedListener(textWatcher);

        showKeyboard(otp1);

        startCountDownTimer();

        phone.setText("+84" + this.phone);

        btnresend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resendEnabled) {
                    startCountDownTimer();
                }
            }
        });

        btnverify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String getOtp = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString() + otp5.getText().toString() + otp6.getText().toString();

                if (getOtp.length() == 6){
                    if (verificationListener != null) {
                        verificationListener.onVerificationCompleted(getOtp);
                    }
                };
            }
        });
    }

    private final TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (editable.length() > 0 ){
                switch (selectedOTPposition){
                    case 0:
                        selectedOTPposition = 1;
                        showKeyboard(otp2);
                        break;
                    case 1:
                        selectedOTPposition = 2;
                        showKeyboard(otp3);
                        break;
                    case 2:
                        selectedOTPposition = 3;
                        showKeyboard(otp4);
                        break;
                    case 3:
                        selectedOTPposition = 4;
                        showKeyboard(otp5);
                        break;
                    case 4:
                        selectedOTPposition = 5;
                        showKeyboard(otp6);
                        break;
                    default:
                        btnverify.setBackgroundResource(R.drawable.round_back_red_10);
                }

            };
        }
    };

    private void showKeyboard(EditText otp){
            otp.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(otp, InputMethodManager.SHOW_IMPLICIT);
    };

    private void startCountDownTimer(){
        resendEnabled = false;
        btnresend.setTextColor(Color.parseColor("#99000000"));

        new CountDownTimer(resendTime * 1000, 1000){
            @Override
            public void onTick(long l) {
                btnresend.setText("Resend code (" + (l/ 1000) + ")" );
            }

            @Override
            public void onFinish() {
                resendEnabled = true;
                btnresend.setText("Resend code" );
                btnresend.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
            }
        }.start();
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public boolean onKeyUp(int keyCode, @NonNull KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_DEL){
            switch (selectedOTPposition){
                case 5:
                    selectedOTPposition = 4;
                    showKeyboard(otp5);
                    break;
                case 4:
                    selectedOTPposition = 3;
                    showKeyboard(otp4);
                    break;
                case 3:
                    selectedOTPposition = 2;
                    showKeyboard(otp3);
                    break;
                case 2:
                    selectedOTPposition = 1;
                    showKeyboard(otp2);
                    break;
                case 1:
                    selectedOTPposition = 0;
                    showKeyboard(otp1);
                    break;
            }
            btnverify.setBackgroundResource(R.drawable.round_back_brown_10);
            return true;
        } else{
            return super.onKeyUp(keyCode, event);
        }
    }

    public void handleAuthenticationFailed(){
        otp1.setText("");
        otp2.setText("");
        otp3.setText("");
        otp4.setText("");
        otp5.setText("");
        otp6.setText("");
        selectedOTPposition = 0;
        showKeyboard(otp1);
        Toast.makeText(getContext(),"Lỗi xác thực", Toast.LENGTH_SHORT).show();

    }
}
