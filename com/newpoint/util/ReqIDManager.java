package com.newpoint.util;

public class ReqIDManager {
    private static final ReqIDManager rtDataProvider = new ReqIDManager();
    private int reqID = 1000;

    public static ReqIDManager getInstance() {
        return rtDataProvider;
    }

    public synchronized int getReqID() {
        return reqID++;
    }

}
