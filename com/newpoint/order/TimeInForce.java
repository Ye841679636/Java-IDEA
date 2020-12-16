package com.newpoint.order;

public enum TimeInForce {
    DAY, //Valid for the day only.
    GTC, //Good until canceled.
    IOC, //Immediate or Cancel.
    GTD, //Good until a date
    OPG, //Use OPG to send a market-on-open (MOO) or limit-on-open (LOO) order.
    FOK, //If the entire Fill-or-Kill order does not execute as soon as it becomes available, the entire order is canceled.
    DTC  //Day until Canceled
}
