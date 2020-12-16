package com.newpoint.instrument;

import java.io.Serializable;
import java.util.Objects;

public class Security implements Serializable {
    public static final String SMART = "SMART";
    public static final String USD = "USD";

    // key variables for a security
    public SecurityType secType;
    public String symbol;
    public String currency;
    public String exchange;
    public String nativeExchange;

    //Key is a unique id for the security, it is implementation specific
    private String secID;
    //name of the security
    private String name;

    public Security(String symbol) {
        this.symbol = symbol;
        this.exchange = SMART;
        this.currency = USD;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Security)) return false;
        Security security = (Security) o;
        return secType == security.secType &&
                Objects.equals(symbol, security.symbol) &&
                Objects.equals(currency, security.currency) &&
                secID.equals(security.secID);
    }

    public int hashCode() {
        return Objects.hash(secType, symbol, currency, secID);
    }

    protected Security(SecurityType secType, String symbol, String exchange, String currency) {
        this.secType = secType;
        this.symbol = symbol;
        this.exchange = exchange;
        this.currency = currency;
    }

    protected Security(SecurityType secType, String symbol) {
        this.secType = secType;
        this.symbol = symbol;
        this.exchange = SMART;
        this.currency = USD;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("Security{");
        sb.append("secType=").append(secType);
        sb.append(", symbol='").append(symbol).append('\'');
        sb.append(", currency='").append(currency).append('\'');
        sb.append(", exchange='").append(exchange).append('\'');
        sb.append(", nativeExchange='").append(nativeExchange).append('\'');
        sb.append(", secID='").append(secID).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getNativeExchange() {
        return nativeExchange;
    }

    public void setNativeExchange(String nativeExchange) {
        this.nativeExchange = nativeExchange;
    }

    public SecurityType getSecType() {
        return secType;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getExchange() {
        return exchange;
    }

    public String getCurrency() {
        return currency;
    }

    public String getSecID() {
        return secID;
    }

    public String getName() {
        return name;
    }

    public void setSecID(String secID) {
        this.secID = secID;
    }

    public void setName(String name) {
        this.name = name;
    }
}