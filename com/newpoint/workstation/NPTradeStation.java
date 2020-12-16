package com.newpoint.workstation;

import com.ib.client.EClientSocket;
import com.ib.client.Order;
import com.newpoint.instrument.Security;
import com.newpoint.instrument.Stock;
import com.newpoint.order.*;

import samples.testbed.contracts.ContractSamples;
import samples.testbed.orders.OrderSamples;

import java.util.*;

//one NP trade station corresponds to one customer only
public class NPTradeStation {
    public static NPTradeSession session = null;

    public static void main(String[] args) {

        try {
            //create a trading session
            session = new NPTradeSession();
            session.start();

            OrderManager orderManager = OrderManager.getInstance();
            //NPOrder npOrder = orderManager.createOrder(SecurityType.STK, OrderType.LMT,BaseOrder.BUY, "BAC", 10, 15);
            NPOrder npOrder = TestExample.getTSLAComboOrder();
            //NPOrder npOrder = getTSLALimitOrder();
            orderManager.placeOrder(npOrder);
            //Collection<NPOrder> orders = orderManager.getAllOrders();
            //TestExample.runBollingerStrategy();
            //TestExample.runRTBollingerStrategy();
            //TestExample.runRTBollingerStrategy();
            //historicalDataRequests2(session.getClient());

            //cancel all open orders
            orderManager.cancelAllOpenOrders(session);
            System.out.println("New Point Trade Station Exit");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            //trade station exits
            session.close();
        }
    }

    public static NPTradeSession getSession() {
        return session;
    }

    private static NPOrder getQQQLimitOrder() {
        Security QQQ = Stock.QQQ;
        LimitOrder limitOrder = new LimitOrder(QQQ, NPOrder.BUY, 20,200);
        return limitOrder;
    }

    private static NPOrder getTSLALimitOrder() {
        Security TSLA = Stock.TSLA;
        LimitOrder limitOrder = new LimitOrder(TSLA, NPOrder.BUY, 5,400);
        return limitOrder;
    }

    private void placeOrder(EClientSocket client, int nextOrderId) {
        LimitOrder lmtOrder = new LimitOrder();
        client.placeOrder(nextOrderId++, ContractSamples.USStock(), OrderSamples.LimitOrder("SELL", 1, 50));
    }

    private void hedgeSample(EClientSocket client, int nextOrderId) {
        //Parent order on a contract which currency differs from your base currency
        Order parent = OrderSamples.LimitOrder("BUY", 100, 10);
        parent.orderId(nextOrderId++);
        parent.transmit(false);
        //Hedge on the currency conversion
        Order hedge = OrderSamples.MarketFHedge(parent.orderId(), "BUY");
        //Place the parent first...
        client.placeOrder(parent.orderId(), ContractSamples.EuropeanStock(), parent);
        //Then the hedge order
        client.placeOrder(nextOrderId++, ContractSamples.EurGbpFx(), hedge);
    }

    private void bracketSample(EClientSocket client, int nextOrderId) {

        List<Order> bracket = OrderSamples.BracketOrder(nextOrderId++, "BUY", 100, 30, 40, 20);
        for(Order o : bracket) {
            client.placeOrder(o.orderId(), ContractSamples.EuropeanStock(), o);
        }
    }

    private static void historicalDataRequests2(EClientSocket client) throws InterruptedException {
        /*** Requesting historical data ***/
        //! [reqHeadTimeStamp]
        // client.reqHeadTimestamp(4001, ContractSamples.USStock(), "TRADES", 1, 1);
        //! [reqHeadTimeStamp]
        //! [cancelHeadTimestamp]
        //client.cancelHeadTimestamp(4001);
        //! [cancelHeadTimestamp]
        //! [reqhistoricaldata]
        TimeZone tz=TimeZone.getTimeZone("America/New_York");
        Calendar gc2=Calendar.getInstance(tz, Locale.US);
        String formatted2 = "" +
                gc2.get(Calendar.YEAR) +
                pad(gc2.get(Calendar.MONTH) + 1) +
                pad(gc2.get(Calendar.DAY_OF_MONTH)) + " " +
                pad(gc2.get(Calendar.HOUR_OF_DAY)) + ":" +
                pad(gc2.get(Calendar.MINUTE)) + ":" +
                pad(gc2.get(Calendar.SECOND)) + " " +
                gc2.getTimeZone().getDisplayName( false, TimeZone.SHORT);
        //client.reqHistoricalData(4001, ContractSamples.STK(), formatted2, "1 M", "1 day", "TRADES", 0, 1, false, null);
        client.reqHistoricalData(4002, ContractSamples.STK(), formatted2, "1 D", "1 min", "TRADES", 1, 1, false, null);
        //client.reqMktData(1001, ContractSamples.STK(), "", false, false, null);
        //client.reqHistoricalTicks(18001, ContractSamples.STK(), "20200710 10:39:33", null, 10, "TRADES", 1, true, null);
        //client.reqHistoricalTicks(18002, ContractSamples.STK(), "20170712 09:39:33", null, 10, "BID_ASK", 1, true, null);
        //client.reqHistoricalTicks(18003, ContractSamples.STK(), "20170712 09:39:33", null, 10, "MIDPOINT", 1, true, null);
        Thread.sleep(50);
        /*** Canceling historical data requests ***/
        //client.cancelHistoricalData(4001);
        //client.cancelHistoricalData(4002);
        //client.cancelHistoricalData(18001);
        //client.cancelHistoricalData(18002);
        //client.cancelHistoricalData(18003);
        return;
    }

    private static String pad(int val) {
        return val < 10 ? "0" + val : "" + val;
    }
}

