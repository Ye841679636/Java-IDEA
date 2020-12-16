package com.newpoint.adapter;

import com.ib.client.Order;
import com.newpoint.order.NPOrder;
import com.newpoint.order.LimitOrder;

public class LimitOrderAdapter extends OrderAdapter {
    private static final LimitOrderAdapter limitOrderAdapter = new LimitOrderAdapter();

    public NPOrder createOrder(){
        return new LimitOrder();
    }

    public Order createIBOrderFrom(NPOrder npOrder) {
        Order ibOrder = new Order();
        convert(npOrder, ibOrder);
        return ibOrder;
    }

    public NPOrder createOrderFrom(Order ibOrder) {
        NPOrder npOrder = new LimitOrder();
        convert(ibOrder, npOrder);
        return npOrder;
    }

    //convert a IB Order into a NP limit order
    public void convert(NPOrder npOrder, Order order) {
        super.convert(npOrder, order);
        LimitOrder limitOrder = (LimitOrder) npOrder;
        order.lmtPrice(limitOrder.getLimitPrice());
    }

    //convert a NP limit orderOrder into an IB order
    public void convert(Order order, NPOrder npOrder) {
        super.convert(order, npOrder);
        ((LimitOrder) npOrder).setLimitPrice(order.lmtPrice());
    }

    public static OrderAdapter getInstance () {
        return limitOrderAdapter;
    }
}
