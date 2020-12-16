package com.newpoint.workstation;

import com.ib.client.*;
import com.newpoint.account.*;
import com.newpoint.instrument.InstrumentManager;
import com.newpoint.instrument.Security;
import com.newpoint.marketdata.*;
import com.newpoint.marketdata.TickType;
import com.newpoint.order.NPOrder;
import com.newpoint.order.ComboOrder;
import com.newpoint.order.NPOrderStatus;
import com.newpoint.order.OrderManager;
import com.newpoint.util.DateUtil;
import com.newpoint.util.InitData;
import com.newpoint.util.LockManager;
import com.newpoint.util.ReqIDManager;
import samples.testbed.EWrapperImpl;

import java.util.*;

public class NPEWrapper extends EWrapperImpl {
    private NPTradeSession session;
    private final AccountManager accountManager;
    private final InstrumentManager instrumentManager;
    private final OrderManager orderManager;
    private final MarketDataProvider marketDataProvider;
    private final RTMarketDataProvider rtMarketDataProvider;
    private final MarketDataCacheManager dataCacheManager;
    private Map<Integer, Set<NPOrder>> parentMap;

    private final Object orderLock;
    private final Object orderReadyLock;
    private final Object accountLock;

    public NPEWrapper(NPTradeSession aSession) {
        this.session = aSession;
        this.accountManager = AccountManager.getInstance();
        this.instrumentManager = InstrumentManager.getInstance();
        this.orderManager = OrderManager.getInstance();
        this.marketDataProvider = MarketDataProvider.getInstance();
        this.rtMarketDataProvider = RTMarketDataProvider.getInstance();
        this.dataCacheManager = MarketDataCacheManager.getInstance();
        this.parentMap = new HashMap<>();
        this.orderLock = LockManager.getInstance().getOrderLock();
        this.orderReadyLock = LockManager.getInstance().getOrderReadyLock();
        this.accountLock = LockManager.getInstance().getAccountLock();
    }

    public NPTradeSession getSession() {
        return session;
    }

    public void setSession(NPTradeSession session) {
        this.session = session;
    }

    public void nextValidId(int orderId) {
        System.out.println("Next Valid Id: [" + orderId + "]");
        synchronized (orderReadyLock) {
            orderManager.setNextOrderNumber(orderId);
            session.setOrderReady(true);
        }
        System.out.println("****Order entry is initialized*****");
    }

    //The callback is called immediately after a connection is established.
    public void managedAccounts(String accountsList) {
        System.out.println("Account list: " + accountsList);
        synchronized (accountLock) {
            List<String> acctList = new ArrayList(Arrays.asList(accountsList.split(",")));
            //create all the account objects associated with this customer
            for (String accountName : acctList) {
                Account account = accountManager.getAccount(accountName);
                if (account == null) {
                    accountManager.createAccount(accountName);
                }
                session.getClient().reqAccountUpdates(true, accountName);
            }
            int nextReqID = ReqIDManager.getInstance().getReqID();
            session.getClient().reqAccountSummary(nextReqID, "All", "AccountType,NetLiquidation,TotalCashValue,SettledCash,AccruedCash,BuyingPower,EquityWithLoanValue,PreviousEquityWithLoanValue,GrossPositionValue,ReqTEquity,ReqTMargin,SMA,InitMarginReq,MaintMarginReq,AvailableFunds,ExcessLiquidity,Cushion,FullInitMarginReq,FullMaintMarginReq,FullAvailableFunds,FullExcessLiquidity,LookAheadNextChange,LookAheadInitMarginReq ,LookAheadMaintMarginReq,LookAheadAvailableFunds,LookAheadExcessLiquidity,HighestSeverity,DayTradesRemaining,Leverage");
        }
    }

    /********Start of Account Callbacks ********/
    public void accountSummary(int reqId, String accountName, String tag, String value, String currency) {
        synchronized (accountLock) {
            Account account = accountManager.getAccount(accountName);
            if (account.containsAttribute(tag)) {
                account.setAttribute(tag, Double.parseDouble(value));
            }
        }
        System.out.println("Acct Summary. ReqId: " + reqId + ", Acct: " + accountName + ", Tag: " + tag + ", Value: " + value + ", Currency: " + currency);
    }

    public void accountSummaryEnd(int reqId) {
        synchronized (accountLock) {
            session.setAccountReady(true);
            accountLock.notify();
        }
        System.out.println("AccountSummaryEnd. Req Id: "+reqId+"\n");
    }

    public void updateAccountValue(String key, String value, String currency, String accountName) {
        System.out.println("UpdateAccountValue. Key: " + key + ", Value: " + value + ", Currency: " + currency + ", AccountName: " + accountName);
    }

    public void updateAccountTime(String timeStamp) {
        System.out.println("UpdateAccountTime. Time: " + timeStamp+"\n");
    }

    public void accountDownloadEnd(String accountName) {
        System.out.println("Account download finished: "+accountName+"\n");
    }

    public void accountUpdateMulti(int reqId, String account, String modelCode,
                                   String key, String value, String currency) {
        System.out.println("Account Update Multi. Request: " + reqId + ", Account: " + account + ", ModelCode: " + modelCode + ", Key: " + key + ", Value: " + value + ", Currency: " + currency + "\n");
    }

    public void accountUpdateMultiEnd(int reqId) {
        System.out.println("Account Update Multi End. Request: " + reqId + "\n");
    }
    /********End of Account Callbacks ********/

    /********Start Of Position Callbacks ********/
    public void updatePortfolio(Contract contract, double quantity,
                                double marketPrice, double marketValue, double averageCost,
                                double unrealizedPNL, double realizedPNL, String accountName) {
        synchronized (accountLock) {
            Account account = accountManager.getAccount(accountName);
            Security security = instrumentManager.getSecurity(String.valueOf(contract.conid()));
            if (security == null) {
                security = instrumentManager.createSecurity(contract);
            }
            Position position = new Position(account, security, quantity);
            position.setMarketPrice(marketPrice);
            position.setAverageCost(averageCost);
            position.setMarketValue(marketValue);
            position.setRealizedPL(realizedPNL);
            position.setUnrealizedPL(unrealizedPNL);
            account.addPosition(position);
        }
        // Position position = accountManager.createAccount()
        System.out.println("UpdatePortfolio. "+contract.symbol()+", "+contract.secType()+" @ "+contract.exchange()
                +": Position: "+quantity+", MarketPrice: "+marketPrice+", MarketValue: "+marketValue+", AverageCost: "+averageCost
                +", UnrealizedPNL: "+unrealizedPNL+", RealizedPNL: "+realizedPNL+", AccountName: "+accountName);
    }

    public void position(String accountName, Contract contract, double pos, double avgCost) {
        synchronized (accountLock) {
            Account account = accountManager.getAccount(accountName);
            Security security = instrumentManager.getSecurity(String.valueOf(contract.conid()));
            if (security == null) {
                security = instrumentManager.createSecurity(contract);
            }
            Position position = account.getPosition(security);
            //expired option position (converted to underlying security) could be sent here, the account will not have this position
            if (position != null) {
                position.setQuantity(pos);
                position.setAverageCost(avgCost);
            }
        }
        System.out.println("Position. "+accountName+" - Symbol: "+contract.symbol()+", SecType: "+contract.secType()+", Currency: "+contract.currency()+", Position: "+pos+", Avg cost: "+avgCost);
    }

    public void positionEnd() {
        System.out.println("PositionEnd \n");
    }

    public void positionMulti(int reqId, String account, String modelCode,
                              Contract contract, double pos, double avgCost) {
        System.out.println("Position Multi. Request: " + reqId + ", Account: " + account + ", ModelCode: " + modelCode + ", Symbol: " + contract.symbol() + ", SecType: " + contract.secType() + ", Currency: " + contract.currency() + ", Position: " + pos + ", Avg cost: " + avgCost + "\n");
    }

    public void positionMultiEnd(int reqId) {
        System.out.println("Position Multi End. Request: " + reqId + "\n");
    }
    /********End Of Position Callbacks ********/

    /********Start Of Order Management Callbacks ********/
    public void orderStatus(int orderId, String status, double filled,
                            double remaining, double avgFillPrice, int permId, int parentId,
                            double lastFillPrice, int clientId, String whyHeld, double mktCapPrice) {
        synchronized (orderLock) {
            NPOrderStatus orderStatus = new NPOrderStatus(status);
            NPOrder order = orderManager.getOrderByNumber(orderId);

            assert (order != null);
            orderStatus.setFilled(filled);
            orderStatus.setRemaining(remaining);
            orderStatus.setAverageFillPrice(avgFillPrice);
            orderStatus.setLastFillPrice(lastFillPrice);
            orderStatus.setReason(whyHeld);
            order.setStatus(orderStatus);
            if (orderStatus.isFilled()) {
                LockManager.getInstance().getOrderFillLock().notify();
            }
        }
        System.out.println("NPOrderStatus. Id: "+orderId+", Status: "+status+", Filled: "+filled+", Remaining: "+remaining
                +", AvgFillPrice: "+avgFillPrice+", PermId: "+permId+", ParentId: "+parentId+", LastFillPrice: "+lastFillPrice+
                ", ClientId: "+clientId+", WhyHeld: "+whyHeld+", MktCapPrice: "+mktCapPrice);
    }

    public void openOrder(int orderId, Contract contract, Order order, OrderState orderState) {
        synchronized (orderLock) {
            Security security = instrumentManager.getSecurity(String.valueOf(contract.conid()));
            if (security == null) {
                security = instrumentManager.createSecurity(contract);
            }
            NPOrder npOrder = orderManager.getOrderByNumber(order.orderId());
            if (npOrder == null) {
                npOrder = orderManager.createOrder(order);
                npOrder.setSecurity(security);
            }

            if (order.parentId() != 0) {  //this current order has a parent
                NPOrder parent = orderManager.getOrderByNumber(order.parentId());
                if (parent == null) {
                    //check if the parent is in the parentMap
                    Set<NPOrder> children = parentMap.get(order.parentId());
                    if (children == null) {
                        int INITIAL_CHILDREN_SIZE = 2;
                        children = new HashSet<>(INITIAL_CHILDREN_SIZE);
                        parentMap.put(order.parentId(), children);
                    }
                    children.add(npOrder);
                } else {
                    //first set up IB parent child relationship
                    parent.addChild(npOrder);
                    npOrder.setParent(parent);
                    //If we get here, the openOrder send parent order first, we need to locate the parent order
                    //we look in the order manager to find an combo order whose entry order is the current parent order
                    ComboOrder comboOrder = orderManager.getComboOrder(parent.getOrderNumber());
                    if (comboOrder == null) {
                        comboOrder = orderManager.createComboOrder();
                    }
                    comboOrder.setEntryOrder(parent);
                    parent.setParent(comboOrder);
                    comboOrder.addChild(npOrder);
                }
            } else { //search parent map to see if the current order is a parent
                int orderNumber = npOrder.getOrderNumber();
                if (parentMap.containsKey(orderNumber)) {
                    //create a combo order
                    ComboOrder comboOrder = orderManager.createComboOrder();
                    comboOrder.setEntryOrder(npOrder);
                    npOrder.setParent(comboOrder);
                    comboOrder.setChildren(parentMap.get(orderNumber));
                    for (NPOrder childOrder : comboOrder.getChildren()) {
                        childOrder.setParent(npOrder);
                    }
                    parentMap.remove(orderNumber);
                }
            }
        }
        System.out.println(EWrapperMsgGenerator.openOrder(orderId, contract, order, orderState));
    }

    public void openOrderEnd() {
        System.out.println("OpenOrderEnd");
    }

    public void orderBound(long orderId, int apiClientId, int apiOrderId) {
        System.out.println(EWrapperMsgGenerator.orderBound(orderId, apiClientId, apiOrderId));
    }

    public void execDetails(int reqId, Contract contract, Execution execution) {
        System.out.println("ExecDetails. "+reqId+" - ["+contract.symbol()+"], ["+contract.secType()+"], ["+contract.currency()+"], ["+execution.execId()+
                "], ["+execution.orderId()+"], ["+execution.shares()+"]"  + ", [" + execution.lastLiquidity() + "]");
    }

    public void execDetailsEnd(int reqId) {
        System.out.println("ExecDetailsEnd. "+reqId+"\n");
    }

    public void completedOrder(Contract contract, Order order, OrderState orderState) {
        System.out.println(EWrapperMsgGenerator.completedOrder(contract, order, orderState));
    }

    public void completedOrdersEnd() {
        System.out.println(EWrapperMsgGenerator.completedOrdersEnd());
    }
    /********End Of Order Management Callbacks ********/


    /********Start Of Market Data Callbacks ********/
    public void historicalData(int reqId, Bar bar) {
        System.out.println("HistoricalData. " + reqId + " - Date: " + bar.time() + ", Open: " + bar.open() + ", High: " + bar.high() + ", Low: " + bar.low() + ", Close: " + bar.close() + ", Volume: " + bar.volume() + ", Count: " + bar.count() + ", WAP: " + bar.wap());
        //writeTxt(reqId, bar);
        InitData.savaHistoricalData(reqId, bar);

        /*
        MarketDataKey dataKey = marketDataProvider.getMarketDataKey(reqId);
        if (dataKey == null)
            throw new RuntimeException("Historical Data key for request " + reqId + " not found");
        Date barTime = DateUtil.parse(bar.time());

        NPBar npBar = new NPBar(bar.high(),bar.low(),bar.open(),bar.close(),bar.volume(),barTime);
        MarketDataCache dataCache = dataCacheManager.getDataCache(dataKey);
        dataCache.addBar(barTime, npBar);*/
    }

    public void historicalDataEnd(int reqId, String startDateStr, String endDateStr) {
        System.out.println("HistoricalDataEnd. " + reqId + " - Start Date: " + startDateStr + ", End Date: " + endDateStr);
        //marketDataProvider.reqFinished(reqId);
        MarketDataProvider.reqList.add(reqId);;
    }

    public void historicalTicks(int reqId, List<HistoricalTick> ticks, boolean done) {
        for (HistoricalTick tick : ticks) {
            System.out.println(EWrapperMsgGenerator.historicalTick(reqId, tick.time(), tick.price(), tick.size()));
        }
    }

    public void historicalTicksBidAsk(int reqId, List<HistoricalTickBidAsk> ticks, boolean done) {
        for (HistoricalTickBidAsk tick : ticks) {
            System.out.println(EWrapperMsgGenerator.historicalTickBidAsk(reqId, tick.time(), tick.tickAttribBidAsk(), tick.priceBid(), tick.priceAsk(), tick.sizeBid(),
                    tick.sizeAsk()));
        }
    }

    public void historicalTicksLast(int reqId, List<HistoricalTickLast> ticks, boolean done) {
        for (HistoricalTickLast tick : ticks) {
            System.out.println(EWrapperMsgGenerator.historicalTickLast(reqId, tick.time(), tick.tickAttribLast(), tick.price(), tick.size(), tick.exchange(),
                    tick.specialConditions()));
        }
    }

    public void histogramData(int reqId, List<HistogramEntry> items) {
        System.out.println(EWrapperMsgGenerator.histogramData(reqId, items));
    }

    //Receives bars in real time if keepUpToDate is set as True in reqHistoricalData. Similar to realTimeBars function,
    //except returned data is a composite of historical data and real time data that is equivalent to TWS chart functionality
    //to keep charts up to date. Returned bars are successfully updated using real time data.
    public void historicalDataUpdate(int reqId, Bar bar) {
        MarketDataKey dataKey = rtMarketDataProvider.getMarketDataKey(reqId);
        if (dataKey == null)
            throw new RuntimeException("Real time Market Data key for request " + reqId + " not found");
        Date barTime = DateUtil.parse(bar.time());

        NPBar npBar = new NPBar(bar.high(),bar.low(),bar.open(),bar.close(),bar.volume(),barTime);
        RTBarCache rtCache = rtMarketDataProvider.getBarCache(dataKey);
        rtCache.putBar(npBar);
        System.out.println("historicalDataUpdate. "+reqId+" - Date: "+bar.time()+", Open: "+bar.open()+", High: "+bar.high()+", Low: "+bar.low()+", Close: "+bar.close()+", Volume: "+bar.volume()+", Count: "+bar.count()+", WAP: "+bar.wap());
    }
    /********End Of Market Data Callbacks ********/

    /********Start Of Real Time Data Callbacks ********/
    public void tickByTickAllLast(int reqId, int tickType, long time, double price, int size, TickAttribLast tickAttribLast,
                                  String exchange, String specialConditions) {
        System.out.println(EWrapperMsgGenerator.tickByTickAllLast(reqId, tickType, time, price, size, tickAttribLast, exchange, specialConditions));
    }

    public void tickByTickBidAsk(int reqId, long time, double bidPrice, double askPrice, int bidSize, int askSize,
                                 TickAttribBidAsk tickAttribBidAsk) {
        Security security = rtMarketDataProvider.getTickSecurity(reqId);
        if (security == null) {
            throw new RuntimeException("tickByTickBidAsk: Unable to find security for req " + reqId);
        }
        Date tickTime = DateUtil.UnixSecondsToDate(time);
        Tick tick = new Tick();
        tick.setType(TickType.BID_ASK);
        tick.setBidPrice(bidPrice);
        tick.setAskPrice(askPrice);
        tick.setBidSize(bidSize);
        tick.setAskSize(askSize);
        rtMarketDataProvider.putTick(security,tick);

        System.out.println(EWrapperMsgGenerator.tickByTickBidAsk(reqId, time, bidPrice, askPrice, bidSize, askSize, tickAttribBidAsk));
    }

    public void tickByTickMidPoint(int reqId, long time, double midPoint) {
        System.out.println(EWrapperMsgGenerator.tickByTickMidPoint(reqId, time, midPoint));
    }
    /********End Of Real Time Data Callbacks ********/

    /********Start Of Error Handing Callbacks ********/
    public void error(Exception e) {
        System.out.println("Exception: "+e.getMessage());
    }

    public void error(String str) {
        System.out.println("Error STR");
    }

    public void error(int id, int errorCode, String errorMsg) {
        System.out.println("Error. Id: " + id + ", Code: " + errorCode + ", Msg: " + errorMsg + "\n");
    }
    /********End Of Error Handing Callbacks ********/

    public void connectionClosed() {
        System.out.println("Connection closed");
    }

    //could be a deprecated method,
    public void connectAck() {
        if (session.getClient().isAsyncEConnect()) {
            System.out.println("Acknowledging connection");
            session.getClient().startAPI();
        }
    }

}
