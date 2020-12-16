package com.newpoint.order;

import com.newpoint.instrument.Security;

public class StopOrder extends NPOrder {
    private double stopPrice;

    public StopOrder() {
        super();
        orderType = OrderType.STP;
    }
    public StopOrder(Security security, double quantity, String side, double stopPrice) {
        super(security, quantity, side);
        this.stopPrice = stopPrice;
        orderType = OrderType.STP;
    }

    public String toString() {
        return "StopOrder{" +
                "stopPrice=" + stopPrice +
                "} " + super.toString();
    }

    public void setAttribute(String side, double quantity, double stopPrice )
    {
        super.setAttribute(side, quantity);
        this.stopPrice = stopPrice;
    }

    public double getStopPrice() {
        return stopPrice;
    }

    public void setStopPrice(double stopPrice) {
        this.stopPrice = stopPrice;
    }
}
