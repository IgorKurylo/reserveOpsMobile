package com.ops.utils;

import android.content.Context;
import android.net.Uri;
import android.widget.TextView;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.logging.SimpleFormatter;

public class UiUtils {

    public static String randomAvatar(Context context) {
        int max = 10, min = 1;
        int randomNumber = new Random().nextInt((max - min) + 1) + min;
        String path = String.format("//assets/users/%s.png", "" + randomNumber);
        Uri imageUri = Uri.fromFile(new File(String.format(path, randomNumber)));
        return imageUri.getPath();
    }

    public static String getMonthName(int month) {
        Month m = Month.of(month);
        return m.getDisplayName(TextStyle.SHORT, Locale.getDefault());
    }

    public static int initDateTextView(Calendar calendar, TextView dateTextView) {
        int todayDate = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        String monthName = UiUtils.getMonthName(month);
        String currentDate = String.format(Locale.getDefault(), "%d, %s %s",
                todayDate, monthName, "Today");
        dateTextView.setText(currentDate);
        return todayDate;
    }

    public static Date convertTimeFromString(String time) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(Constant.TIME_FORMAT, Locale.getDefault());
        return formatter.parse(time);
    }

    public static String getTodayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat(Constant.DATE_FORMAT, Locale.getDefault());
        Calendar today = Calendar.getInstance();
        return sdf.format(today.getTime());
    }
}
