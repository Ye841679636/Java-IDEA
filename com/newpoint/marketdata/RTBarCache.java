package com.newpoint.marketdata;

import java.util.Collection;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class RTBarCache {
    private MarketDataKey dataKey;
    private final int dataSize;
    private final BlockingDeque<NPBar> barQueue;

    public RTBarCache(MarketDataKey dataKey, int dataSize) {
        this.dataKey = dataKey;
        this.dataSize = dataSize;
        this.barQueue = new LinkedBlockingDeque<NPBar>(dataSize);
    }

    public BlockingDeque<NPBar> getBarQueue() {
        return barQueue;
    }

    public int getSize() {
        return barQueue.size();
    }

    public Collection<NPBar> getBars() {
        return barQueue;
    }

    public void putBar(NPBar bar) {
        try {
            barQueue.put(bar);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public NPBar getBar() {
        NPBar bar = null;
        try {
            bar = barQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bar;
    }

}
