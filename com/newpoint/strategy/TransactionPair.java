package com.newpoint.strategy;

import com.newpoint.order.Transaction;

public class TransactionPair {
    private Transaction buy;
    private Transaction sell;

    public TransactionPair(Transaction buy, Transaction sell) {
        this.buy = buy;
        this.sell = sell;
    }

    public boolean isProfit() {
        return sell.getPrice() > buy.getPrice();
    }

    public double getPL() {
        return sell.getPrice() - buy.getPrice();
    }

    public Transaction getBuy() {
        return buy;
    }

    public void setBuy(Transaction buy) {
        this.buy = buy;
    }

    public Transaction getSell() {
        return sell;
    }

    public void setSell(Transaction sell) {
        this.sell = sell;
    }
}
