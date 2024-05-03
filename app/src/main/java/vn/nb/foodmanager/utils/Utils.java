package vn.nb.foodmanager.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;

public class Utils {
    public static String formatCurrency(long amount) {
        DecimalFormat formatter = new DecimalFormat("#,### đ");
        return formatter.format(amount);
    }

    public static String formatTime (long time){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.SHORT);
        return dateFormat.format(calendar.getTime());
    }
}
