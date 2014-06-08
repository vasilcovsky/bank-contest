package com.bank.event;

import com.bank.domain.Transaction;

abstract public class Event {
    private final Transaction transaction;

    Event(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction() {
        return transaction;
    }

}
