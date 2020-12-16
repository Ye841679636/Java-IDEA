package com.newpoint.util;

import com.ib.client.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    public static SimpleDateFormat MARKET_DATA_FORMAT = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    private static SimpleDateFormat yMdsdf = new SimpleDateFormat("yyyyMMdd");

    public static List<Date> getWeekDays(int year) {
        List<Date> list = new ArrayList<Date>();
        Calendar cal = Calendar.getInstance();
        cal.set(year, 0, 1);
        for (int day = 1; day <= cal.getActualMaximum(Calendar.DAY_OF_YEAR); day++) {
            cal.set(Calendar.DAY_OF_YEAR, day);
            int weekDay = cal.get(Calendar.DAY_OF_WEEK);
            if (weekDay != Calendar.SATURDAY && weekDay != Calendar.SUNDAY) {
                list.add(cal.getTime());
            }
        }
        return list;
    }

    public static List<Date> getDates(int year,int month){
        List<Date> dates = new ArrayList<Date>();

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH,  month - 1);
        cal.set(Calendar.DATE, 1);

        while(cal.get(Calendar.YEAR) == year &&
                cal.get(Calendar.MONTH) < month){
            int day = cal.get(Calendar.DAY_OF_WEEK);

            if(!(day == Calendar.SUNDAY || day == Calendar.SATURDAY)){
                dates.add((Date)cal.getTime().clone());
            }
            cal.add(Calendar.DATE, 1);
        }
        return dates;
    }

    public static Date parse(String dateString) {
        try {
           return MARKET_DATA_FORMAT.parse(dateString);
        }
        catch (ParseException e) {
            throw new RuntimeException("DateUtil:parse: unable to convert date for " + dateString);
        }
    }

    public static Date stringToDate(String dateString) {
        if (!dateString.isEmpty()) {
            Date date = null;
            try {
                date = MARKET_DATA_FORMAT.parse(dateString);
            } catch (ParseException e) {
                try {
                    date = yMdsdf.parse(dateString);
                } catch (ParseException ex) {
                    throw new RuntimeException("DateUtil:parse: unable to convert date for " + dateString);
                }
            }
            return date;
        } else {
            throw new RuntimeException("dateString cannot be empty " + dateString);
        }
    }

    public static String UnixMillisecondsToString(long milliseconds){
        String ibTickTimeFormat = "yyyyMMdd-HH:mm:ss zzz";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(ibTickTimeFormat);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String UnixSecondsToString(long seconds){
        return UnixMillisecondsToString(seconds * 1000);
    }

    public static Date UnixSecondsToDate(long seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(seconds*1000);
        return calendar.getTime();
    }

    public static String formatDate(Date date) {
        return MARKET_DATA_FORMAT.format(date);
    }

    /**
     * 获取每年每月最后一天
     * @param year
     * @return
     */
    public static List<Date> getLastDayByMonth(int year) {
        List<Date> list = new ArrayList<>();
        for (int month = 0; month < 12; month++) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, 0,1,23,59,59);
            cal.add(Calendar.MONTH, month);
            cal.add(Calendar.MONTH, 1);
            cal.add(Calendar.DATE, -1);
            list.add(cal.getTime());
        }
        return list;
    }
}
