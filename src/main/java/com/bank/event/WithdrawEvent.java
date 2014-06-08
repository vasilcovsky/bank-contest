package com.bank.event;

import com.bank.domain.Transaction;

public class WithdrawEvent extends Event {
    public WithdrawEvent(Transaction transaction) {
        super(transaction);
    }
}
