package com.newpoint.adapter;

import com.newpoint.instrument.SecurityType;
import com.newpoint.order.OrderType;

public class AdapterManager {
    private static final AdapterManager adapterManager = new AdapterManager();

    public SecurityAdapter getSecurityAdapter(String secType){
        return getSecurityAdapter(SecurityType.valueOf(secType));

    }
    public SecurityAdapter getSecurityAdapter(SecurityType secType) {
        switch (secType) {
            case STK:
                return StockAdaptor.getInstance();
            case OPT:
                return OptionAdapter.getInstance();
        }
        throw new RuntimeException("No SecurityAdapter for "+ secType + " Found in AdapterManager");
    }
    public OrderAdapter getOrderAdapter(OrderType orderType) {
        switch (orderType) {
            case LMT:
                return LimitOrderAdapter.getInstance();
            case STP:
                return StopOrderAdapter.getInstance();
            case COMBO:
                return ComboOrderAdaptor.getInstance();
        }
        throw new RuntimeException("No OrderAdapter for " + orderType + " Found in AdapterManager");
    }

    public OrderAdapter getOrderAdapter(String orderType) {
        return getOrderAdapter(OrderType.valueOf(orderType));
    }

    public static AdapterManager getInstance() {
        return adapterManager;
    }
}
