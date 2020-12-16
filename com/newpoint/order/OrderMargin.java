package com.newpoint.order;

public class OrderMargin {
    private double itMarginBefore;          //The account's current initial margin.
    private double maintMarginBefore;       //The account's current maintenance margin.
    private double equityWithLoanBefore;    //The account's current equity with loan.
    private double initMarginChange;        //The change of the account's initial margin.
    private double maintMarginChange;       //The change of the account's maintenance margin.
    private double equityWithLoanChange;    //The change of the account's equity with loan.
    private double initMarginAfter;         //order's impact on the account's initial margin.
    private double maintMarginAfter;        //The order's impact on the account's maintenance margin.
    private double equityWithLoanAfter;     //Shows the impact the order would have on the account's equity with loan.
}
