package com.ops.ui.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ops.R;
import com.ops.utils.Constant;
import com.ops.utils.UiUtils;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Locale;

public class HomeFragment extends Fragment implements View.OnClickListener {

    ImageView userAvatar;
    TextView dateTxt, reservationCountTxt, visitsCountTxt, lastRestaurantTxt;
    View view;
    final Calendar calendar = Calendar.getInstance();
    private int todayDate;
    private String filterDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        userAvatar = view.findViewById(R.id.userAvatar);
        dateTxt = view.findViewById(R.id.dateTxt);
        reservationCountTxt = view.findViewById(R.id.reservationCountTxt);
        visitsCountTxt = view.findViewById(R.id.visitCountTxt);
        lastRestaurantTxt = view.findViewById(R.id.lastRestaurantTxt);
        todayDate = UiUtils.initDateTextView(calendar, dateTxt);
        String imageUrl = UiUtils.randomAvatar(view.getContext());
        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.ic_user)
                .into(userAvatar);
        dateTxt.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.dateTxt) {
            new DatePickerDialog(view.getContext(), dateSetListener, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show();
        }
    }

    private final DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String dayText = dayOfMonth == todayDate ? "Today" : dayOfMonth < todayDate ? "Yesterday" : "";
            String monthDisplayTxt = UiUtils.getMonthName(month + 1);
            String chooseDate = String.format(Locale.getDefault(), "%s, %d %s", dayText, dayOfMonth, monthDisplayTxt);
            dateTxt.setText(chooseDate);
            SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.getDefault());
            calendar.set(year, month, dayOfMonth);
            filterDate = sdf.format(calendar.getTime());
        }
    };
}