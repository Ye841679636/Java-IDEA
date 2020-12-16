package com.newpoint.instrument;

import com.ib.client.Contract;
import com.newpoint.adapter.AdapterManager;
import com.newpoint.adapter.SecurityAdapter;
import java.util.HashMap;
import java.util.Map;

public class InstrumentManager {
    //static variable single_instance of type OrderManager
    private final static InstrumentManager instrumentManager = new InstrumentManager();
    private final Map<String,Security> secMap;

    public InstrumentManager() {
        this.secMap = new HashMap<String,Security>();
    }

    public static InstrumentManager getInstance() {
        return instrumentManager;
    }

    public Security getSecurity(String sectID) {
        return secMap.get(sectID);
    }

    public void addSecurity(String sectID, Security security) {
        secMap.put(sectID,security);
    }

    public Security createSecurity(SecurityType securityType, String symbol) {
        SecurityAdapter securityAdapter = AdapterManager.getInstance().getSecurityAdapter(securityType);
        return securityAdapter.createSecurity(symbol);
    }

    public Security createSecurity(Contract contract)  {
        SecurityAdapter securityAdapter = AdapterManager.getInstance().getSecurityAdapter(contract.getSecType());
        Security security = securityAdapter.createSecurity(contract.symbol());
        securityAdapter.convert(contract,security);
        addSecurity(security.getSecID(),security);
        return security;
    }

    public Contract createIBSecurity(Security security)  {
        SecurityAdapter securityAdapter = AdapterManager.getInstance().getSecurityAdapter(security.getSecType().toString());
        Contract contract = securityAdapter.createIBSecurity();
        securityAdapter.convert(security, contract);
        return contract;
    }

    //to be implemented
    public void initialize() {
    }
}
