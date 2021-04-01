package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ops.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView registerTxtView;
    EditText phoneNumberEdTxt;
    Button logInBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumberEdTxt = findViewById(R.id.phoneNumberEditText);
        logInBtn = findViewById(R.id.loginBtn);
        registerTxtView = findViewById(R.id.registerTxtView);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerTxtView) {

        }
    }
}