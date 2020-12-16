package com.newpoint.order;

import com.newpoint.instrument.Security;

public class StopLimitOrder extends NPOrder {
    private double limitPrice;
    private double stopPrice;

    public StopLimitOrder(Security security, double quantity, String side, double stopPrice, double limitPrice) {
        super(security, quantity, side);
        this.stopPrice = stopPrice;
        this.limitPrice = limitPrice;
        orderType = OrderType.STP_LMT;
    }

    public double getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(double limitPrice) {
        this.limitPrice = limitPrice;
    }

    public double getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(double stopPrice) {
        this.stopPrice = stopPrice;
    }
}
