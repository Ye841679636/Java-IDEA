package com.newpoint.marketdata;

import java.io.Serializable;
import java.util.Date;

public class NPBar implements Serializable {
    private double high;
    private double low;
    private double open;
    private double close;
    private long volume;
    private Date time;

    public NPBar(double high, double low, double open, double close, long volume, Date time) {
        this.high = high;
        this.low = low;
        this.open = open;
        this.close = close;
        this.volume = volume;
        this.time = time;
    }

    public NPBar() {
    }

    @Override
    public String toString() {
        return "NPBar{" +
                "high=" + high +
                ", low=" + low +
                ", open=" + open +
                ", close=" + close +
                ", volume=" + volume +
                ", time=" + time +
                '}';
    }

    public double getHigh() {
        return high;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public double getLow() {
        return low;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public double getClose() {
        return close;
    }

    public void setClose(double close) {
        this.close = close;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
