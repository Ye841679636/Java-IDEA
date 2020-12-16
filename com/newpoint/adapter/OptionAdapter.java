package com.newpoint.adapter;

import com.ib.client.Contract;
import com.newpoint.instrument.Option;
import com.newpoint.instrument.Security;

public class OptionAdapter extends SecurityAdapter {
    private static final OptionAdapter optionAdapter = new OptionAdapter();

    //convert a IB contract into a NP security
    public Security createSecurity(String symbol)  {
        return new Option(symbol);
    }

    //convert a IB contract into a NP security
    public void convert(Contract contract, Security security) {
        super.convert(contract,security);
        Option option = (Option) security;
        option.setStrikePrice(contract.strike());
        option.setMultiplier(Integer.parseInt(contract.multiplier()));
        option.setRight(contract.right().getApiString());
        option.setLastTradeDateOrContractMonth(contract.lastTradeDateOrContractMonth());
    }

    public static SecurityAdapter getInstance() {
        return optionAdapter;
    }
}
