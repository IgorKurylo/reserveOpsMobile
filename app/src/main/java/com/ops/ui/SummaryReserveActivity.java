package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ops.R;
import com.ops.models.Reserve;
import com.ops.utils.CacheManager;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;

import java.text.ParseException;
import java.util.Locale;

public class SummaryReserveActivity extends AppCompatActivity {

    TextView reserveInfoFirst, reserveInfoSecond, userFullNameInfo;
    Button okBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_reserve);
        reserveInfoFirst = findViewById(R.id.reservationInfo1);
        reserveInfoSecond = findViewById(R.id.reservationInfo2);
        userFullNameInfo = findViewById(R.id.userFullNameInfo);
        okBtn = findViewById(R.id.okBtn);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constant.RESERVE)) {
            Reserve reserve = (Reserve) intent.getSerializableExtra(Constant.RESERVE);
            try {
                reserveInfoFirst.setText(String.format("%s,%s", UiUtils.formatDate(reserve.getDate()), reserve.getTime()));
                reserveInfoSecond.setText(String.format(Locale.getDefault(), "%d Guests '%s'",
                        reserve.getGuestsNumber(), reserve.getWishes() == null ? "" : reserve.getWishes()));
                userFullNameInfo.setText(String.format("%s %s",
                        CacheManager.getInstance().getString(Constant.FIRST_NAME), CacheManager.getInstance().getString(Constant.LAST_NAME)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(in);
                finish();
            }
        });


    }
}