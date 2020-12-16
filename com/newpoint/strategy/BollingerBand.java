package com.newpoint.strategy;

import com.newpoint.marketdata.NPBar;

public class BollingerBand {
    private double BU;
    private double BM;
    private double BL;
    private double HIGH;
    private double LOW;
    private double OPEN;
    private double CLOSE;

    private BollingerBand preOne;

    private NPBar bar;

    public BollingerBand(NPBar bar) {
        this.bar = bar;
        HIGH = bar.getHigh();
        LOW = bar.getLow();
        OPEN = bar.getOpen();
        CLOSE = bar.getOpen();
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("BollingerBand{");
        sb.append("BU=").append(BU);
        sb.append(", BM=").append(BM);
        sb.append(", BL=").append(BL);
        sb.append(", HIGH=").append(HIGH);
        sb.append(", LOW=").append(LOW);
        sb.append(", OPEN=").append(OPEN);
        sb.append(", CLOSE=").append(CLOSE);
        sb.append('}');
        return sb.toString();
    }

    public double getHIGH() {
        return HIGH;
    }

    public double getLOW() {
        return LOW;
    }

    public double getOPEN() {
        return OPEN;
    }

    public double getCLOSE() {
        return CLOSE;
    }

    public double getBU() {
        return BU;
    }

    public void setBU(double PBU) {
        this.BU = PBU;
    }

    public double getBM() {
        return BM;
    }

    public void setBM(double PBM) {
        this.BM = PBM;
    }

    public double getBL() {
        return BL;
    }

    public void setBL(double PBL) {
        this.BL = PBL;
    }

    public BollingerBand getPreOne() {
        return preOne;
    }

    public void setPreOne(BollingerBand preOne) {
        this.preOne = preOne;
    }

    public NPBar getBar() {
        return bar;
    }

    public void setBar(NPBar bar) {
        this.bar = bar;
    }
}
