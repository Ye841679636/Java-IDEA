package com.newpoint.marketdata;


import com.newpoint.instrument.Security;

import java.io.Serializable;
import java.util.Objects;

public class MarketDataKey implements Serializable {
    private Security security;
    private String barSize;

    public MarketDataKey(Security security, String barSize) {
        this.security = security;
        this.barSize = barSize;
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("MarketDataKey{");
        sb.append(security.symbol);
        sb.append(", ").append(barSize).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketDataKey)) return false;
        MarketDataKey that = (MarketDataKey) o;
        return Objects.equals(security, that.security) &&
                Objects.equals(barSize, that.barSize);
    }

    public int hashCode() {
        return Objects.hash(security, barSize);
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public String getBarSize() {
        return barSize;
    }

    public void setBarSize(String barSize) {
        this.barSize = barSize;
    }
}
