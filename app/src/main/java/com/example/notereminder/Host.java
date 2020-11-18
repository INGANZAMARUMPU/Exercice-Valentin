package com.example.notereminder;

import android.widget.DatePicker;

public class Host {
    public static final String URL = "https://daviddurand.info/D228/reminder";

    public static String getDate(DatePicker dp) {
        return dp.getDayOfMonth()+ "/"+dp.getMonth()+"/"+dp.getYear();
    }
}
