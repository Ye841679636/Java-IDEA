package com.newpoint.workstation;

import com.newpoint.instrument.Security;
import com.newpoint.instrument.Stock;
import com.newpoint.marketdata.BarSize;
import com.newpoint.marketdata.MarketDataBlock;
import com.newpoint.marketdata.MarketDataProvider;
import com.newpoint.order.NPOrder;
import com.newpoint.order.ComboOrder;
import com.newpoint.order.Transaction;
import com.newpoint.strategy.BollingerStrategy;
import com.newpoint.strategy.RTStrategyRunner;
import com.newpoint.strategy.StrategyResult;
import com.newpoint.strategy.StrategyRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TestExample {
    public static ComboOrder getTSLAComboOrder() {
        Security TSLA = Stock.TSLA;
        return new ComboOrder(TSLA, NPOrder.BUY, 5, 500, 515, 485);
    }

    private static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

    //Run Bollinger Band strategy
    public static void runBollingerStrategy() {
        Security security = Stock.QQQ;
        BollingerStrategy strategy = new BollingerStrategy(2, 20, true);
        StrategyRunner runner = new StrategyRunner(strategy);
        MarketDataBlock marketData = null;

        Calendar cal = Calendar.getInstance();
        Date today = cal.getTime();
        // get next year
        cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        Date oneYearAgo = cal.getTime();

        String startDate = "20190801";
        String endDate = "20200801";

        try {
            marketData = MarketDataProvider.getInstance().getMarketDataBlock(security, BarSize.ONE_DAY, yyyyMMdd.parse(startDate), yyyyMMdd.parse(endDate));
        } catch (ParseException e) {

        }
        //marketData = MarketDataProvider.getInstance().getMarketDataBlock(security, BarSize.ONE_DAY, oneYearAgo, today);
        runner.setMarketData(marketData);
        StrategyResult result = runner.runStrategy();
        for (Transaction transaction : result.getTransactions()) {
            System.out.println(transaction.toString());
        }

        result.calculateSummary();
        System.out.println(result.toString());
    }

    //Run Bollinger Band strategy
    public static void runRTBollingerStrategy() {
        Security security = Stock.QQQ;
        int SHARES = 10;
        String barSize = BarSize.ONE_MINUTE;
        BollingerStrategy strategy = new BollingerStrategy(2, 20, true);
        RTStrategyRunner runner = new RTStrategyRunner(strategy, security, barSize, SHARES);
        try {
            runner.run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
