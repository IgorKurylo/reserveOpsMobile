package com.ops.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.ops.R;
import com.ops.adapters.RestaurantRecyclerViewAdapter;
import com.ops.models.Restaurant;
import com.ops.models.RestaurantArea;
import com.ops.ui.RestaurantDetails;
import com.ops.utils.Constant;

import java.util.ArrayList;
import java.util.List;


public class RestaurantFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, RestaurantRecyclerViewAdapter.OnChooseRestaurantListener {

    Button searchBtn;
    EditText searchEditText;
    ChipGroup restaurantAreaChipsGroup;
    RecyclerView restaurantRv;
    RestaurantRecyclerViewAdapter recyclerViewAdapter;
    List<RestaurantArea> filters;
    Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_restaurant, container, false);
        filters = new ArrayList<>();
        searchEditText = view.findViewById(R.id.searchEditTxt);
        searchBtn = view.findViewById(R.id.searchBtn);
        restaurantAreaChipsGroup = view.findViewById(R.id.filterResAreas);
        restaurantRv = view.findViewById(R.id.rvRestaurant);
        restaurantRv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerViewAdapter = new RestaurantRecyclerViewAdapter(mockRestaurant(), view.getContext());
        recyclerViewAdapter.setListener(this);
        restaurantRv.setAdapter(recyclerViewAdapter);
        RestaurantArea[] areas = RestaurantArea.values();
        initChipGroup(areas);

        return view;
    }

    private ArrayList<Restaurant> mockRestaurant() {
        ArrayList<Restaurant> list = new ArrayList<>();
        return list;
    }

    private void initChipGroup(RestaurantArea[] areas) {
        for (RestaurantArea restaurantArea : areas) {
            String area = restaurantArea.name().charAt(0) + "" + restaurantArea.name().toLowerCase().substring(1, restaurantArea.name().length());
            Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chips_layout, null, false);
            chip.setOnCheckedChangeListener(this);
            chip.setText(area);
            chip.setTag(restaurantArea);
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
                buttonView.setTextColor(ContextCompat.getColor(mContext, R.color.colorLight));
                filters.add(area);
            } else {
                buttonView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                filters.remove(area);
            }
        }
    }

    @Override
    public void OnChooseRestaurant(Restaurant restaurant) {
        Intent intent = new Intent(mContext, RestaurantDetails.class);
        intent.putExtra(Constant.RESTAURANT_KEY, restaurant);
        mContext.startActivity(intent);

    }
}