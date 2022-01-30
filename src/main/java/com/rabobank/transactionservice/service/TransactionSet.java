package com.rabobank.transactionservice.service;


import com.rabobank.transactionservice.domain.Transaction;

import java.util.HashSet;
import java.util.Set;

public enum TransactionSet {

    INSTANCE;

    private TransactionSet() {}

    public Set<Transaction> getTransactionSet() {
        return transactionSet;
    }


    private Set<Transaction> transactionSet = new HashSet<>();

    public boolean addTransaction(Transaction transaction) {
        System.out.println(transactionSet.toString());
        return transactionSet.add(transaction);
    }


}
