package com.myApp.vilrolf.discgolfputting.Utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Viljar on 21-Feb-17.
 */

public class DateUtil {

    public static Calendar stringToCal(String datestring) {
        // 2017-02-19 17:14:25
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(dateFormat.parse(datestring));
            return cal;
        } catch (ParseException e) {
            return null;
        }
    }
}
