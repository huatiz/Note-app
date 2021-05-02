package com.example.NOTE;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

public class changeDayTime {
    private int year, month, day, hour, min;
    private Calendar c = Calendar.getInstance();

    public void getDateTime() {
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        hour = c.get(Calendar.HOUR_OF_DAY);
        min = c.get(Calendar.MINUTE);
    }

    public void setDateTime(String day, String time) {
        String[] Day = day.split("-");
        String[] Time = time.split(":");

        c.set(Calendar.YEAR, Integer.valueOf(Day[0]));
        c.set(Calendar.MONTH, Integer.valueOf(Day[1])-1);
        c.set(Calendar.DAY_OF_MONTH, Integer.valueOf(Day[2]));
        c.set(Calendar.HOUR_OF_DAY, Integer.valueOf(Time[0]));
        c.set(Calendar.MINUTE, Integer.valueOf(Time[1]));
    }

    public void changeDay(TextView dayTextView, Context context) {
        new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int sYear, int sMonth, int sDay) {
                year = sYear;
                month = sMonth;
                day = sDay;

                dayTextView.setText(sYear + "-" + String.format("%02d", sMonth+1) + "-" + String.format("%02d", sDay));
            }
        }, year, month, day).show();
    }

    public void changeTime(TextView timeTextView, Context context) {
        new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int sHour, int sMin) {
                hour = sHour;
                min = sMin;

                timeTextView.setText(String.format("%02d", sHour) + ":" + String.format("%02d", sMin));
            }
        }, hour, min, true).show();
    }
}
