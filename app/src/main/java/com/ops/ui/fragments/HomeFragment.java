package com.ops.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ops.R;
import com.ops.models.response.BaseResponse;
import com.ops.models.response.StatisticsResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IStatisticsService;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    ImageView userAvatar;
    TextView reservationCountTxt, dateUpcomingReserveTxt, timeUpcomingReserveTxt, restaurantNameUpcomingReserveTxt, guestNumberUpcomingReserve, lastRestaurantTxt;
    View view;
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
        String imageUrl = UiUtils.randomAvatar(view.getContext());
        userAvatar.setImageResource(0);
        Picasso.get().load(imageUrl)
                .into(userAvatar);
        loadStatistics();
        return view;
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
        timeUpcomingReserveTxt.setText(result.getReserve().getTime());
        try {
            dateUpcomingReserveTxt.setText(UiUtils.formatDate(result.getReserve().getDate()));
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
        }
        restaurantNameUpcomingReserveTxt.setText(result.getReserve().getRestaurant().getRestaurantName());
        guestNumberUpcomingReserve.setText(String.format(Locale.getDefault(), "%d %s", result.getReserve().getGuestsNumber(), view.getContext().getString(R.string.guestLabel)));
    }


//    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
//        @Override
//        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//            String dayText = dayOfMonth == todayDate ? "Today" : dayOfMonth < todayDate ? "Yesterday" : "";
//            String monthDisplayTxt = UiUtils.getMonthName(month + 1);
//            String chooseDate = String.format(Locale.getDefault(), "%s, %d %s", dayText, dayOfMonth, monthDisplayTxt);
//            dateTxt.setText(chooseDate);
//            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.getDefault());
//            calendar.set(year, month, dayOfMonth);
//            filterDate = sdf.format(calendar.getTime());
//        }
//    };
}