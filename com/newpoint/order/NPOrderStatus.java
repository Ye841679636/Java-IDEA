package com.newpoint.order;

public class NPOrderStatus {
    private enum Status {
        ApiPending,     //indicates order has not yet been sent to IB server, for instance if there is a delay in receiving the security definition. Uncommonly received.
        PendingSubmit,  //indicates the order was sent from TWS, but confirmation has not been received that it has been received by the destination. Most commonly because exchange is closed.
        PendingCancel,  //indicates that a request has been sent to cancel an order but confirmation has not been received of its cancellation.
        PreSubmitted,   //indicates that a simulated order type has been accepted by the IB system and that this order has yet to be elected. The order is held in the IB system until the election criteria are met. At that time the order is transmitted to the order destination as specified.
        Submitted,      //indicates that your order has been accepted at the order destination and is working.
        ApiCancelled,   //after an order has been submitted and before it has been acknowledged, an API client can request its cancellation, producing this state.
        Cancelled,      //indicates that the balance of your order has been confirmed cancelled by the IB system. This could occur unexpectedly when IB or the destination has rejected your order. For example, if your order is subject to price checks, it could be cancelled, as explained in Order Placement Considerations
        Filled,         //indicates that the order has been completely filled.
        Inactive        //indicates an order is not working, possible reasons include:
        //it is invalid or triggered an error. A corresponding error code is expected to the error() function.
        //This error may be a reject, for example a regulatory size reject. See Order Placement Considerations
        //the order is to short shares but the order is being held while shares are being located.
        //an order is placed manually in TWS while the exchange is closed.
        //an order is blocked by TWS due to a precautionary setting and appears there in an untransmitted state
    }
    private NPOrder order;
    private Status status;
    private double filled;
    private double remaining;
    private double averageFillPrice;
    private double lastFillPrice;
    private String reason;      //the reason for various status, especially held orders

    public NPOrderStatus(String statusString) {
        status = Status.valueOf(statusString);
    }

    public NPOrder getOrder() {
        return order;
    }

    public void setOrder(NPOrder order) {
        this.order = order;
    }

    public boolean isCanceled() {
        return status.equals(Status.Cancelled);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public double getFilled() {
        return filled;
    }

    public void setFilled(double filled) {
        this.filled = filled;
    }

    public double getRemaining() {
        return remaining;
    }

    public void setRemaining(double remaining) {
        this.remaining = remaining;
    }

    public double getAverageFillPrice() {
        return averageFillPrice;
    }

    public void setAverageFillPrice(double averageFillPrice) {
        this.averageFillPrice = averageFillPrice;
    }

    public double getLastFillPrice() {
        return lastFillPrice;
    }

    public void setLastFillPrice(double lastFillPrice) {
        this.lastFillPrice = lastFillPrice;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public boolean isFilled() {
        return status.equals(Status.Filled);
    }

}
