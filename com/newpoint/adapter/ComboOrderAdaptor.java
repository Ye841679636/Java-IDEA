package com.newpoint.adapter;

import com.ib.client.Order;
import com.newpoint.order.NPOrder;
import com.newpoint.order.ComboOrder;

import java.util.ArrayList;
import java.util.List;

public class ComboOrderAdaptor extends OrderAdapter {
    private static final ComboOrderAdaptor orderAdaptor = new ComboOrderAdaptor();

    public static ComboOrderAdaptor getInstance () {
        return orderAdaptor;
    }

    public ComboOrder createOrder() {
        return new ComboOrder();
    }

    public List<Order> createIBOrdersFrom(NPOrder comboOrder) {
        List<Order> ibOrders = new ArrayList<>();

        ComboOrder plOrder = (ComboOrder) comboOrder;
        //Convert buy order
        NPOrder buyOrder = plOrder.getEntryOrder();
        OrderAdapter orderAdapter = AdapterManager.getInstance().getOrderAdapter(buyOrder.getOrderType());
        Order ibBuyOrder = orderAdapter.createIBOrderFrom(buyOrder);
        ibBuyOrder.transmit(false);
        ibOrders.add(ibBuyOrder);

        //convert take profit order
        NPOrder profitOrder = plOrder.getProfitOrder();
        orderAdapter = AdapterManager.getInstance().getOrderAdapter(profitOrder.getOrderType());
        Order ibProfitOrder = orderAdapter.createIBOrderFrom(profitOrder);
        ibProfitOrder.transmit(false);
        ibProfitOrder.parentId(buyOrder.getOrderNumber());
        ibOrders.add(ibProfitOrder);

        //convert stop loss order
        NPOrder lossOrder = plOrder.getLossOrder();
        orderAdapter = AdapterManager.getInstance().getOrderAdapter(lossOrder.getOrderType());
        Order ibLossOrder = orderAdapter.createIBOrderFrom(lossOrder);
        ibLossOrder.parentId(buyOrder.getOrderNumber());
        ibProfitOrder.transmit(true);
        ibOrders.add(ibLossOrder);
        return ibOrders;
    }
}
