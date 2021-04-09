package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

import com.ops.R;
import com.ops.utils.CacheManager;
import com.ops.utils.Constant;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        TextView welcomeText = findViewById(R.id.welcomeText);
        ImageView userAvatar = findViewById(R.id.userAvatar);
        TextView reservationCountTxt=findViewById(R.id.reservationCountTxt);
        TextView pendingReservation=findViewById(R.id.pendingReservation);
        String imageUrl = CacheManager.getInstance().getString(Constant.AVATAR);
        Picasso.get().load(imageUrl)
                .error(R.drawable.ic_user)
                .into(userAvatar);
        welcomeText.setText(getString(R.string.welcomeText, CacheManager.getInstance().getString(Constant.FIRST_NAME) +
                " " + CacheManager.getInstance().getString(Constant.LAST_NAME)));


    }
}