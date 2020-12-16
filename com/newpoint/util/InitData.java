package com.newpoint.util;

import com.ib.client.Bar;
import com.newpoint.marketdata.NPBar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class InitData {

    private static SimpleDateFormat yyyyMMddSdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat yMdsdf = new SimpleDateFormat("yyyyMMdd");
    private static SimpleDateFormat hmmssSdf = new SimpleDateFormat("HH:mm:ss");

    public static List<NPBar> oneMinutesHistoricalDataList = new ArrayList();
    public static List<NPBar> twoMinutesHistoricalDataList = new ArrayList();
    public static List<NPBar> thirtyMinutesHistoricalDataList = new ArrayList();
    public static List<NPBar> oneHourHistoricalDataList = new ArrayList();
    public static List<NPBar> fourHourHistoricalDataList = new ArrayList();
    public static List<NPBar> eightHourHistoricalDataList = new ArrayList();

    public static List<NPBar> oneDayHistoricalDataList = new ArrayList();
    public static List<NPBar> twoDayHistoricalDataList = new ArrayList();

    public static List<NPBar> oneWeekHistoricalDataList = new ArrayList();
    public static List<NPBar> oneMonthHistoricalDataList = new ArrayList();
    public static List<NPBar> oneYearHistoricalDataList = new ArrayList();
    public static List<NPBar> defaultHistoricalDataList = new ArrayList();

    public static void savaHistoricalData(int tickerId, Bar bar){
//        Map<String, Object> map = new HashMap<>();
//        map.put("reqId",reqId);
//        map.put("time",bar.time());
//        map.put("open",bar.open());
//        map.put("high",bar.high());
//        map.put("low",bar.low());
//        map.put("close",bar.close());
//        map.put("volume",bar.volume());
//        map.put("count",bar.count());
//        map.put("wap",bar.wap());
//        barlist.add(map);

        try {
            NPBar npBar1 = new NPBar(bar.high(),bar.low(),bar.open(),bar.close(),bar.volume(),yMdsdf.parse(bar.time()));
            oneYearHistoricalDataList.add(npBar1);
            System.out.println("oneYearHistoricalDataList：" + oneYearHistoricalDataList.size());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * 判断是否是有效工作日
     * @param startTime
     * @return
     * @throws ParseException
     */
    public static boolean isWorkingDay(String startTime) throws ParseException {
        Date startTempDate = yyyyMMddSdf.parse("20080701 10:00:00");
        Date endTempDate = yyyyMMddSdf.parse("20080701 16:00:01");

        Date date = yyyyMMddSdf.parse(startTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 非工作日或>=10点或<=16点
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || hmmssSdf.format(date).compareTo(hmmssSdf.format(startTempDate)) < 0 || hmmssSdf.format(date).compareTo(hmmssSdf.format(endTempDate)) > 0) {
            return false;
        } else {
            return true;
        }
    }
}
