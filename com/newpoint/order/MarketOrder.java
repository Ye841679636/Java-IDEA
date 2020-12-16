package com.newpoint.order;

import com.newpoint.account.Account;
import com.newpoint.instrument.Security;

public class MarketOrder extends NPOrder {

    public MarketOrder(Account account, Security security, double quantity, String side) {
        super(security, quantity, side);
        orderType = OrderType.MKT;
    }
}
