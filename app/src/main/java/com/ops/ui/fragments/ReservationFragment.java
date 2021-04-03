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
import com.ops.models.response.BaseResponse;
import com.ops.models.response.ReservesResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IReserveService;
import com.ops.ui.ReserveActivity;
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


public class ReservationFragment extends Fragment implements View.OnClickListener {

    private ReservationRecyclerViewAdapter adapter;
    private LinearLayout calendarLayout;
    private RecyclerView reservationsRV;
    private TextView calendarTextView;
    final Calendar calendar = Calendar.getInstance();
    private int todayDate;
    private View view;
    final String TAG = ReservationFragment.class.getName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_reservation, container, false);
        calendarLayout = view.findViewById(R.id.calendarLayout);
        reservationsRV = view.findViewById(R.id.reservationRv);
        calendarTextView = view.findViewById(R.id.dateTxt);
        todayDate = UiUtils.initDateTextView(calendar, calendarTextView);
        calendarLayout.setOnClickListener(this);
        initReservationAdapter();
        String date = UiUtils.getTodayDate();
        loadReservation(date);
        return view;
    }

    private void initReservationAdapter() {
        adapter = new ReservationRecyclerViewAdapter(new ArrayList<>(), view.getContext());
        reservationsRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        reservationsRV.setAdapter(adapter);
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
        IReserveService service = NetworkApi.getInstance().getRetrofit().create(IReserveService.class);
        service.reserves(date).enqueue(new Callback<BaseResponse<ReservesResponse>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<ReservesResponse>> call, @NotNull Response<BaseResponse<ReservesResponse>> response) {
                if (response.body() != null && response.body().isSuccess()) {
                    ReservesResponse reservesResponse = (ReservesResponse) response.body().getData();
                    adapter.update(reservesResponse.getReserveList());
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<ReservesResponse>> call, @NotNull Throwable t) {
                Log.e(TAG, t.getMessage());

            }
        });
    }
}