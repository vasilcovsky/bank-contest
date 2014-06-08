package com.bank;

import com.bank.domain.*;
import com.bank.event.*;
import java.util.*;
import java.util.concurrent.*;

public class ThreadRunner implements TransactionHistoryRunner {

    @Override
    public void run(Bank bank) {
        List<Thread> threads = new ArrayList<Thread>();

        CountDownLatch counter =
                new CountDownLatch(bank.getTransactions().length);

        Map<Integer, BlockingQueue<Event>> queues =
                new HashMap<Integer, BlockingQueue<Event>>();

        /**
         * Create threads and event queue for each thread
         */
        for(Account acc: bank.getAccounts()) {
            BlockingQueue q = new LinkedBlockingQueue();
            queues.put(acc.getAccountId(), q);

            Thread t = new Thread(new BankWorker(counter, q));
            t.start();
            threads.add(t);
        }

        /**
         * Thread consumes Event objects.
         *
         * If instance of Event is WithdrawEvent, then thread immediately
         * transfers money to another account . Otherwise, thread waits
         * for money.
         */
        for(Transaction t: bank.getTransactions()) {
            BlockingQueue fromQueue = queues.get(t.getFrom().getAccountId());
            BlockingQueue toQueue = queues.get(t.getTo().getAccountId());

            fromQueue.add(new WithdrawEvent(t));
            toQueue.add(new DepositEvent(t));
        }

        /**
         * wait for completion of processing transaction queues.
         */
        try {
            counter.await();
        } catch (InterruptedException e) {}


        for(Thread t: threads) {
            t.interrupt();
        }

    }

}
