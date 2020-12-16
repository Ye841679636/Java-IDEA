package com.newpoint.account;

import com.newpoint.instrument.Security;

import java.util.Objects;

//Assume for each instrument, there is only one position for a user account
public class Position {
    private Account account;
    private Security security;

    //Quantity of long/short shares or contracts, + long - short
    private double quantity;

    //Average cost of stock and securities options opening positions, including commissions.
    private double averageCost;

    //(Market value of positions since close of period*) - (average cost for these positions).
    //Cut-off times for determining realized P&L for transactions are as follows:
    //20:30 ET for securities, Two periods for futures: 17:30 ET and 7:10 ET
    private double realizedPL;

    //(Market value of positions) - (average cost).
    private double unrealizedPL;

    //(quantity) x (market price).
    private double marketValue;

    //Real-time price of the position.
    private double marketPrice;

    public Position(Account account, Security security, double quantity) {
        this.account = account;
        this.security = security;
        this.quantity = quantity;
    }

    public Security getInstrument() { return security;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Position)) return false;
        Position position = (Position) o;
        return account.equals(position.account) &&
                security.equals(position.security);
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("Position{");
        sb.append("account=").append(account);
        sb.append(", security=").append(security);
        sb.append(", quantity=").append(quantity);
        sb.append(", averageCost=").append(averageCost);
        sb.append(", realizedPL=").append(realizedPL);
        sb.append(", unrealizedPL=").append(unrealizedPL);
        sb.append(", marketValue=").append(marketValue);
        sb.append(", marketPrice=").append(marketPrice);
        sb.append('}');
        return sb.toString();
    }

    public int hashCode() {
        return Objects.hash(account, security);
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getAverageCost() {
        return averageCost;
    }

    public void setAverageCost(double averageCost) {
        this.averageCost = averageCost;
    }

    public double getRealizedPL() {
        return realizedPL;
    }

    public void setRealizedPL(double realizedPL) {
        this.realizedPL = realizedPL;
    }

    public double getUnrealizedPL() {
        return unrealizedPL;
    }

    public void setUnrealizedPL(double unrealizedPL) {
        this.unrealizedPL = unrealizedPL;
    }

    public double getMarketValue() {
        return marketValue;
    }

    public void setMarketValue(double marketValue) {
        this.marketValue = marketValue;
    }

    public double getMarketPrice() {
        return marketPrice;
    }

    public void setMarketPrice(double marketPrice) {
        this.marketPrice = marketPrice;
    }
}
