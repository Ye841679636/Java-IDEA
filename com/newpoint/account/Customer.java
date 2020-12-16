package com.newpoint.account;

import java.util.*;

public class Customer {
    private String id;
    private String name;
    private final Map<String,Account> accounts = new HashMap<String,Account>();

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return id.equals(customer.id);
    }

    public Customer(String id) {
        this.id = id;
    }

    public int hashCode() {
        return Objects.hash(id);
    }

    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                '}';
    }

    public String getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Account getAccount(String accountNumber) {
        return (Account) accounts.get(accountNumber);
    }

    public void addAccount(Account account) {
        accounts.put(account.getAccountKey(),account);
    }

    public Collection<Account> getAccounts() {
        return accounts.values();
    }
}
