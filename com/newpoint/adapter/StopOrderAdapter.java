package com.newpoint.adapter;

import com.ib.client.Order;
import com.newpoint.order.NPOrder;
import com.newpoint.order.StopOrder;

public class StopOrderAdapter extends OrderAdapter {
    private static final StopOrderAdapter stopOrderAdapter = new StopOrderAdapter();

    public NPOrder createOrder(){
        return new StopOrder();
    }

    public Order createIBOrderFrom(NPOrder npOrder) {
        Order ibOrder = new Order();
        convert(npOrder, ibOrder);
        return ibOrder;
    }

    public NPOrder createOrderFrom(Order ibOrder) {
        NPOrder npOrder = new StopOrder();
        convert(ibOrder, npOrder);
        return npOrder;
    }

    //convert a IB Order into a NP stop order
    public void convert(NPOrder npOrder, Order order) {
        super.convert(npOrder, order);
        StopOrder stopOrder = (StopOrder) npOrder;
        order.auxPrice(stopOrder.getStopPrice());
    }

    //convert a NP stop orderOrder into an IB order
    public void convert(Order order, NPOrder npOrder) {
        super.convert(order, npOrder);
        ((StopOrder) npOrder).setStopPrice(order.auxPrice());
    }

    public static OrderAdapter getInstance () {
        return stopOrderAdapter;
    }
}
