package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.ops.R;
import com.ops.adapters.RestaurantRecyclerViewAdapter;
import com.ops.models.RestaurantArea;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Button searchBtn;
    EditText searchEditText;
    ChipGroup restaurantAreaChipsGroup;
    RecyclerView restaurantRv;
    RestaurantRecyclerViewAdapter recyclerViewAdapter;
    List<RestaurantArea> filters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        filters = new ArrayList<>();
        searchEditText = findViewById(R.id.searchEditTxt);
        searchBtn = findViewById(R.id.searchBtn);
        restaurantAreaChipsGroup = findViewById(R.id.filterResAreas);
        restaurantRv = findViewById(R.id.rvRestaurant);
        RestaurantArea[] areas = RestaurantArea.values();
        initChipGroup(areas);

    }

    private void initChipGroup(RestaurantArea[] areas) {
        for (int i = 0; i < areas.length; i++) {
            String area = areas[i].name().charAt(0) + "" + areas[i].name().toLowerCase().substring(1, areas[i].name().length());
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chips_layout, null, false);
            chip.setOnCheckedChangeListener(this);
            chip.setText(area);
            chip.setTag(areas[i]);
            chip.setCheckable(true);
            chip.setChecked(false);
            restaurantAreaChipsGroup.addView(chip);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        RestaurantArea area = (RestaurantArea) buttonView.getTag();
        if (area != null) {
            if (isChecked) {
                buttonView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorLight));
                filters.add(area);
            } else {
                buttonView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.black));
                filters.remove(area);
            }
        }
    }
}