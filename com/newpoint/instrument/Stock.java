package com.newpoint.instrument;

public class Stock extends Security {
    public static Stock TSLA = new Stock("TSLA");
    public static Stock QQQ = new Stock("QQQ");

    public Stock(String symbol) {
        super(symbol);
        this.secType = SecurityType.STK;
    }

    public Stock(String symbol, String exchange, String currency) {
        super(SecurityType.STK, symbol, exchange, currency);
    }
}
