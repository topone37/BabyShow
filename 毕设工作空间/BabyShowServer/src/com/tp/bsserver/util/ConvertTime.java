package com.tp.bsserver.util;

/**
 * Created by Administrator on 2015/12/6.
 */
public class ConvertTime {
    public static String convert(String datetime) {
        if (datetime.length() < 14) {
            return datetime;
        } else {
            String t[] = datetime.split(" "); //  2015-11-23
            t[0] = t[0].substring(5);
//            String time = t[1].substring(0, 5); //  12:34
//            return t[0] + "  " + time;
            return t[0];
        }


    }

    public static String formatTime(String date) {
        if (date.length() < 4) {
            return date;
        } else {
            String a[] = date.split("-");
            String y = a[0];
            String m = a[1];
            String d = a[2];

            return m + "." + d;
        }


    }
}
