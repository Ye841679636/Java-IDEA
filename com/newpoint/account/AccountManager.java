package com.newpoint.account;

import com.newpoint.util.LockManager;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

// Java program implementing Singleton class
// with getInstance() method
public class AccountManager {
     // static variable single_instance of type OrderManager
    private final static AccountManager accountManager = new AccountManager();
    private Map<String, Account> accounts = null;
    private final Object accountLock;

    // private constructor restricted to this class itself
    private AccountManager()
    {
        accounts = new HashMap<>();
        accountLock = LockManager.getInstance().getAccountLock();
    }

    // static method to return instance of OrderManager class
    public static AccountManager getInstance()
    {
        return accountManager;
    }

    //do nothing for now
    public void initialize() {
    }

    public Account getAccount(String accountNumber) {
        synchronized (accountLock) {
            return accounts.get(accountNumber);
        }
    }

    public Account createAccount(String acctKey) {
        synchronized (accountLock) {
            Account account = new Account(acctKey);
            accounts.put(acctKey, account);
            return account;
        }
    }

    public Collection<Account> getAccounts() {
        synchronized (accountLock) {
            return accounts.values();
        }
    }

}

