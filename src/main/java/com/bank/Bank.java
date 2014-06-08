package com.bank;

import com.bank.domain.Account;
import com.bank.domain.Transaction;
import com.bank.domain.TransactionStatus;
import org.apache.commons.lang3.time.StopWatch;

import java.io.Serializable;
import java.util.Random;

public class Bank implements Serializable {

    public static final int MAX_AMOUNT = 100000;
    public static final int TOTAL_TRANSACTIONS = 1000;
    public static final int TOTAL_ACCOUNTS = 100;

    private final Account[] accounts = new Account[TOTAL_ACCOUNTS];

    private final Transaction[] transactions = new Transaction[TOTAL_TRANSACTIONS];

    private final Random random = new Random(System.currentTimeMillis());

    private Bank() {}

    public Account[] getAccounts() {
        return accounts;
    }

    public Transaction[] getTransactions() {
        return transactions;
    }

    public static Bank initializeBank() {
        Bank bank = new Bank();

        for (int i=0; i<TOTAL_ACCOUNTS; i++) {
            bank.accounts[i] = new Account(i+1, bank.random.nextInt(MAX_AMOUNT));
        }

        for (int i=0; i<TOTAL_TRANSACTIONS; i++) {
            Transaction t = new Transaction();
            t.setFrom(bank.getRandomAccount());
            t.setTo(bank.getRandomAccount());
            t.setAmount(bank.random.nextInt(MAX_AMOUNT));
            bank.transactions[i] = t;
        }
        return bank;
    }

    public int getFunds() {
        int total = 0;
        for (Account account : accounts) {
            total += account.getBalance();
        }
        return total;
    }

    public int getTransactionsCount(TransactionStatus status) {
        int counter = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getStatus().equals(status)) {
                counter++;
            }
        }
        return counter;
    }

    Account getRandomAccount() {
        return accounts[random.nextInt(TOTAL_ACCOUNTS)];
    }

    public long process(TransactionHistoryRunner runner) {
        StopWatch watch = new StopWatch();

        watch.start();
        runner.run(this);
        watch.stop();

        return watch.getTime();
    }

}
