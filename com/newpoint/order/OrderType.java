package com.newpoint.order;

public enum OrderType  {
    // Combo Order - Simple Profit and Loss
    COMBO( "COMBO"),
    // Bracket Order - Simple Profit and Loss
    BRACKET( "BRACKET"),
    //A Market order is an order to buy or sell at the market bid or offer price. A market order may increase the likelihood of a fill and the speed of execution,
    // but unlike the Limit order a Market order provides no price protection and may fill at a price far lower/higher than the current displayed bid/ask.
    MKT( "MKT"),

    //A Limit order is an order to buy or sell at a specified price or better. The Limit order ensures that if the order fills,
    //it will not fill at a price less favorable than your limit price, but it does not guarantee a fill.
    LMT( "LMT"),

    //A Stop order is an instruction to submit a buy or sell market order if and when the user-specified stop trigger price is attained or penetrated.
    // A Stop order is not guaranteed a specific execution price and may execute significantly away from its stop price.
    // A Sell Stop order is always placed below the current market price and is typically used to limit a loss or protect a profit on a long stock position.
    // A Buy Stop order is always placed above the current market price. It is typically used to limit a loss or help protect a profit on a short sale.
    STP( "STP"),

    //A Stop-Limit order is an instruction to submit a buy or sell limit order when the user-specified stop trigger price is attained or penetrated.
    // The order has two basic components: the stop price and the limit price. When a trade has occurred at or through the stop price,
    // the order becomes executable and enters the market as a limit order, which is an order to buy or sell at a specified price or better.
    STP_LMT( "STP LMT"),

    //A Limit-on-close (LOC) order will be submitted at the close and will execute if the closing price is at or better than the submitted limit price.
    LOC( "LOC"),

    //A Market-to-Limit (MTL) order is submitted as a market order to execute at the current best market price.
    // If the order is only partially filled, the remainder of the order is canceled and re-submitted as a limit order with the limit price equal to the price at which the filled portion of the order executed.
    MTL( "MTL");

    private String orderType;

    OrderType(String type) {
        this.orderType = type;
    }

    @Override public String toString() {
        return orderType;
    }
}
