package com.newpoint.strategy;

import com.newpoint.marketdata.NPBar;
import com.newpoint.order.NPOrder;
import com.newpoint.order.Transaction;

import java.util.Collection;

public class BollingerStrategy extends Strategy {
    //display strings
    public static String BOLLINGER = "Bollinger Band";
    public static String NUM_OF_STD = "Number of Standard Deviations";
    public static String NUM_OF_PERIOD = "Number of Periods";
    public static String BOUNCE_FROM_MIDDLE = "Bounce From Middle";
    public static String BOUNCE_FROM_LOW = "Bounce From LOW";
    public static int SHARE = 1;

    private int numOfSTD = 2; // number of standard deviation, default is 2
    private int numOfPeriod = 20; //number of period for average calculation, default is 20
    private boolean bounceFromMiddle = true;

    private final double e1 = 0.0;
    private final double dOrder = 0.0;
    private final double dStop = 0.0;

    public BollingerStrategy(int numOfSTD, int numOfPeriod, boolean bounceFromMiddle) {
        this.numOfSTD = numOfSTD;
        this.numOfPeriod = numOfPeriod;
        this.bounceFromMiddle = bounceFromMiddle;
    }

    public int getNumOfPeriod() {
        return numOfPeriod;
    }


    public BollingerBand createDataPoint(Collection<NPBar> bars, BollingerBand lastPoint, NPBar currentBar) {
        BollingerBand currentPoint = new BollingerBand(currentBar);
        double[] tpArray = getTPArray(bars);
        double SMA = Quant.SMA(tpArray);
        double STD = Quant.STD(tpArray,SMA);
        currentPoint.setBM(Quant.SMA(tpArray));
        currentPoint.setBU(SMA+numOfSTD*STD);
        currentPoint.setBL(SMA-numOfSTD*STD);

        if (lastPoint!=null) {
            currentPoint.setPreOne(lastPoint);
        }
        return currentPoint;
    }

    //return a typical price double array
    public double[] getTPArray(Collection<NPBar> bars) {
        double[] tpArray = new double[bars.size()];
        int index = 0;
        for (NPBar bar: bars) {
            double tp = 0.0;
            tp = bar.getHigh() + bar.getLow() + bar.getClose();
            tpArray[index++] = tp/3;
        }
        return tpArray;
    }

    public boolean isDataCreationReady (int currentIndex) {
        return (currentIndex - numOfPeriod + 1) >= 0;
    }

    public boolean isDataReady (int index) {
        return index > numOfPeriod + 1;
    }

    public boolean isDataReady(BollingerBand current) {
        BollingerBand preOne = current.getPreOne();
        if (preOne == null) return false;
        BollingerBand preTwo = preOne.getPreOne();
        if (preTwo == null) return false;
        BollingerBand preThree = preTwo.getPreOne();
        return preThree != null;
    }

    public boolean checkFill(BollingerBand current) {
        BollingerBand preOne = current.getPreOne();
        BollingerBand preTwo = preOne.getPreOne();
        BollingerBand preThree = preTwo.getPreOne();
        if (preOne==null || preTwo==null ||preThree==null) {
            throw new RuntimeException("BollingerBank Strategy check fill failed");
        }

        //Ascending middle MA,If P(BM,0)>P(BM,-1)>P(BM-2)
        //Prev 3rd or 2nd period downward pierced lower band  && If P(Low, -2) < P(BL,-2) or P(Low, -3) < P(BL,-3)
        //One period closed above lower band since piercing && If P(Close, -1) >= P(BL,-1), or P(Close, -2) >= P(BL,-2)
        //current bar is green or current price is above lower band && If P(Close, 0) >= P(BL,0) or P(Close, 0) > P(Open,0) +e1,
        return (bounceFromMiddle) ?
             (current.getBM() > preOne.getBM() && preOne.getBM() > preTwo.getBM()
                    && (preTwo.getLOW() < preTwo.getBM() || preThree.getLOW() < preThree.getBM())
                    && (preOne.getCLOSE() >= preOne.getBM() || preTwo.getCLOSE() >= preTwo.getBM())
                    && (current.getCLOSE() >= current.getBM() || current.getCLOSE() > current.getOPEN() + e1))
            :
            (current.getBM() > preOne.getBM() && preOne.getBM() > preTwo.getBM()
                && (preTwo.getLOW() < preTwo.getBL() || preThree.getLOW() < preThree.getBL())
                && (preOne.getCLOSE() >= preOne.getBL() || preTwo.getCLOSE() >= preTwo.getBL())
                && (current.getCLOSE() >= current.getBL() || current.getCLOSE() > current.getOPEN() + e1));
    }

    public boolean checkProfit(BollingerBand current) {
        //Exit a long position once the bar exceeds Bollinger Band Upper the 2rd times
        BollingerBand preOne = current.getPreOne();
        BollingerBand preTwo = preOne.getPreOne();
        if (preOne==null || preTwo==null) {
            throw new RuntimeException("BollingerBank Strategy check fill failed");
        }
        return preTwo.getHIGH() > preTwo.getBU() && preOne.getHIGH() > preOne.getBU();
    }

    public boolean checkLoss(BollingerBand current) {
        //Exit at a trailing stop loss
        BollingerBand preOne = current.getPreOne();
        return current.getCLOSE()<preOne.getCLOSE()*(1-dStop);
    }

    //enter a buy order fill
    public Transaction createFill(BollingerBand point) {
        Transaction transaction = new Transaction();
        transaction.setShares(SHARE);
        transaction.setSide(NPOrder.BUY);
        transaction.setPrice(point.getCLOSE());
        transaction.setTime(point.getBar().getTime());
        return transaction;
    }

    public Transaction createLoss(BollingerBand point) {
        Transaction transaction = new Transaction();
        transaction.setShares(SHARE);
        transaction.setSide(NPOrder.SELL);
        transaction.setPrice(point.getCLOSE()+dOrder);
        transaction.setTime(point.getBar().getTime());
        return transaction;
    }

    public Transaction createProfit(BollingerBand point) {
        Transaction transaction = new Transaction();
        transaction.setShares(SHARE);
        transaction.setSide(NPOrder.SELL);
        transaction.setPrice(point.getCLOSE()-dOrder);
        transaction.setTime(point.getBar().getTime());
        return transaction;
    }

    public void setNumOfSTD(int numOfSTD) {
        this.numOfSTD = numOfSTD;
    }

    public void setNumOfPeriod(int numOfPeriod) {
        this.numOfPeriod = numOfPeriod;
    }
}