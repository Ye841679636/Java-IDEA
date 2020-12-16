package com.newpoint.marketdata;

import java.util.Date;

public class Period {
    private Date startTime;
    private Date endTime;

    public Period(Date startTime, Date endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "PeriodImpl{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
