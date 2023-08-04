package com.example.thtfood.Controller;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import com.example.thtfood.R;

public class CustomDialog extends Dialog {

    private TextView dialogMessage;
    private Button btnOk;
    private Button btnCancel;
    private CustomDialogListener listener;

    public CustomDialog(Context context) {
        super(context);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(getContext().getResources().getColor(android.R.color.white)));
        setContentView(R.layout.custom_dialog_layout);

        dialogMessage = findViewById(R.id.dialog_message);
        btnOk = findViewById(R.id.btn_ok);
        btnCancel = findViewById(R.id.btn_cancel);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onOkButtonClicked();
                }
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
    public void setDialogMessage(String message) {
        dialogMessage.setText(message);
    }

    public void setCustomDialogListener(CustomDialogListener listener) {
        this.listener = listener;
    }

    public interface CustomDialogListener {
        void onOkButtonClicked();
    }
}
