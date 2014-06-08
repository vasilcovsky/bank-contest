package com.bank;

import com.bank.domain.TransactionStatus;
import org.apache.commons.lang3.SerializationUtils;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;


public class BankTest {
    @Test
    public void testProcess() throws Exception {
        Bank bank = Bank.initializeBank();
        Bank singleThreadedBank = SerializationUtils.clone(bank);
        Bank multiThreadedBank = SerializationUtils.clone(bank);

        System.out.println("Total no. of transactions: " + Bank.TOTAL_TRANSACTIONS);
        System.out.println("Total no. of accounts: " + Bank.TOTAL_ACCOUNTS);

        System.out.println("Running transaction history in single thread...");
        long timerSingle = singleThreadedBank.process(new SimpleRunner());

        System.out.println("Running transaction history in multiple threads...");
        long timerThreaded = multiThreadedBank.process(new ThreadRunner());

        System.out.println("Elapsed time for single-thread solution: " + timerSingle);
        System.out.println("Elapsed time for multi-thread solution: " + timerThreaded);

        assertEquals(
                singleThreadedBank.getFunds(),
                multiThreadedBank.getFunds(),
                "Equal funds"
        );

        assertEquals(
                singleThreadedBank.getTransactionsCount(TransactionStatus.SUCCESS),
                multiThreadedBank.getTransactionsCount(TransactionStatus.SUCCESS),
                "Equal number of SUCCESS transactions"
        );

        assertEquals(
                singleThreadedBank.getTransactionsCount(TransactionStatus.FAILURE),
                multiThreadedBank.getTransactionsCount(TransactionStatus.FAILURE),
                "Equal number of FAILURE transactions"
        );

        assertEquals(
                singleThreadedBank.getTransactionsCount(TransactionStatus.NONE),
                multiThreadedBank.getTransactionsCount(TransactionStatus.NONE),
                "Equal number of NONE transactions"
        );

        assertTrue(timerThreaded < timerSingle, "Multithreaded version is faster");

    }
}
