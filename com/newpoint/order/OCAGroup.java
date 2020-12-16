package com.newpoint.order;

import java.util.HashSet;
import java.util.Set;

public class OCAGroup {
    private Set<NPOrder> orders;
    private String groupID;

    public OCAGroup(String groupID) {
        this.groupID = groupID;
        this.orders = new HashSet<>();
    }
}
