package com.ops.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.ops.R;
import com.ops.adapters.RestaurantRecyclerViewAdapter;
import com.ops.models.Area;
import com.ops.models.Restaurant;
import com.ops.models.RestaurantArea;
import com.ops.models.request.RequestRestaurant;
import com.ops.models.response.BaseResponse;
import com.ops.models.response.RestaurantResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IRestaurantService;
import com.ops.ui.RestaurantDetails;
import com.ops.utils.Constant;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RestaurantFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, RestaurantRecyclerViewAdapter.OnChooseRestaurantListener {

    Button searchBtn;
    EditText searchEditText;
    ChipGroup restaurantAreaChipsGroup;
    RecyclerView restaurantRv;
    RestaurantRecyclerViewAdapter recyclerViewAdapter;
    List<Area> filters;
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
        recyclerViewAdapter = new RestaurantRecyclerViewAdapter(new ArrayList<>(), view.getContext());
        recyclerViewAdapter.setListener(this);
        restaurantRv.setAdapter(recyclerViewAdapter);
        RestaurantArea[] areas = RestaurantArea.values();
        initChipGroup(areas);
        getRestaurants();
        return view;
    }

    private void getRestaurants() {
        IRestaurantService service = NetworkApi.getInstance().getRetrofit().create(IRestaurantService.class);
        service.restaurantsByArea(new RequestRestaurant(this.filters)).enqueue(new Callback<BaseResponse<RestaurantResponse>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<RestaurantResponse>> call, @NotNull Response<BaseResponse<RestaurantResponse>> response) {
                if (response.body() != null) {
                    RestaurantResponse restaurantResponse = (RestaurantResponse) response.body().getData();
                    if (restaurantResponse.getRestaurantList() != null) {
                        recyclerViewAdapter.updateRestaurants(restaurantResponse.getRestaurantList());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<RestaurantResponse>> call, @NonNull Throwable t) {
                Log.e(RestaurantFragment.class.getName(), t.toString());
            }
        });

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
                filters.add(new Area(area.name()));
            } else {
                buttonView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
                filters.remove(new Area(area.name()));
            }
            getRestaurants();
        }


    }

    @Override
    public void OnChooseRestaurant(Restaurant restaurant) {
        Intent intent = new Intent(mContext, RestaurantDetails.class);
        intent.putExtra(Constant.RESTAURANT_KEY, restaurant);
        mContext.startActivity(intent);

    }
}