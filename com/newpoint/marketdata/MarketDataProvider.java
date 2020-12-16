package com.newpoint.marketdata;

import com.ib.client.Contract;
import com.ib.client.EClientSocket;
import com.newpoint.instrument.InstrumentManager;
import com.newpoint.instrument.Security;
import com.newpoint.instrument.SecurityType;
import com.newpoint.util.DateUtil;
import com.newpoint.util.InitData;
import com.newpoint.workstation.NPTradeSession;
import com.newpoint.workstation.NPTradeStation;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.*;
import java.time.Period;

// with getInstance() method
public class MarketDataProvider
{
    public static String WHAT_TO_SHOW ="TRADES";    //type of data for head timestamp - "BID", "ASK", "TRADES", etc
    public static int USE_REGULAR_TRADING_HOUR = 1; //use regular trading hours only, 1 for yes or 0 for no
    public static int RETURN_DATE_FORMAT = 1;       //set to 1 to obtain the bars' time as yyyyMMdd HH:mm:ss, set to 2 to obtain it like system time format in seconds
    public static boolean KEEP_UP_TO_DATE = false;  //set to True to received continuous updates on most recent bar data. If True, and endDateTime cannot be specified.
    public static int REQ_ID = 10000;

    private Map<Integer, MarketDataKey> marketDataReqMap;
    private boolean isMarketDataInitialized = false;

    public static List<Integer> reqList = new ArrayList();

    private static SimpleDateFormat yyyyMMddHHmmssSdf = new SimpleDateFormat("yyyyMMdd HH:mm:ss");

    public static int reqId = 20000;

    // static variable single_instance of type OrderManager
    private static final MarketDataProvider marketDataProvider = new MarketDataProvider();

    // private constructor restricted to this class itself
    private MarketDataProvider()
    {
        this.marketDataReqMap = new HashMap<Integer, MarketDataKey>();
    }

    // static method to create instance of OrderManager class
    public static MarketDataProvider getInstance()
    {
        return marketDataProvider;
    }


    //to be implemented
    public void initialize() {
        //this.session = NPTradeStation.getSession();
        //this.marketDataReqMap = new HashMap<Integer, MarketDataKey>();
       // this.cacheManager = MarketDataCacheManager.getInstance();

        /*initializeMarketData();

        while (!isMarketDataInitialized) {
            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("*****Market Data Initialized*****");
        cacheManager.saveDataToFile();
        cacheManager.retrieveDataFromFile(); */
    }

    public void reqFinished(int reqId) {
        Iterator<Integer> iterator = marketDataReqMap.keySet().iterator();
        while (iterator.hasNext()){
            Integer currentReqId = iterator.next();
            if (currentReqId.equals(reqId)) {
                iterator.remove();
            }
        }
        if (marketDataReqMap.isEmpty()) {
            isMarketDataInitialized = true;
        }
    }

    public void initializeMarketData() {
        NPTradeSession session = NPTradeStation.getSession();
        Security security = InstrumentManager.getInstance().createSecurity(SecurityType.STK,"QQQ");
        Contract contract = InstrumentManager.getInstance().createIBSecurity(security);
        MarketDataKey dataKey = new MarketDataKey(security,BarSize.ONE_MINUTE);
        //prepare for the data cache.
        MarketDataCacheManager cacheManager = MarketDataCacheManager.getInstance();
        cacheManager.createCacheForKey(dataKey);
        String barSize = BarSize.ONE_MINUTE;
        Date date = null;
        String duration = getDurationByBarSize(barSize);
        List<Date> days = DateUtil.getWeekDays(2019);
        for (Date day: days) {
            String dayString = DateUtil.formatDate(day);
            marketDataReqMap.put(REQ_ID,dataKey);
            session.getClient().reqHistoricalData(REQ_ID, contract, dayString, duration, barSize, WHAT_TO_SHOW, USE_REGULAR_TRADING_HOUR, RETURN_DATE_FORMAT, KEEP_UP_TO_DATE, null);
            REQ_ID++;
            if (REQ_ID>10002) break;
        }

    }

    public MarketDataKey getMarketDataKey(int reqid) {
        return marketDataReqMap.get(reqid);
    }

    /**
     * 获取历史数据
     * @param security
     * @param barSize
     * @param startDate
     * @param endDate
     * @return
     */
    public static MarketDataBlock getMarketDataBlock(Security security, String barSize, Date startDate, Date endDate) {
        System.out.println("security = " + security + ", barSize = " + barSize + ", startDate = " + startDate + ", endDate = " + endDate);
        reqId = reqId + 1;
        String duration = getDurationByBarSize(barSize);

        int yearsNum = Period.between(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getYears();
        int monthsNum = Period.between(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getMonths();
        int daysNum = Period.between(startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate(), endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getDays();
        if (yearsNum < 0 || monthsNum != 0 || daysNum != 0) {
            System.err.println("只能输入年的倍数，并且输入的开始时间不能大约结束时间！");
            return null;
        } else if (yearsNum == 0) {
             sendHistoricalDataRequests(NPTradeStation.getSession().getClient(), reqId, security.symbol, security.secType.STK.toString(), Security.USD, Security.SMART, yyyyMMddHHmmssSdf.format(endDate), duration, barSize, DataType.TRADES.toString());
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);
            for (int i = 0; i < yearsNum; i++) {
                cal.add(Calendar.YEAR, 1);
                String startDateStr = yyyyMMddHHmmssSdf.format(cal.getTime());
                 sendHistoricalDataRequests(NPTradeStation.getSession().getClient(), reqId, security.symbol, security.secType.STK.toString(), Security.USD, Security.SMART, startDateStr, duration, barSize, DataType.TRADES.toString());
            }
        }

        MarketDataBlock marketDataBlock = null;
        boolean isReadComplete = false;
        while (!isReadComplete) {
            for (int i = 0; i < reqList.size() ; i++) {
                if (reqList.get(i) == reqId) {
                    isReadComplete = true;
                    marketDataBlock = new MarketDataBlock();
                    marketDataBlock.setBars(InitData.oneYearHistoricalDataList);
                } else {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return marketDataBlock;
    }

    /**
     * 根据barSize返回最优的Duration
     * @param barSize
     * @return
     */
    public static String getDurationByBarSize(String barSize) {
        String duration = "";
        if (BarSize.ONE_SECOND.equals(barSize)) {
            duration = duration + "1800 " + Duration.S;
        } else if (BarSize.FIVE_SECONDS.equals(barSize)) {
            duration = duration +  "3600 " + Duration.S;
        } else if (BarSize.TEN_SECONDS.equals(barSize)) {
            duration = duration +  "14400 " + Duration.S;
        } else if (BarSize.THIRTY_SECONDS.equals(barSize)) {
            duration = duration +  "28800 " + Duration.S;
        } else if (BarSize.ONE_MINUTE.equals(barSize)) {
            duration = duration +  "1 " + Duration.D;
        } else if (BarSize.TWO_MINUTES.equals(barSize)) {
            duration = duration +  "2 " + Duration.D;
        } else if (BarSize.THREE_MINUTES.equals(barSize)) {
            duration = duration +  "1 " + Duration.W;
        } else if (BarSize.THIRTY_MINUTE.equals(barSize)) {
            duration = duration +  "1 " + Duration.M;
        } else if (BarSize.ONE_DAY.equals(barSize)) {
            duration = duration +  "1 " + Duration.Y;
        } else {
            System.err.println("barSize 输入错误");
        }
        return duration;
    }

    /**
     * Requesting historical data
     * @param client
     * @param symbol
     * @param secType
     * @param currencyUSD
     * @param exchangeSMART
     * @param endTime
     * @param duration
     * @param barSize
     * @param whatToShow
     */
    public static void sendHistoricalDataRequests(EClientSocket client, int tickerId, String symbol, String secType, String currencyUSD, String exchangeSMART, String endTime, String duration, String barSize, String whatToShow) {
        Contract contract = new Contract();
        contract.symbol(symbol);
        contract.secType(secType);
        contract.currency(currencyUSD);
        contract.exchange(exchangeSMART);
        try {
            // Requesting historical data
            client.reqHistoricalData(tickerId, contract, endTime, duration, barSize, whatToShow, 1, 1, false, null);
            Thread.sleep(5000);
            /*** Canceling historical data requests ***/
            client.cancelHistoricalData(tickerId);
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
