package com.newpoint.marketdata;

public enum TickType {
    BID_ASK( "BidAsk"),
    LAST( "Last"),
    MID_POINT( "MidPoint");

    private String tickType;

    TickType(String type) {
        this.tickType = type;
    }

    public String toString() {
        return tickType;
    }
}
