package com.ops.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.ops.R;
import com.ops.models.response.BaseResponse;
import com.ops.models.response.StatisticsResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IStatisticsService;
import com.ops.utils.CacheManager;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    ImageView userAvatar;
    TextView reservationCountTxt, upComingReserve, dateUpcomingReserveTxt, timeUpcomingReserveTxt, restaurantNameUpcomingReserveTxt, guestNumberUpcomingReserve, lastRestaurantTxt;
    View view;
    CardView upComingReserveCard;
    final String TAG = HomeFragment.class.getName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        userAvatar = view.findViewById(R.id.userAvatar);
        reservationCountTxt = view.findViewById(R.id.reservationCountTxt);
        lastRestaurantTxt = view.findViewById(R.id.lastRestaurantTxt);
        dateUpcomingReserveTxt = view.findViewById(R.id.dateUpcomingReserveTxt);
        timeUpcomingReserveTxt = view.findViewById(R.id.timeUpcomingReserveTxt);
        restaurantNameUpcomingReserveTxt = view.findViewById(R.id.restaurantNameUpcomingReserveTxt);
        guestNumberUpcomingReserve = view.findViewById(R.id.guestNumberUpcomingReserve);
        upComingReserve = view.findViewById(R.id.upComingOrders);
        upComingReserveCard = view.findViewById(R.id.upComingOrderCard);
        TextView welcomeTxt = view.findViewById(R.id.welcomeText);
        loadUserDetails(welcomeTxt);
        loadStatistics();
        return view;
    }

    private void loadUserDetails(TextView welcomeTxt) {
        String imageUrl = CacheManager.getInstance().getString(Constant.AVATAR);
        Picasso.get().load(imageUrl)
                .error(R.drawable.ic_user)
                .into(userAvatar);
        welcomeTxt.setText(getString(R.string.welcomeText, CacheManager.getInstance().getString(Constant.FIRST_NAME)));
    }

    private void loadStatistics() {
        IStatisticsService service = NetworkApi.getInstance().getRetrofit().create(IStatisticsService.class);
        service.statistics().enqueue(new Callback<BaseResponse<StatisticsResponse>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<StatisticsResponse>> call, @NotNull Response<BaseResponse<StatisticsResponse>> response) {
                if (response.body() != null) {
                    if (response.body().isSuccess()) {
                        StatisticsResponse result = (StatisticsResponse) response.body().getData();
                        updateUI(result);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<StatisticsResponse>> call, @NotNull Throwable t) {
                Log.e(TAG, t.getMessage());

            }
        });
    }

    private void updateUI(StatisticsResponse result) {
        reservationCountTxt.setText(String.valueOf(result.getNumberOfReservation()));
        lastRestaurantTxt.setText(result.getRestaurant().getRestaurantName());
        // set upcoming reserve
        if (result.getReserve() != null) {
            upComingReserveCard.setVisibility(View.VISIBLE);
            upComingReserve.setTextColor(view.getContext().getColor(R.color.lightGrey));
            timeUpcomingReserveTxt.setText(result.getReserve().getTime());
            try {
                dateUpcomingReserveTxt.setText(UiUtils.formatDate(result.getReserve().getDate()));
            } catch (ParseException e) {
                Log.e(TAG, e.toString());
            }
            restaurantNameUpcomingReserveTxt.setText(result.getReserve().getRestaurant().getRestaurantName());
            guestNumberUpcomingReserve.setText(String.format(Locale.getDefault(), "%d %s", result.getReserve().getGuestsNumber(), view.getContext().getString(R.string.guestLabel)));
        } else {
            upComingReserveCard.setVisibility(View.GONE);
            upComingReserve.setText(getString(R.string.noUpComingReserve));
            upComingReserve.setTextColor(view.getContext().getColor(R.color.colorSecondPrimary));
        }
    }

}