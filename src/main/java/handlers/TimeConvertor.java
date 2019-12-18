package handlers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeConvertor {
    private static DateFormat dateTime = new SimpleDateFormat("dd MMM yyyy HH:mm");
    private static DateFormat timeOnly = new SimpleDateFormat("HH:mm");
    private static DateFormat dayOnly  = new SimpleDateFormat("dd");
    private static Date today = new Date();
    private static Date yesterday = new Date(System.currentTimeMillis()-24*60*60*1000);

    public static String ascTime(Long timestamp){
        Date tsDate = new Date(timestamp);
        return dateTime.format(tsDate);
    }

    public static String compareDate(String timestamp){
        Long _timestamp = Long.valueOf(timestamp);
        if (Integer.valueOf(dayOnly.format(today)).equals(Integer.valueOf(dayOnly.format(_timestamp)))){
            return "Today at " + timeOnly.format(_timestamp);
        }else if(Integer.valueOf(dayOnly.format(yesterday)).equals(Integer.valueOf(dayOnly.format(_timestamp)))){
            return "Yesterday at " + timeOnly.format(_timestamp);
        }else{
            return ascTime(_timestamp);
        }

    }
}
