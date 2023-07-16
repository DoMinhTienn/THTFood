package com.example.thtfood;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InfoAccountActivity extends AppCompatActivity {
    String[] roleUser = {"Khách", "Người bán hàng"};

    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterRole;

    private Button btnconfirm;
    private EditText etName, etEmail;
    private String name, email;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_account);

        etName = findViewById(R.id.textInputName);
        etEmail = findViewById(R.id.textInputEmail);

        autoCompleteTextView = findViewById(R.id.dropdown_role);
        adapterRole = new ArrayAdapter<>(this, R.layout.list_role, roleUser);
        autoCompleteTextView.setAdapter(adapterRole);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View v, int i, long l) {
                role = String.valueOf(i);
            }
        });

        btnconfirm = findViewById(R.id.btnConfirm);
        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = etName.getText().toString();
                email = etEmail.getText().toString();
                Intent intent = new Intent(InfoAccountActivity.this, RegisterActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("role", role);
                startActivity(intent);
            }
        });
    }
}