package com.newpoint.order;

import com.ib.client.Contract;
import com.ib.client.Order;
import com.newpoint.adapter.AdapterManager;
import com.newpoint.adapter.ComboOrderAdaptor;
import com.newpoint.adapter.OrderAdapter;
import com.newpoint.instrument.InstrumentManager;
import com.newpoint.instrument.Security;
import com.newpoint.util.LockManager;
import com.newpoint.workstation.NPTradeSession;
import com.newpoint.workstation.NPTradeStation;

import java.util.*;

// Java program implementing Singleton class
// with getInstance() method
public class OrderManager
{
    // static variable single_instance of type OrderManager
    private static final OrderManager orderManager = new OrderManager();
    private final Map<Integer, NPOrder> ordersByNumber;
    private final Object orderLock;
    private int nextOrderNumber = 0;        //this is IB specific
    private int nextComboOrderNumber = -1;  //this is NP specific

    // private constructor restricted to this class itself
    private OrderManager()
    {
        this.ordersByNumber = new Hashtable<>();
        this.orderLock = LockManager.getInstance().getOrderLock();
    }

    // static method to create instance of OrderManager class
    public static OrderManager getInstance()
    {
        return orderManager;
    }

    public NPOrder getOrderByNumber(Integer orderNumber) {
        return ordersByNumber.get(orderNumber);
    }

    public Collection<NPOrder> getAllOrders() {
        Set<NPOrder> orders = new HashSet<>(ordersByNumber.size());
        synchronized (orderLock) {
            orders.addAll(ordersByNumber.values());
        }
        orders.removeIf(order->order.getParent()!=null);
        return orders;
    }

    public void placeOrder(NPOrder npOrder) {
       NPTradeSession session = NPTradeStation.getSession();
       //create and convert NP security into IB contract
       InstrumentManager instrumentManager = InstrumentManager.getInstance();

       synchronized (orderLock) {
           Contract contract = instrumentManager.createIBSecurity(npOrder.getSecurity());
           //create and convert NP order into IB order
           if (npOrder.isComboOrder()) {
               ComboOrder combo = (ComboOrder) npOrder;
               combo.getEntryOrder().setOrderNumber(getNextOrderNumber());
               for (NPOrder order : combo.getChildren()) {
                   order.setOrderNumber(getNextOrderNumber());
               }
               List<Order> ibOrders = createIBOrders(npOrder);
               for (Order ibOrder : ibOrders) {
                   session.getClient().placeOrder(ibOrder.orderId(), contract, ibOrder);
               }
           } else {
               npOrder.setOrderNumber(getNextOrderNumber());
               Order ibOrder = createIBOrder(npOrder);
               //call session to create order in TWS
               session.getClient().placeOrder(ibOrder.orderId(), contract, ibOrder);
           }
       }
    }

    public Transaction createTransaction(NPOrder order) {
        Transaction transaction = new Transaction();
        transaction.setShares(order.getQuantity());
        transaction.setSide(order.getSide());
        transaction.setPrice(order.getStatus().getAverageFillPrice());
        return transaction;
    }

    public void cancelAllOpenOrders(NPTradeSession session) {
         synchronized (orderLock) {
             for (NPOrder IBOrder : ordersByNumber.values()) {
                 cancelOrder(session, IBOrder);
             }
         }
    }

    public void cancelOrder(NPTradeSession session, NPOrder npOrder) {
        synchronized (orderLock) {
            if (npOrder.isComboOrder()) {
                cancelComboOrder(session, (ComboOrder) npOrder);
            } else {
                cancelSingleOrder(session, npOrder);
            }
        }
    }

    private void cancelComboOrder(NPTradeSession session, ComboOrder comboOrder) {
        cancelSingleOrder(session, comboOrder.getEntryOrder());
        for (NPOrder childOrder: comboOrder.getChildren()) {
            cancelSingleOrder(session, childOrder);
        }
    }

    private void cancelSingleOrder(NPTradeSession session, NPOrder npOrder)
    {
        assert (npOrder.getStatus() != null);
        if (!npOrder.getStatus().isCanceled())
            session.getClient().cancelOrder(npOrder.getOrderNumber());
    }

    public NPOrder createOrder(Security security, OrderType orderType, String side, double quantity, double limitPrice) {
        OrderAdapter orderAdapter = AdapterManager.getInstance().getOrderAdapter(orderType);
        NPOrder order = orderAdapter.createOrder();
        order.setSecurity(security);
        order.setAttribute(side, quantity, limitPrice);
        return order;
    }

    private Order createIBOrder(NPOrder npOrder) {
        OrderAdapter orderAdapter = AdapterManager.getInstance().getOrderAdapter(npOrder.getOrderType());
        return orderAdapter.createIBOrderFrom(npOrder);
    }

    private List<Order> createIBOrders(NPOrder npOrder) {
        OrderAdapter orderAdapter = AdapterManager.getInstance().getOrderAdapter(npOrder.getOrderType());
        return orderAdapter.createIBOrdersFrom(npOrder);
    }

    public NPOrder createOrder(Order ibOrder) {
        synchronized (orderLock) {
            OrderAdapter orderAdapter = AdapterManager.getInstance().getOrderAdapter(ibOrder.getOrderType());
            NPOrder npOrder = orderAdapter.createOrderFrom(ibOrder);
            putOrderByNumber(npOrder.getOrderNumber(), npOrder);
            return npOrder;
        }
    }

    public ComboOrder createComboOrder() {
        synchronized (orderLock) {
            ComboOrderAdaptor comboAdapter = ComboOrderAdaptor.getInstance();
            ComboOrder comboOrder = comboAdapter.createOrder();
            comboOrder.setOrderNumber(getNextComboOrderNumber());
            putOrderByNumber(comboOrder.getOrderNumber(), comboOrder);
            return comboOrder;
        }
    }

    private void putOrderByNumber(Integer orderNumber, NPOrder order) {
        ordersByNumber.put(orderNumber, order);
    }

    // return a combo order whose entry order's number is the argument
    public ComboOrder getComboOrder(int orderNumber) {
        for (NPOrder order: ordersByNumber.values()) {
            if (order.isComboOrder()) {
                ComboOrder combo = (ComboOrder) order;
                if (combo.getEntryOrder() != null && combo.getEntryOrder().getOrderNumber()==orderNumber)
                    return combo;
            }
        }
        return null;
    }

    public synchronized int getNextOrderNumber() {
        return nextOrderNumber++;
    }

    public synchronized void setNextOrderNumber(int nextOrderNumber) {
        this.nextOrderNumber = nextOrderNumber;
    }

    public synchronized int getNextComboOrderNumber() {
        return nextComboOrderNumber--;
    }

    //to be implemented
    public void initialize() {
    }
}
