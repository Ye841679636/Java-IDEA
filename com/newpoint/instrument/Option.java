package com.newpoint.instrument;

import java.util.Objects;

public class Option extends Security {
    public static final String CALL = "C";
    public static final String PUT = "P";

    private double strikePrice;
    private int multiplier;

    private String right;  //either put or call
    //The contract's last trading day or contract month (for Options and Futures).
    //Strings with format YYYYMM will be interpreted as the Contract Month whereas YYYYMMDD will be interpreted as Last Trading Day.
    private String lastTradeDateOrContractMonth;

    public Option(String symbol) {
        super(symbol);
        secType = SecurityType.OPT;
    }

    public Option(String symbol, String exchange, String currency, double strikePrice, int multiplier, String right, String lastTradeDateOrContractMonth) {
        super(SecurityType.OPT, symbol, exchange, currency);
        this.strikePrice = strikePrice;
        this.multiplier = multiplier;
        this.right = right;
        this.lastTradeDateOrContractMonth = lastTradeDateOrContractMonth;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Option)) return false;
        if (!super.equals(o)) return false;
        Option option = (Option) o;
        return Double.compare(option.strikePrice, strikePrice) == 0 &&
                multiplier == option.multiplier &&
                right.equals(option.right) &&
                lastTradeDateOrContractMonth.equals(option.lastTradeDateOrContractMonth);
    }

    public int hashCode() {
        return Objects.hash(super.hashCode(), strikePrice, multiplier, right, lastTradeDateOrContractMonth);
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("Option{");
        sb.append(super.toString());
        sb.append("strikePrice=").append(strikePrice);
        sb.append(", multiplier=").append(multiplier);
        sb.append(", right='").append(right).append('\'');
        sb.append(", lastTradeDateOrContractMonth='").append(lastTradeDateOrContractMonth).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    public int getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(int multiplier) {
        this.multiplier = multiplier;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public String getLastTradeDateOrContractMonth() {
        return lastTradeDateOrContractMonth;
    }

    public void setLastTradeDateOrContractMonth(String lastTradeDateOrContractMonth) {
        this.lastTradeDateOrContractMonth = lastTradeDateOrContractMonth;
    }
}
