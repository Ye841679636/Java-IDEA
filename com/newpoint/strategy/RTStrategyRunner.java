package com.newpoint.strategy;

import com.newpoint.instrument.Security;
import com.newpoint.marketdata.*;
import com.newpoint.order.NPOrder;
import com.newpoint.order.OrderManager;
import com.newpoint.order.OrderType;
import com.newpoint.util.LockManager;

import java.util.concurrent.BlockingDeque;

public class RTStrategyRunner {
    private final StrategyResult result;
    private final BollingerStrategy strategy;
    private final Security security;
    private final String barSize;
    private final int shares;
    private final Object orderFillLock;

    BollingerBand currentPoint;
    StrategyStatus status;

    public RTStrategyRunner(BollingerStrategy strategy, Security security, String barSize, int shares) {
        this.strategy = strategy;
        this.security = security;
        this.barSize = barSize;
        this.status = StrategyStatus.DATA_NOT_READY;
        this.shares = shares;
        this.result = new StrategyResult(strategy);
        this.orderFillLock = LockManager.getInstance().getOrderFillLock();
    }

    public void run() throws InterruptedException {
        RTMarketDataProvider dataProvider = RTMarketDataProvider.getInstance();
        OrderManager orderManager =  OrderManager.getInstance();

        int periods = strategy.getNumOfPeriod();
        dataProvider.reqRTBar(security, barSize, periods);
        MarketDataKey dataKey = new MarketDataKey(security, barSize);
        RTBarCache dataCache = dataProvider.getBarCache(dataKey);
        BlockingDeque<NPBar> barQueue = dataCache.getBarQueue();
        while (true) {
            while (barQueue.size() == periods) {
                Tick tick;
                NPOrder entryOrder = null;
                NPOrder profitOrder = null;
                NPOrder lossOrder = null;

                NPBar currentBar = barQueue.getLast();
                currentPoint = strategy.createDataPoint(barQueue, currentPoint, currentBar);
                switch (status) {
                    case DATA_NOT_READY:
                        if (strategy.isDataReady(currentPoint)) {
                            status = StrategyStatus.DATA_IS_READY;
                        }
                        break;
                    case DATA_IS_READY:
                    case PROFIT_ORDER_FILLED:
                    case LOSS_ORDER_FILLED:
                        if (strategy.checkFill(currentPoint)) { 
                            tick = dataProvider.getTick(security);
                            double limitPrice = tick.getBidPrice();
                            entryOrder = orderManager.createOrder(security, OrderType.LMT, NPOrder.BUY, shares, limitPrice);
                            orderManager.placeOrder(entryOrder);
                            status = StrategyStatus.ENTRY_ORDER_PLACED;
                        }
                        break;
                    case ENTRY_ORDER_PLACED:
                        synchronized (orderFillLock) {
                            //whenever the thread is notified starts again from the loop
                            while (!(entryOrder.getStatus() != null && !entryOrder.getStatus().isFilled())) {
                                System.out.println("Waiting for Entry Order to fill..........");
                                orderFillLock.wait();  // wait() is also true
                            }
                            status = StrategyStatus.ENTRY_ORDER_FILLED;
                            result.addTransaction(orderManager.createTransaction(entryOrder));
                            System.out.println("Entry Order filled: " + entryOrder.toString());
                        }
                        break;
                    case ENTRY_ORDER_FILLED:
                        if (strategy.checkProfit(currentPoint)) {
                            tick = dataProvider.getTick(security);
                            double limitPrice = tick.getAskPrice();
                            String side = entryOrder.getSide().equals(NPOrder.BUY)? NPOrder.SELL: NPOrder.BUY;
                            profitOrder = orderManager.createOrder(security, OrderType.LMT, side, shares, limitPrice);
                            orderManager.placeOrder(profitOrder);
                            status = StrategyStatus.PROFIT_ORDER_PLACED;
                            break;
                        }
                        if (strategy.checkLoss(currentPoint)) {
                            tick = dataProvider.getTick(security);
                            double limitPrice = tick.getAskPrice();
                            String side = entryOrder.getSide().equals(NPOrder.BUY)? NPOrder.SELL: NPOrder.BUY;
                            profitOrder = orderManager.createOrder(security, OrderType.LMT, side, shares, limitPrice);
                            orderManager.placeOrder(profitOrder);
                            status = StrategyStatus.LOSS_ORDER_PLACED;
                            status = StrategyStatus.LOSS_ORDER_FILLED;
                            break;
                        }
                        break;
                    case PROFIT_ORDER_PLACED:
                        synchronized (orderFillLock) {
                            //whenever the thread is notified starts again from the loop
                            while (profitOrder.getStatus() != null && !profitOrder.getStatus().isFilled()) {
                                System.out.println("Waiting for Profit Order to fill..........");
                                orderFillLock.wait();  // wait() is also true
                            }
                            status = StrategyStatus.PROFIT_ORDER_FILLED;
                            result.addTransaction(orderManager.createTransaction(profitOrder));
                            System.out.println("Profit Order filled: " + profitOrder.toString());
                        }
                        break;
                    case LOSS_ORDER_PLACED:
                        synchronized (orderFillLock) {
                            //whenever the thread is notified starts again from the loop
                            while (lossOrder.getStatus() != null && !lossOrder.getStatus().isFilled()) {
                                System.out.println("Waiting for Loss Order to fill..........");
                                orderFillLock.wait();  // wait() is also true
                            }
                            status = StrategyStatus.LOSS_ORDER_FILLED;
                            result.addTransaction(orderManager.createTransaction(lossOrder));
                            System.out.println("Loss Order filled: " + lossOrder.toString());
                        }
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + status);
                }
                try {
                    barQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
