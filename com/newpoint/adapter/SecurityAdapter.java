package com.newpoint.adapter;

import com.ib.client.Contract;
import com.newpoint.instrument.Security;

//convert a IB contract into a NP security
public class SecurityAdapter {
    public SecurityAdapter() {
    }

    public Security createSecurity(String symbol) {
        throw new RuntimeException("Security creation should be called from subclasses");
    }

    public Contract createIBSecurity() {
        return new Contract();
    }

    //convert IB contract into NP security
    public void convert(Contract contract, Security security) {
        security.setSymbol(contract.symbol());
        security.setExchange(contract.exchange());
        security.setCurrency(contract.currency());
        security.setSecID(String.valueOf(contract.conid()));
        security.setNativeExchange(contract.primaryExch());
    }

    //convert NP security into IB contract
    public void convert(Security security, Object contractObj) {
        Contract contract = (Contract) contractObj;
        contract.secType(security.getSecType().toString());
        contract.symbol(security.getSymbol());
        contract.exchange(security.getExchange());
        contract.currency(security.getCurrency());
        contract.primaryExch(security.getNativeExchange());
    }
}
