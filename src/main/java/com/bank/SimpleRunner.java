package com.bank;

import com.bank.domain.*;
import java.util.Random;

public class SimpleRunner implements TransactionHistoryRunner {

    @Override
    public void run(Bank bank) {
        processTransactions(bank);
    }

    void processTransactions(Bank bank) {
        Transaction[] transactions = bank.getTransactions();
        Random random = new Random(System.currentTimeMillis());

        for(Transaction t: transactions) {
            Account from = t.getFrom();
            Account to = t.getTo();
            if (from.getBalance() >= t.getAmount()) {
                from.withdraw(t.getAmount());
                to.deposit(t.getAmount());
                t.setStatus(TransactionStatus.SUCCESS);
            }
            else {
                t.setStatus(TransactionStatus.FAILURE);
            }

            try {
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {}
        }
    }
}
