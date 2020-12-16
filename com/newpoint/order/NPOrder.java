package com.newpoint.order;
import com.newpoint.account.Account;
import com.newpoint.instrument.Security;

import java.util.*;

public class NPOrder {
    public static String BUY = "BUY";
    public static String SELL = "SELL";
    protected OrderType orderType;

    private int orderNumber;        // in IB, it is orderId, which is the same as nextOrderId
    private String orderID;         // in IB, it is permId, persistent between sessions
    private Account account;
    private Security security;
    private TimeInForce timeInForce;

    private double quantity;
    private String side;
    private boolean allOrNone;      //Indicates whether or not all the order has to be filled on a single execution.
    private double minQuantity;     //Identifies a minimum quantity order type

    private NPOrder parent;
    private Set<NPOrder> children;

    private NPOrderStatus status;
    private List<Transaction> transactions;
    private int INITIAL_CHILDREN_SIZE = 2;

    public NPOrder() {
        this.timeInForce = TimeInForce.DAY;     //default
        this.children = new HashSet<>(INITIAL_CHILDREN_SIZE);
    }

    public NPOrder(Security security, double quantity, String side) {
        this.security = security;
        this.quantity = quantity;
        this.side = side;
        this.timeInForce = TimeInForce.DAY;
        this.children = new HashSet<>(INITIAL_CHILDREN_SIZE);
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NPOrder)) return false;
        NPOrder IBOrder = (NPOrder) o;
        if (orderNumber == 0 && IBOrder.orderNumber == 0)
            return super.equals(o);
        else
            return orderNumber == IBOrder.orderNumber;
    }

    public int hashCode() {
        return Objects.hash(orderNumber);
    }

    public void setAttribute(String side, double quantity)
    {
        this.side = side;
        this.quantity = quantity;
    }

    public boolean hasChildren() {
        return children != null && !children.isEmpty();
    }

    public NPOrder getParent() {
        return parent;
    }

    public void setParent(NPOrder parent) {
        this.parent = parent;
    }

    public void addChild(NPOrder order) {
        children.add(order);
    }

    public Set<NPOrder> getChildren() {
        return children;
    }

    public void setChildren(Set<NPOrder> children) {
        this.children = children;
    }

    public String toString() {
        return "NPOrder{" +
                "orderType=" + orderType +
                ", orderNumber=" + orderNumber +
                ", security=" + security.symbol +
                ", side='" + side + '\'' +
                '}';
    }

    public void setAttribute(String side, double quantity, double stopPrice )
    {
        throw new RuntimeException("BaseOrder setAttribute method should be invoked from subclasses");
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public void setOrderType(OrderType orderType) {
        this.orderType = orderType;
    }

    public TimeInForce getTimeInForce() {
        return timeInForce;
    }

    public void setTimeInForce(TimeInForce timeInForce) {
        this.timeInForce = timeInForce;
    }


    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public boolean isAllOrNone() {
        return allOrNone;
    }

    public void setAllOrNone(boolean allOrNone) {
        this.allOrNone = allOrNone;
    }

    public double getMinQuantity() {
        return minQuantity;
    }

    public void setMinQuantity(double minQuantity) {
        this.minQuantity = minQuantity;
    }

    public NPOrderStatus getStatus() {
        return status;
    }

    public void setStatus(NPOrderStatus status) {
        this.status = status;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public boolean isComboOrder() {return false;}

}
