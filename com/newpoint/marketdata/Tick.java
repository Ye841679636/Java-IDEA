package com.newpoint.marketdata;

import java.util.Date;

public class Tick {
    private TickType type;
    private TickAttribute attribute;
    private Date time;
    private double bidPrice;
    private double askPrice;
    private double lastPrice;
    private double midPrice;
    private long bidSize;
    private long askSize;
    private long lastSize;
    private String exchange;

    public TickType getType() {
        return type;
    }

    public void setType(TickType type) {
        this.type = type;
    }

    public TickAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(TickAttribute attribute) {
        this.attribute = attribute;
    }

    public double getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(double bidPrice) {
        this.bidPrice = bidPrice;
    }

    public double getAskPrice() {
        return askPrice;
    }

    public void setAskPrice(double askPrice) {
        this.askPrice = askPrice;
    }

    public long getBidSize() {
        return bidSize;
    }

    public void setBidSize(long bidSize) {
        this.bidSize = bidSize;
    }

    public long getAskSize() {
        return askSize;
    }

    public void setAskSize(long askSize) {
        this.askSize = askSize;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getMidPrice() {
        return midPrice;
    }

    public void setMidPrice(double midPrice) {
        this.midPrice = midPrice;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }
}
