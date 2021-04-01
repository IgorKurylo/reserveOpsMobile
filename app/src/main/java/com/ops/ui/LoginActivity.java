package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ops.R;
import com.ops.models.User;
import com.ops.models.response.BaseResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IAuthService;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = LoginActivity.class.getName();
    TextView registerTxtView;
    EditText phoneNumberEdTxt, firstNameEditTxt, lastNameEditTxt, phoneNumberEditTxtReg, progressBar;
    Button logInBtn, registerBtn;
    BottomSheetDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumberEdTxt = findViewById(R.id.phoneNumberEditText);
        logInBtn = findViewById(R.id.loginBtn);
        registerTxtView = findViewById(R.id.registerTxtView);
        registerTxtView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerTxtView) {
            registerOpenDialog();
        }
        if (v.getId() == R.id.registerBtn) {
            User user = new User(firstNameEditTxt.getText().toString(),
                    lastNameEditTxt.getText().toString(), phoneNumberEditTxtReg.getText().toString());
            progressBar.setVisibility(View.VISIBLE);
            dialog.setCanceledOnTouchOutside(false);
            registerBtn.setEnabled(false);
            register(user);
        }
    }

    private void registerOpenDialog() {
        View registerView = getLayoutInflater().inflate(R.layout.registration_layout, null);
        initDialogViews(registerView);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(registerView);
        dialog.show();
    }

    private void initDialogViews(View registerView) {
        firstNameEditTxt = registerView.findViewById(R.id.firstName);
        lastNameEditTxt = registerView.findViewById(R.id.lastName);
        phoneNumberEditTxtReg = registerView.findViewById(R.id.phoneNumberEditTextReg);
        progressBar = registerView.findViewById(R.id.progressBar);
        registerBtn = registerView.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);

    }

    private void register(User user) {
        IAuthService service = NetworkApi.getInstance().getRetrofit().create(IAuthService.class);
        service.register(user).enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<User>> call, @NotNull Response<BaseResponse<User>> response) {
                if (response.body() != null) {
                    if (response.body().isSuccess()) {
                        progressBar.setVisibility(View.INVISIBLE);
                        dialog.hide();
                        Toast.makeText(getApplicationContext(), getString(R.string.registerCompleted), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<User>> call, @NotNull Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }
}