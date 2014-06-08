package com.bank.event;

import com.bank.domain.Transaction;

public class DepositEvent extends Event {
    public DepositEvent(Transaction transaction) {
        super(transaction);
    }
}
