package com.newpoint.order;

import java.util.Date;

public class Transaction {
    private NPOrder order;
    private String side;
    private Date time;
    private String exchange;
    private double shares;
    private double price;
    private double executionCommission;
    private double regulatoryCommission;
    private boolean isLiquidation;

    public Transaction() {
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("Transaction{");
        sb.append("side='").append(side).append('\'');
        sb.append(", time=").append(time);
        sb.append(", shares=").append(shares);
        sb.append(", price=").append(price);
        sb.append('}');
        return sb.toString();
    }

    public NPOrder getOrder() {
        return order;
    }

    public void setOrder(NPOrder order) {
        this.order = order;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public double getShares() {
        return shares;
    }

    public void setShares(double shares) {
        this.shares = shares;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getExecutionCommission() {
        return executionCommission;
    }

    public void setExecutionCommission(double executionCommission) {
        this.executionCommission = executionCommission;
    }

    public double getRegulatoryCommission() {
        return regulatoryCommission;
    }

    public void setRegulatoryCommission(double regulatoryCommission) {
        this.regulatoryCommission = regulatoryCommission;
    }

    public boolean isLiquidation() {
        return isLiquidation;
    }

    public void setLiquidation(boolean liquidation) {
        isLiquidation = liquidation;
    }
}
