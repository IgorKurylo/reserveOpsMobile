package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ops.R;
import com.ops.models.Restaurant;
import com.ops.utils.Constant;

public class RestaurantDetails extends AppCompatActivity implements View.OnClickListener {

    Restaurant restaurant;
    TextView restaurantNameTxt, callRestaurantTxt, visitWebTxt, showOnMapTxt;
    Button reserveTableBtn;
    ImageView restaurantImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_details);
        initViews();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(Constant.RESTAURANT_KEY)) {
            restaurant = (Restaurant) intent.getSerializableExtra(Constant.RESTAURANT_KEY);
            restaurantNameTxt.setText(restaurant.getRestaurantName());
            Glide.with(this)
                    .load(restaurant.getImageUrl())
                    .into(restaurantImage);
        }

    }

    private void initViews() {
        restaurantNameTxt = findViewById(R.id.restNameTxt);
        callRestaurantTxt = findViewById(R.id.callRestTxtView);
        visitWebTxt = findViewById(R.id.visitWebTxtView);
        showOnMapTxt = findViewById(R.id.showOnMapTxtView);
        reserveTableBtn = findViewById(R.id.reserveTableBtn);
        restaurantImage = findViewById(R.id.restaurantImageView);
        reserveTableBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.reserveTableBtn) {
            Intent intent = new Intent(this, ReserveActivity.class);
            Bundle bd = new Bundle();
            bd.putInt(Constant.RESTAURANT_ID, restaurant.getId());
            bd.putString(Constant.RESTAURANT_START_TIME, restaurant.getWorkTimeStart());
            bd.putString(Constant.RESTAURANT_END_TIME, restaurant.getWorkTimeEnd());
            intent.putExtras(bd);
            startActivity(intent);
            finish();

        }
    }
}