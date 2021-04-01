package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.ops.R;

public class LoginActivity extends AppCompatActivity {

    EditText phoneNumberEdTxt;
    Button logInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumberEdTxt = findViewById(R.id.phoneNumberEditText);
        logInBtn = findViewById(R.id.loginBtn);

    }
}