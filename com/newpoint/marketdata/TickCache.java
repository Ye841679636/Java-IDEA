package com.newpoint.marketdata;

import com.newpoint.instrument.Security;

import java.util.ArrayDeque;
import java.util.Deque;

public class TickCache {
    private Security security;
    private int maxSize = 20;
    private final Deque<Tick> tickQueue;

    public TickCache(Security security, int size) {
        this.security = security;
        this.maxSize = size;
        this.tickQueue = new ArrayDeque<Tick>(size);
    }

    public synchronized void putTick(Tick tick) {
        while (tickQueue.size() > maxSize - 1) { tickQueue.remove(); }
        tickQueue.add(tick);
    }

    //return the most recent tick
    public synchronized Tick getTick() {
        return tickQueue.peek();
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public int getSize() {
        return maxSize;
    }

    public void setSize(int size) {
        this.maxSize = size;
    }
}
