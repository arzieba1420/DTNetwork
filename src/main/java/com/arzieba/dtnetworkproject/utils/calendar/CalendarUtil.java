package com.arzieba.dtnetworkproject.utils.calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
    //Calendar to String
    public static String cal2string(Calendar calendar) {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int realMonth = month+1;
        String stringMonth = realMonth<10? "0"+realMonth:""+realMonth;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String stringDay = day<10? "0"+day:""+day;

        return stringDay+"-"+stringMonth+"-"+year;
    }

    public static Calendar string2cal(String dateToParse) {
        String stringDate = dateToParse;
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");

        Date date;
        {
            try {
                date = formatter.parse(stringDate);
                Calendar calender = Calendar.getInstance();
                calender.setTime(date);
                return calender;
            } catch (ParseException e) {
                e.getMessage();
            }
            return null;
        }
    }
}
