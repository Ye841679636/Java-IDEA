package com.newpoint.order;

import com.newpoint.instrument.Security;

public class LimitOrder extends NPOrder {
    private double limitPrice;

    public LimitOrder() {
        super();
        orderType = OrderType.LMT;
    }

    public void setAttribute(String side, double quantity, double limitPrice )
    {
        super.setAttribute(side, quantity);
        this.limitPrice = limitPrice;
    }

    public LimitOrder(Security security, String side, double quantity, double limitPrice) {
        super(security, quantity, side);
        this.limitPrice = limitPrice;
        orderType = OrderType.LMT;
    }

    public String toString() {
        return "LimitOrder{" +
                "limitPrice=" + limitPrice +
                "} " + super.toString();
    }

    public double getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
    }
}
