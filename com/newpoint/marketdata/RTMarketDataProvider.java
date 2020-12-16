package com.newpoint.marketdata;

import com.ib.client.Contract;
import com.newpoint.instrument.InstrumentManager;
import com.newpoint.instrument.Security;
import com.newpoint.util.ReqIDManager;
import com.newpoint.workstation.NPTradeSession;
import com.newpoint.workstation.NPTradeStation;
import java.util.Hashtable;
import java.util.Map;

public class RTMarketDataProvider {
    private static final RTMarketDataProvider rtDataProvider = new RTMarketDataProvider();
    //bar maps
    private final Map<Integer, MarketDataKey> reqBarMap;
    private final Map<MarketDataKey, RTBarCache> barMap;

    //tick maps
    private final Map<Integer,Security> reqTickMap;
    private Map<Security, TickCache> tickMap;

    private static final int NUMBER_OF_TICKS = 0;
    private static final boolean IGNORE_SIZE = false;

    // static method to create instance of OrderManager class
    public static RTMarketDataProvider getInstance()
    {
        return rtDataProvider;
    }

    public RTMarketDataProvider() {
        this.barMap = new Hashtable<MarketDataKey, RTBarCache>();
        this.reqBarMap = new Hashtable<Integer, MarketDataKey>();
        this.reqTickMap = new Hashtable<Integer, Security>();
        this.tickMap = new Hashtable<Security, TickCache>();
    }

    public void initialize() {
    }

    public void putBar(MarketDataKey barKey, NPBar bar){
        RTBarCache barCache = barMap.get(barKey);
        barCache.putBar(bar);
    }

    public NPBar getBar(MarketDataKey barKey) {
        RTBarCache barCache = barMap.get(barKey);
        return barCache.getBar();
    }
    public void putTick(Security security, Tick tick){
        TickCache tickCache = tickMap.get(security);
        tickCache.putTick(tick);
    }

    public Tick getTick(Security security) {
        TickCache tickCache = tickMap.get(security);
        return tickCache.getTick();
    }

    public TickCache getTickCache(Security security) {
        return tickMap.get(security);
    }

    public Security getTickSecurity(Integer reqId) {
        return reqTickMap.get(reqId);
    }

    public MarketDataKey getMarketDataKey(int reqid) {
        return reqBarMap.get(reqid);
    }

    public RTBarCache getBarCache(MarketDataKey dataKey) {
        return barMap.get(dataKey);
    }

    public void reqRTBar(Security security, String barSize, int dataSize)  {
        MarketDataKey dataKey = new MarketDataKey(security,barSize);
        RTBarCache rtDataCache = new RTBarCache(dataKey, dataSize);
        barMap.put(dataKey,rtDataCache);
        String duration = MarketDataProvider.getDurationByBarSize(barSize);

        //convert to IB contract
        NPTradeSession session = NPTradeStation.getSession();
        Contract contract = InstrumentManager.getInstance().createIBSecurity(security);
        int reqID = ReqIDManager.getInstance().getReqID();
        //send request to IB
        reqBarMap.put(reqID,dataKey);
        session.getClient().reqHistoricalData(reqID, contract, null, duration, barSize, MarketDataProvider.WHAT_TO_SHOW, MarketDataProvider.USE_REGULAR_TRADING_HOUR, MarketDataProvider.RETURN_DATE_FORMAT, true, null);
    }

    public void reqTick(Security security, TickType tickType, int dataSize) {
        //convert to IB contract
        NPTradeSession session = NPTradeStation.getSession();
        Contract contract = InstrumentManager.getInstance().createIBSecurity(security);
        int reqID = ReqIDManager.getInstance().getReqID();
        //send request to IB
        reqTickMap.put(reqID,security);
        session.getClient().reqTickByTickData(reqID, contract, tickType.toString(), NUMBER_OF_TICKS, IGNORE_SIZE);
    }
}
