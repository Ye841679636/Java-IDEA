package com.newpoint.order;

import com.newpoint.instrument.Security;

import java.util.HashSet;
import java.util.Set;

public class ComboOrder extends NPOrder {
    //Children orders, likely Once Cancel All (OCA) orders
    private NPOrder entryOrder;
    private Set<NPOrder> children;
    private final int COMBO_SIZE = 2;

    public ComboOrder() {
        super();
        this.orderType = OrderType.COMBO;
        this.children = new HashSet<>(COMBO_SIZE);
    }

    public ComboOrder(Security security, String action, double quantity, double limitPrice, double takeProfitLimitPrice, double stopLossPrice) {
        super();
        this.setSecurity(security);
        this.orderType = OrderType.COMBO;
        this.children = new HashSet<>(COMBO_SIZE);

        this.entryOrder = new LimitOrder();
        this.entryOrder.setSecurity(security);
        this.entryOrder.setAttribute(action, quantity,limitPrice);
        this.entryOrder.setParent(this);

        NPOrder profitOrder = new LimitOrder();
        profitOrder.setSecurity(security);
        profitOrder.setAttribute(action.equals(NPOrder.BUY)? NPOrder.SELL: NPOrder.BUY, quantity,takeProfitLimitPrice);
        profitOrder.setParent(entryOrder);
        this.children.add(profitOrder);

        NPOrder lossOrder = new StopOrder();
        lossOrder.setSecurity(security);
        lossOrder.setAttribute(action.equals(NPOrder.BUY)? NPOrder.SELL: NPOrder.BUY, quantity,stopLossPrice);
        lossOrder.setParent(entryOrder);
        this.children.add(lossOrder);
    }

    public NPOrder getProfitOrder() {
        for (NPOrder order: children) {
            if (order.getOrderType().equals(OrderType.LMT))
                return order;
        }
        return null;
    }

    public NPOrder getLossOrder() {
        for (NPOrder order: children) {
            if (order.getOrderType().equals(OrderType.STP))
                return order;
        }
        return null;
    }

    public boolean isComboOrder() {
        return true;
    }

    public String toString() {
        return "ComboOrder{" +
                "entryOrder=" + entryOrder +
                "} " + entryOrder.toString();
    }

    public NPOrder getEntryOrder() {
        return entryOrder;
    }

    public void setEntryOrder(NPOrder order) {
        this.entryOrder = order;
   }
    public Set<NPOrder> getChildren() {
        return children;
    }

    public void addChild(NPOrder order) {
        children.add(order);
    }

    public void setChildren(Set<NPOrder> children) {
        this.children = children;
    }
}
