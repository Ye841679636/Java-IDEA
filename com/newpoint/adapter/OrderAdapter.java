package com.newpoint.adapter;

import com.ib.client.Order;
import com.newpoint.account.Account;
import com.newpoint.account.AccountManager;
import com.newpoint.order.NPOrder;
import com.newpoint.order.TimeInForce;

import java.util.List;

public class OrderAdapter {
    public OrderAdapter() {
    }

    public NPOrder createOrderFrom(Order ibOrder) {
        throw new RuntimeException("NP order creation should be created from subclasses");
    }

    public NPOrder createOrder() {
        throw new RuntimeException("NP order creation should be created from subclasses");
    }

    public List<Order> createIBOrdersFrom(NPOrder comboOrder) {
        throw new RuntimeException("NP order creation should be created from subclasses");
    }

    public Order createIBOrderFrom(NPOrder npOrder) {
        throw new RuntimeException("NP order creation should be created from subclasses");
    }

    //convert an IB Order into a NP limit order
    public void convert(NPOrder npOrder, Order order) {
        order.orderType(npOrder.getOrderType().toString());
        order.orderId(npOrder.getOrderNumber());
        if (npOrder.getAccount() != null)
            order.account(npOrder.getAccount().getAccountKey());
        order.action(npOrder.getSide());
        order.totalQuantity(npOrder.getQuantity());
    }

    //convert an NP order into IB order
    public void convert(Order order, NPOrder npOrder) {
        //IB order's permId (persistent across sessions) is the unique order for NP order
        npOrder.setOrderID(String.valueOf(order.permId()));
        npOrder.setOrderNumber(order.orderId());
        npOrder.setQuantity(order.totalQuantity());
        npOrder.setSide(order.getAction());
        npOrder.setTimeInForce(TimeInForce.valueOf(order.tif().getApiString()));

        String accountNumber = order.account();
        if (accountNumber!= null) {
            Account account = AccountManager.getInstance().getAccount(accountNumber);
            if (account == null) {
                account = AccountManager.getInstance().createAccount(accountNumber);
            }
            npOrder.setAccount(account);
        }
    }
}
