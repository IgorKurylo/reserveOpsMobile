package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ops.R;
import com.ops.adapters.TimeRecyclerViewAdapter;
import com.ops.models.AvailableTime;
import com.ops.models.response.BaseResponse;
import com.ops.models.response.TimeAvailabilityResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IOrderService;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveActivity extends AppCompatActivity implements View.OnClickListener {

    final int MIN_GUESTS_NUMBER = 2;
    final int MAX_GUESTS_NUMBER = 10;
    TextView reserveGuestTxtView, dateReserveTxtView;
    RelativeLayout downGuestLayout, upGuestLayout, datePickerLayout;
    TextView timeLabel;
    EditText wishesEditText;
    Button reserveBtn;
    RecyclerView timePickerRecyclerView;
    final Calendar calendar = Calendar.getInstance();
    int todayDate;
    String reserveDate, startTime, endTime;
    TimeRecyclerViewAdapter timeRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        findViews();
        Intent intent = getIntent();
        try {
            extractData(intent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        todayDate = UiUtils.initDateTextView(calendar, dateReserveTxtView);
        reserveGuestTxtView.setText(String.valueOf(MIN_GUESTS_NUMBER));
    }

    private void findViews() {
        downGuestLayout = findViewById(R.id.downGuestLayout);
        upGuestLayout = findViewById(R.id.upGuestLayout);
        reserveGuestTxtView = findViewById(R.id.reserveGuestTxtView);
        datePickerLayout = findViewById(R.id.datePickerLayout);
        dateReserveTxtView = findViewById(R.id.reserveDateTxt);
        wishesEditText = findViewById(R.id.wishesEditText);
        reserveBtn = findViewById(R.id.reserveBtn);
        timePickerRecyclerView = findViewById(R.id.timeRv);
        timeLabel = findViewById(R.id.labelTime);
        datePickerLayout.setOnClickListener(this);
        upGuestLayout.setOnClickListener(this);
        downGuestLayout.setOnClickListener(this);
        initAvailableTimeRv();
    }

    private void extractData(Intent intent) throws ParseException {
        if (intent.getExtras() != null) {
            Bundle bd = intent.getExtras();
            int restaurantId = bd.getInt(Constant.RESTAURANT_ID);
            startTime = bd.getString(Constant.RESTAURANT_START_TIME);
            endTime = bd.getString(Constant.RESTAURANT_END_TIME);
            String date = UiUtils.getTodayDate();
            getAvailableTimes(restaurantId, date);
        }
    }

    private void getAvailableTimes(int restaurantId, String date) {
        IOrderService service = NetworkApi.getInstance().getRetrofit().create(IOrderService.class);
        service.availableTimes(restaurantId, date).enqueue(new Callback<BaseResponse<TimeAvailabilityResponse>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<TimeAvailabilityResponse>> call, @NotNull Response<BaseResponse<TimeAvailabilityResponse>> response) {
                if (response.body() != null) {
                    if (response.body().isSuccess()) {
                        TimeAvailabilityResponse availabilityResponse = (TimeAvailabilityResponse) response.body().getData();
                        if (availabilityResponse.getAvailableTimeList().size() > 0) {
                            timeRecyclerViewAdapter.updateList(availabilityResponse.getAvailableTimeList());
                        } else {
                            buildAvailableTimes(startTime, endTime);
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<TimeAvailabilityResponse>> call, Throwable t) {
                Log.e(ReserveActivity.class.getName(), t.toString());
            }
        });


    }

    private void buildAvailableTimes(String start, String end) {
        List<AvailableTime> list = new ArrayList<>();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            java.util.Date startTime = UiUtils.convertTimeFromString(start);
            java.util.Date endTime = UiUtils.convertTimeFromString(end);
            c1.setTime(startTime);
            c2.setTime(endTime);
            int startHour = c1.get(Calendar.HOUR_OF_DAY);
            int endHour = c2.get(Calendar.HOUR_OF_DAY) == 0 ? 24 : c2.get(Calendar.HOUR_OF_DAY);
            while (startHour <= endHour) {
                list.add(new AvailableTime(startHour < 10 ?
                        String.format(Locale.getDefault(), "%d%d:00", 0, startHour)
                        : String.format(Locale.getDefault(), "%d:00", startHour)
                        , true));
                startHour++;
            }
            timeRecyclerViewAdapter.updateList(list);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    private void initAvailableTimeRv() {
        timeRecyclerViewAdapter = new TimeRecyclerViewAdapter(new ArrayList<>(), this);
        timePickerRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        timePickerRecyclerView.setAdapter(timeRecyclerViewAdapter);
    }

    private void guestsPicker(int current, int direction) {
        if (direction > 0) {
            if (current < MAX_GUESTS_NUMBER) {
                reserveGuestTxtView.setText(String.valueOf(++current));
            }
        } else {
            if (current > MIN_GUESTS_NUMBER) {
                reserveGuestTxtView.setText(String.valueOf(--current));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upGuestLayout:
                guestsPicker(Integer.parseInt(reserveGuestTxtView.getText().toString()), 1);
                break;
            case R.id.downGuestLayout:
                guestsPicker(Integer.parseInt(reserveGuestTxtView.getText().toString()), -1);
                break;
            case R.id.datePickerLayout:
                new DatePickerDialog(ReserveActivity.this, dateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
        }
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String dayText = dayOfMonth == todayDate ? "Today," : dayOfMonth < todayDate ? "Yesterday," : "";
            String monthDisplayTxt = UiUtils.getMonthName(month + 1);
            String chooseDate = String.format(Locale.getDefault(), "%s %d %s", dayText, dayOfMonth, monthDisplayTxt);
            dateReserveTxtView.setText(chooseDate);
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.getDefault());
            calendar.set(year, month, dayOfMonth);
            reserveDate = sdf.format(calendar.getTime());

        }
    };
}