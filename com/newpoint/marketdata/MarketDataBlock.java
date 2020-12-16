package com.newpoint.marketdata;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MarketDataBlock {
    private List<NPBar> bars;

    public List<NPBar> getBars() {
        return bars;
    }

    public void setBars(List<NPBar> bars) {
        this.bars = bars;
    }

    public NPBar getBarAt(int index) {
        return bars.get(index);
    }

    public int getDataSize() {
        return bars.size();
    }

    //return a typical price double array
    public Collection<NPBar> getBars(int start, int end) {
        List<NPBar> bars = new ArrayList(end - start + 1);
        NPBar bar = null;
        for (int index = start; index < end + 1; index++) {
            bar = getBarAt(index);
            bars.add(bar);
        }
        return bars;
    }

}