package com.newpoint.strategy;

import com.newpoint.order.Transaction;

import java.util.ArrayList;
import java.util.List;

public class StrategyResult {
    private BollingerStrategy strategy;
    private List<Transaction> transactions;
    private List<TransactionPair> transactionPairs;

    private int numberOfProfit = 0;
    private int numberOfLoss = 0;
    private double totalPL = 0.0;
    private int winningPercent = 0;

    public int getNumberOfProfit() {
        return numberOfProfit;
    }

    public void setNumberOfProfit(int numberOfProfit) {
        this.numberOfProfit = numberOfProfit;
    }

    public int getNumberOfLoss() {
        return numberOfLoss;
    }

    public void setNumberOfLoss(int numberOfLoss) {
        this.numberOfLoss = numberOfLoss;
    }

    public double getTotalPL() {
        return totalPL;
    }

    public void setTotalPL(double totalPL) {
        this.totalPL = totalPL;
    }

    public int getWinningPercent() {
        return winningPercent;
    }

    public void setWinningPercent(int winningPercent) {
        this.winningPercent = winningPercent;
    }

    public StrategyResult(BollingerStrategy strategy) {
        this.strategy = strategy;
        transactions = new ArrayList<Transaction>();
        transactionPairs = new ArrayList<TransactionPair>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public void addTransactionPair(TransactionPair pair) {
        transactionPairs.add(pair);
    }
    
    public int getTransactionCount() {
        return transactions.size();
    }

    public String toString() {
        final StringBuilder sb = new StringBuilder("StrategyResult{");
        sb.append("numberOfProfit=").append(numberOfProfit);
        sb.append(", numberOfLoss=").append(numberOfLoss);
        sb.append(", totalPL=").append(totalPL);
        sb.append(", winningPercent=").append(winningPercent);
        sb.append('}');
        return sb.toString();
    }

    public void calculateSummary() {
        int size = transactions.size();
        for (int index = 0; index < size;) {
            if (index + 1 < size) {
                Transaction buy = transactions.get(index);
                Transaction sell = transactions.get(index + 1);
                TransactionPair pair = new TransactionPair(buy, sell);
                if (pair.isProfit())
                    numberOfProfit = numberOfProfit + 1;
                else
                    numberOfLoss = numberOfLoss + 1;

                totalPL = totalPL + pair.getPL();
                transactionPairs.add(pair);

                index = index + 2;
            }
        }
        if((numberOfProfit+numberOfLoss) > 0){
            winningPercent = (numberOfProfit * 100)/(numberOfProfit+numberOfLoss);
        }
    }

}
