package com.newpoint.adapter;

import com.ib.client.Contract;
import com.newpoint.instrument.Security;
import com.newpoint.instrument.Stock;

public class StockAdaptor extends SecurityAdapter {
    private static final StockAdaptor secAdapter= new StockAdaptor();

    //convert a IB contract into a NP security
    public Security createSecurity(String symbol)  {
        return new Stock(symbol);
    }

    public void convert(Contract contract, Security security) {
        super.convert(contract, security);
    }

    public static SecurityAdapter getInstance() {
        return secAdapter;
    }
}
