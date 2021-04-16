package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.ops.R;
import com.ops.adapters.TimeRecyclerViewAdapter;
import com.ops.models.AvailableTime;
import com.ops.models.Reserve;
import com.ops.models.Restaurant;
import com.ops.models.request.RequestReserve;
import com.ops.models.response.BaseResponse;
import com.ops.models.response.ReserveResponse;
import com.ops.models.response.TimeAvailabilityResponse;
import com.ops.network.NetworkApi;
import com.ops.network.services.IReserveService;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReserveActivity extends AppCompatActivity implements View.OnClickListener, TimeRecyclerViewAdapter.OnTimeSelectionListener {

    final String TAG = ReserveActivity.class.getName();
    final int MIN_GUESTS_NUMBER = 2;
    final int MAX_GUESTS_NUMBER = 10;
    TextView reserveGuestTxtView, dateReserveTxtView, timeLabel;
    RelativeLayout downGuestLayout, upGuestLayout, datePickerLayout;
    EditText wishesEditText;
    Button reserveBtn;
    ProgressBar progressBar;
    RecyclerView timePickerRecyclerView;
    final Calendar calendar = Calendar.getInstance();
    int todayDate, restaurantId;
    String reserveDate, startTime, endTime, reserveTime;
    TimeRecyclerViewAdapter timeRecyclerViewAdapter;
    IReserveService service;
    private int endHour, startHour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reserve);
        service = NetworkApi.getInstance().getRetrofit().create(IReserveService.class);
        findViews();
        Intent intent = getIntent();
        try {
            extractData(intent);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        todayDate = UiUtils.initDateTextView(calendar, dateReserveTxtView);
        reserveGuestTxtView.setText(String.valueOf(MIN_GUESTS_NUMBER));
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.getDefault());
        reserveDate = sdf.format(calendar.getTime());
    }

    /**
     * find views from layout
     */
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
        progressBar = findViewById(R.id.progressBar);
        datePickerLayout.setOnClickListener(this);
        upGuestLayout.setOnClickListener(this);
        downGuestLayout.setOnClickListener(this);
        reserveBtn.setOnClickListener(this);
        initAvailableTimeRv();
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.tableReservation));
        }
    }

    /**
     * init adapter of time available
     */
    private void initAvailableTimeRv() {
        timeRecyclerViewAdapter = new TimeRecyclerViewAdapter(new ArrayList<>(), this);
        timePickerRecyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        timePickerRecyclerView.setAdapter(timeRecyclerViewAdapter);
        timeRecyclerViewAdapter.setTimeSelectionListener(this);
    }

    /**
     * Get data from intent
     *
     * @param intent
     * @throws ParseException
     */
    private void extractData(Intent intent) throws ParseException {
        if (intent.getExtras() != null) {

            restaurantId = intent.getIntExtra(Constant.RESTAURANT_ID, 0);
            startTime = intent.getStringExtra(Constant.RESTAURANT_START_TIME);
            endTime = intent.getStringExtra(Constant.RESTAURANT_END_TIME);
            String date = UiUtils.getTodayDate();
            getAvailableTimes(restaurantId, date);
        }
    }

    /**
     * create list of times between start working and end working
     *
     * @param start
     * @param end
     */
    private void buildAvailableTimes(String start, String end) {
        List<AvailableTime> list = new ArrayList<>();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            java.util.Date startTime = UiUtils.convertTimeFromString(start);
            java.util.Date endTime = UiUtils.convertTimeFromString(end);
            c1.setTime(startTime);
            c2.setTime(endTime);
            startHour = c1.get(Calendar.HOUR_OF_DAY);
            endHour = c2.get(Calendar.HOUR_OF_DAY) == 0 ? Constant.MIDNIGHT : c2.get(Calendar.HOUR_OF_DAY);
            reserveTime = generateTime(startHour);
            while (startHour < endHour) {
                list.add(new AvailableTime(generateTime(startHour), true));
                startHour++;
            }
            if (endHour == 24) {
                list.add(new AvailableTime("00:00", true));
            } else {
                list.add(new AvailableTime(String.format(Locale.getDefault(), "%d:00", endHour), true));
            }
            timeRecyclerViewAdapter.updateList(list);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    private String generateTime(int startHour) {
        return startHour < 10 ?
                String.format(Locale.getDefault(), "%d%d:00", 0, startHour)
                : String.format(Locale.getDefault(), "%d:00", startHour);

    }


    /**
     * Picker on guest component
     *
     * @param current
     * @param direction
     */
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

    @SuppressLint("NonConstantResourceId")
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
                Calendar minDate = Calendar.getInstance();
                minDate.add(Calendar.DATE, -1);
                DatePickerDialog datePickerDialog = new DatePickerDialog(ReserveActivity.this, dateSetListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(minDate.getTimeInMillis());
                datePickerDialog.show();
                break;
            case R.id.reserveBtn:
                int reserveDateNumber = UiUtils.getDateNumber(reserveDate);
                if ((reserveDateNumber != todayDate && calendar.get(Calendar.HOUR_OF_DAY) < endHour) ||
                        (reserveDateNumber == todayDate && calendar.get(Calendar.HOUR_OF_DAY) < endHour)) {
                    Reserve reserve = new Reserve(reserveDate, reserveTime, new Restaurant(restaurantId), Integer.parseInt(reserveGuestTxtView.getText().toString()), wishesEditText.getText().toString());
                    createReserve(reserve);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.reserveAlert, reserveTime), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    /**
     * Send reserve to api
     *
     * @param reserve
     */
    private void createReserve(Reserve reserve) {
        progressBar.setVisibility(View.VISIBLE);
        reserveBtn.setEnabled(false);
        service.createReserve(new RequestReserve(reserve)).enqueue(new Callback<BaseResponse<ReserveResponse>>() {
            @Override
            public void onResponse(@NotNull Call<BaseResponse<ReserveResponse>> call, @NotNull Response<BaseResponse<ReserveResponse>> response) {
                progressBar.setVisibility(View.INVISIBLE);
                reserveBtn.setEnabled(true);
                if (response.body() != null) {
                    if (response.body().isSuccess()) {
                        ReserveResponse reserveResponse = (ReserveResponse) response.body().getData();
                        startReserveSummary(reserveResponse.getReserve());
                    }
                } else {

                    if (response.errorBody() != null) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response.errorBody().string());
                            String message = object.getString("message");
                            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NotNull Call<BaseResponse<ReserveResponse>> call, @NotNull Throwable t) {
                Log.e(TAG, t.toString());
                progressBar.setVisibility(View.INVISIBLE);
                reserveBtn.setEnabled(true);
            }
        });
    }

    private void startReserveSummary(Reserve reserve) {
        Intent intent = new Intent(getApplicationContext(), SummaryReserveActivity.class);
        intent.putExtra(Constant.RESERVE, reserve);
        startActivity(intent);
        finish();

    }

    /**
     * @param restaurantId
     * @param date
     */
    private void getAvailableTimes(int restaurantId, String date) {

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
                Log.e(TAG, t.toString());
            }
        });


    }

    /**
     * Date picker listener
     */
    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String dayText = dayOfMonth == todayDate ? "Today," : "";
            String monthDisplayTxt = UiUtils.getMonthName(month + 1);
            String chooseDate = String.format(Locale.getDefault(), "%s %d %s", dayText, dayOfMonth, monthDisplayTxt);
            dateReserveTxtView.setText(chooseDate);
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.getDefault());
            calendar.set(year, month, dayOfMonth);
            reserveDate = sdf.format(calendar.getTime());
            getAvailableTimes(restaurantId, reserveDate);

        }
    };

    @Override
    public void onTimeSelection(String time) {
        reserveTime = String.format("%s:00", time);
    }
}