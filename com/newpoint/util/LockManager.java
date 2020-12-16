package com.newpoint.util;

public class LockManager {
    private static final LockManager lockManager = new LockManager();
    private final Object orderFillLock = new Object();
    private final Object orderLock  = new Object();
    private final Object orderReadyLock  = new Object();
    private final Object accountLock  = new Object();
    private final Object marketDataLock = new Object();

    public LockManager() {
    }

    public static LockManager getInstance() {
        return lockManager;
    }

    public Object getOrderFillLock() {
        return orderFillLock;
    }

    public Object getOrderLock() {
        return orderLock;
    }

    public Object getAccountLock() {
        return accountLock;
    }

    public Object getOrderReadyLock() {
        return orderReadyLock;
    }

    public Object getMarketDataLock() {
        return marketDataLock;
    }
}
