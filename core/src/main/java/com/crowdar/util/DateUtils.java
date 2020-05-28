package com.crowdar.util;

import com.crowdar.core.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static String getActualDateFormatted(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(getActualCalendar().getTime());
    }

    public static String getActualDateFormatted() {
        return getActualDateFormatted(Constants.getSimpleDatePattern());
    }

    public static Calendar getActualCalendar() {
        return Calendar.getInstance();
    }

    public static Date getActualDate() {
        return getActualCalendar().getTime();
    }
}
