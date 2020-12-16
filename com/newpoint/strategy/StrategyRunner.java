package com.newpoint.strategy;

import com.newpoint.marketdata.MarketDataBlock;
import com.newpoint.marketdata.NPBar;

import java.util.Collection;

public class StrategyRunner {
    MarketDataBlock marketData;
    StrategyResult result;
    BollingerStrategy strategy;

    BollingerBand currentPoint;
    StrategyStatus status;

    public StrategyRunner(BollingerStrategy strategy) {
        this.strategy = strategy;
        this.status = StrategyStatus.DATA_NOT_READY;
        this.result = new StrategyResult(strategy);
    }

    public MarketDataBlock getMarketData() {
        return marketData;
    }

    public void setMarketData(MarketDataBlock marketData) {
        this.marketData = marketData;
    }

    public StrategyResult getResult() {
        return result;
    }

    public void setResult(StrategyResult result) {
        this.result = result;
    }

    public StrategyResult runStrategy() {
        for (int index = 0; index < marketData.getDataSize(); index++) {
            runStrategyPoint(index);
        }

        return result;
    }

    private void runStrategyPoint(int currentIndex) {
        if (strategy.isDataCreationReady(currentIndex)) {
            int startIndex = currentIndex - strategy.getNumOfPeriod() + 1;
            Collection<NPBar> bars = marketData.getBars(startIndex,currentIndex);
            NPBar currentBar = marketData.getBarAt(currentIndex);
            currentPoint = strategy.createDataPoint(bars, currentPoint, currentBar);
        }
        switch (status) {
            case DATA_NOT_READY:
                if (strategy.isDataReady(currentIndex)) {
                    status = StrategyStatus.DATA_IS_READY;
                }
                break;
            case DATA_IS_READY:
            case PROFIT_ORDER_FILLED:
            case LOSS_ORDER_FILLED:
                if (strategy.checkFill(currentPoint)) {
                    result.addTransaction(strategy.createFill(currentPoint));
                    status = StrategyStatus.ENTRY_ORDER_FILLED;
                }
                break;
            case ENTRY_ORDER_FILLED:
                if (strategy.checkProfit(currentPoint)) {
                    result.addTransaction(strategy.createProfit(currentPoint));
                    status = StrategyStatus.PROFIT_ORDER_FILLED;
                    break;
                }
                if (strategy.checkLoss(currentPoint)) {
                    result.addTransaction(strategy.createLoss(currentPoint));
                    status = StrategyStatus.LOSS_ORDER_FILLED;
                    break;
                }
                break;
        }
    }
}
