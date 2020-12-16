package com.newpoint.marketdata;

import java.io.Serializable;
import java.util.Date;
import java.util.SortedMap;
import java.util.TreeMap;

public class MarketDataCache implements Serializable {
    private MarketDataKey cacheKey;
    private SortedMap<Date, NPBar> barCache = new TreeMap<Date, NPBar>();

    public MarketDataCache(MarketDataKey cacheKey) {
        this.cacheKey = cacheKey;
    }

    public void addBar(Date date, NPBar bar) {
        barCache.put(date, bar);
    }

    public NPBar getBar(Date date) {
        return barCache.get(date);
    }

    public SortedMap<Date, NPBar> getBarCache() {
        return barCache;
    }
}
