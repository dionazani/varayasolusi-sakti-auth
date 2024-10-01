package org.varayasolusi.saktiauth.utils.commons;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtils {

    public static Timestamp getCurrentTimestamp() {

        Date date= new Date();
        long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        return ts;

    }

    public static long getCurrentTimestampInLong() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime();
    }

    public static Date getDateFromString(String strDate) throws ParseException {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(strDate);
        return date;
    }

    public static Timestamp getTimestampFromString(String str) {

        Timestamp timestamp = null;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date parsedDate = dateFormat.parse(str);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());

        } catch(Exception e) { //this generic but you can control another types of exception
            // look the origin of excption
        }

        return timestamp;
    }

    public static String getFromTimestamp(Timestamp ts) {

        String formattedDate = "";
        if (ts != null) {
            Date date = new Date();
            date.setTime(ts.getTime());
            formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        }

        return formattedDate;
    }

}