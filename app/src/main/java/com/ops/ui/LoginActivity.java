package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.ops.R;
import com.ops.models.AccessToken;
import com.ops.models.AuthCredentials;
import com.ops.models.Role;
import com.ops.models.User;
import com.ops.models.response.BaseResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IAuthService;
import com.ops.utils.CacheManager;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = LoginActivity.class.getName();
    TextView registerTxtView;
    EditText phoneNumberEdTxt, firstNameEditTxt, lastNameEditTxt, phoneNumberEditTxtReg;
    ProgressBar progressBarReg, progressBarLogin;
    Button logInBtn, registerBtn;
    BottomSheetDialog dialog;
    IAuthService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        phoneNumberEdTxt = findViewById(R.id.phoneNumberEditText);
        logInBtn = findViewById(R.id.loginBtn);
        registerTxtView = findViewById(R.id.registerTxtView);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        registerTxtView.setOnClickListener(this);
        logInBtn.setOnClickListener(this);
        service = NetworkApi.getInstance().getRetrofit().create(IAuthService.class);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.loginLbl);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.registerTxtView) {
            registerOpenDialog();
        }
        if (v.getId() == R.id.registerBtn) {
            User user = new User(firstNameEditTxt.getText().toString(),
                    lastNameEditTxt.getText().toString(), phoneNumberEditTxtReg.getText().toString());
            progressBarReg.setVisibility(View.VISIBLE);
            dialog.setCanceledOnTouchOutside(false);
            registerBtn.setEnabled(false);
            register(user);
        }
        if (v.getId() == R.id.loginBtn) {
            String phone = phoneNumberEdTxt.getText().toString();
            progressBarLogin.setVisibility(View.VISIBLE);
            logInBtn.setEnabled(false);
            login(phone);
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
        progressBarReg = registerView.findViewById(R.id.progressBarReg);
        registerBtn = registerView.findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);

    }

    private void register(User user) {

        service.register(user).enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<User>> call, @NotNull Response<BaseResponse<User>> response) {
                progressBarReg.setVisibility(View.INVISIBLE);
                if (response.body() != null) {
                    if (response.body().isSuccess()) {
                        dialog.hide();
                        Toast.makeText(getApplicationContext(), getString(R.string.registerCompleted), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<User>> call, @NotNull Throwable t) {
                progressBarReg.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), getString(R.string.errorRegister), Toast.LENGTH_LONG).show();
                registerBtn.setEnabled(true);
                Log.e(TAG, t.toString());
            }
        });
    }

    private void login(String phone) {
        service.authentication(new AuthCredentials(phone)).enqueue(new Callback<BaseResponse<AccessToken>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<AccessToken>> call, @NotNull Response<BaseResponse<AccessToken>> response) {
                progressBarLogin.setVisibility(View.INVISIBLE);
                logInBtn.setEnabled(true);
                if (response.body() != null) {
                    if (response.body().isSuccess()) {
                        AccessToken token = (AccessToken) response.body().getData();
                        CacheManager.getInstance().setString(Constant.ACCESS_TOKEN, token.getAccessToken());
                        CacheManager.getInstance().setString(Constant.ROLE, token.getRole());
                        CacheManager.getInstance().setString(Constant.FIRST_NAME, token.getUser().getFirstName());
                        CacheManager.getInstance().setString(Constant.LAST_NAME, token.getUser().getLastName());
                        if (CacheManager.getInstance().getString(Constant.AVATAR).isEmpty()) {
                            CacheManager.getInstance().setString(Constant.AVATAR, UiUtils.randomAvatar(getApplicationContext()));
                        }
                        if (Role.valueOf(token.getRole()) == Role.SimpleUser) {
                            Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (Role.valueOf(token.getRole()) == Role.Admin) {
                            Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<AccessToken>> call, @NotNull Throwable t) {
                Log.e(TAG, t.toString());
                progressBarLogin.setVisibility(View.INVISIBLE);
                logInBtn.setEnabled(true);
                Toast.makeText(getApplicationContext(), getString(R.string.loginRegister), Toast.LENGTH_LONG).show();
            }
        });
    }
}