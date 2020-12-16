package com.newpoint.marketdata;

public class TickAttribute {
    private boolean pastLimit = false;
    private boolean preOpen= false;

    public boolean isPastLimit() {
        return pastLimit;
    }

    public void setPastLimit(boolean pastLimit) {
        this.pastLimit = pastLimit;
    }

    public boolean isPreOpen() {
        return preOpen;
    }

    public void setPreOpen(boolean preOpen) {
        this.preOpen = preOpen;
    }
}
