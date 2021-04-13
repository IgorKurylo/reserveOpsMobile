package com.ops.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ops.R;
import com.ops.adapters.ReservationRecyclerViewAdapter;
import com.ops.models.Reserve;
import com.ops.models.ReserveStatus;
import com.ops.models.Role;
import com.ops.models.request.RequestReserve;
import com.ops.models.response.BaseResponse;
import com.ops.models.response.ReserveResponse;
import com.ops.models.response.ReservesResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IReserveService;
import com.ops.ui.ReserveActivity;
import com.ops.utils.CacheManager;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Reservation fragment for user and admin
 */
public class ReservationFragment extends Fragment implements View.OnClickListener, ReservationRecyclerViewAdapter.AdminActionListener {

    private ReservationRecyclerViewAdapter adapter;
    private LinearLayout calendarLayout;
    private RecyclerView reservationsRV;
    private TextView calendarTextView, noReservationTxt;
    final Calendar calendar = Calendar.getInstance();
    private int todayDate;
    private View view;
    final String TAG = ReservationFragment.class.getName();

    public static ReservationFragment newInstance() {
        return new ReservationFragment();
    }

    IReserveService service;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reservation, container, false);
        calendarLayout = view.findViewById(R.id.calendarLayout);
        TextView reservationLbl = view.findViewById(R.id.reservationLbl);
        reservationsRV = view.findViewById(R.id.reservationRv);
        calendarTextView = view.findViewById(R.id.dateTxt);
        todayDate = UiUtils.initDateTextView(calendar, calendarTextView);
        noReservationTxt = view.findViewById(R.id.noReservation);
        calendarLayout.setOnClickListener(this);
        initReservationAdapter();
        String date = UiUtils.getTodayDate();
        if (Role.valueOf(CacheManager.getInstance().getString(Constant.ROLE)) == Role.Admin) {
            reservationLbl.setText(view.getContext().getString(R.string.adminReservation));
            calendarLayout.setVisibility(View.VISIBLE);
        }
        service = NetworkApi.getInstance().getRetrofit().create(IReserveService.class);
        loadReservation(date);

        return view;
    }

    private void initReservationAdapter() {
        adapter = new ReservationRecyclerViewAdapter(new ArrayList<>(), view.getContext());
        reservationsRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        reservationsRV.setAdapter(adapter);
        adapter.setActionListener(this);
    }

    public DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String dayText = dayOfMonth == todayDate ? "Today," : "";
            String monthDisplayTxt = UiUtils.getMonthName(month + 1);
            String chooseDate = String.format(Locale.getDefault(), "%s %d %s", dayText, dayOfMonth, monthDisplayTxt);
            calendarTextView.setText(chooseDate);
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.getDefault());
            calendar.set(year, month, dayOfMonth);
            String reserveDate = sdf.format(calendar.getTime());
            loadReservation(reserveDate);
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.calendarLayout) {
            Calendar minDate = Calendar.getInstance();
            minDate.add(Calendar.DATE, -1);
            DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), dateSetListener, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
            datePickerDialog.show();
        }
    }

    private void loadReservation(String date) {
        service.reserves(date).enqueue(new Callback<BaseResponse<ReservesResponse>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<ReservesResponse>> call, @NotNull Response<BaseResponse<ReservesResponse>> response) {
                if (response.body() != null && response.body().isSuccess()) {
                    ReservesResponse reservesResponse = (ReservesResponse) response.body().getData();
                    if (reservesResponse.getReserveList().size() > 0) {
                        adapter.update(reservesResponse.getReserveList());
                    } else {
                        adapter.update(new ArrayList<>());
                        noReservationTxt.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<ReservesResponse>> call, @NotNull Throwable t) {
                Log.e(TAG, t.getMessage());

            }
        });
    }

    @Override
    public void OnApprove(int reservationId) {
        service.updateReservationStatus(new RequestReserve(new Reserve(reservationId, ReserveStatus.Approved.name()))).enqueue(new Callback<BaseResponse<ReserveResponse>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<ReserveResponse>> call, @NotNull Response<BaseResponse<ReserveResponse>> response) {

            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<ReserveResponse>> call, @NotNull Throwable t) {

            }
        });
    }

    @Override
    public void OnDecline(int reservationId) {
        service.updateReservationStatus(new RequestReserve(new Reserve(reservationId, ReserveStatus.Decline.name()))).enqueue(new Callback<BaseResponse<ReserveResponse>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<ReserveResponse>> call, @NotNull Response<BaseResponse<ReserveResponse>> response) {

            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<ReserveResponse>> call, @NotNull Throwable t) {

            }
        });
    }
}