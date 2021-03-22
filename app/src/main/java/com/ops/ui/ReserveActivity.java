package com.ops.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ops.R;
import com.ops.adapters.TimeRecyclerViewAdapter;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
    String reserveDate;
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
    }

    private void extractData(Intent intent) throws ParseException {
        if (intent.getExtras() != null) {
            Bundle bd = intent.getExtras();
            String start = bd.getString(Constant.RESTAURANT_START_TIME);
            String end = bd.getString(Constant.RESTAURANT_END_TIME);
            Date startTime = UiUtils.convertTimeFromString(start);
            Date endTime = UiUtils.convertTimeFromString(end);
            initTimePickerRecyclerView(startTime, endTime);
        }
    }

    private void initTimePickerRecyclerView(Date start, Date end) {
        List<String> timeList = new ArrayList<>();
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(start);
        c2.setTime(end);
        int startHour = c1.get(Calendar.HOUR_OF_DAY);
        int endHour = c2.get(Calendar.HOUR_OF_DAY);
        while (startHour <= endHour) {
            timeList.add(startHour < 10 ?
                    String.format(Locale.getDefault(), "%d%d:00", 0, startHour)
                    : String.format(Locale.getDefault(), "%d:00", startHour));
            startHour++;
        }
        timeRecyclerViewAdapter = new TimeRecyclerViewAdapter(timeList, this);
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