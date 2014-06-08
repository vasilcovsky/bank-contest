package com.bank;

import com.bank.domain.*;
import com.bank.event.*;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

class BankWorker implements Runnable {
    private final BlockingQueue<Event> queue;
    private final CountDownLatch counter;
    private final Random random = new Random(System.currentTimeMillis());

    public BankWorker(CountDownLatch counter, BlockingQueue<Event> queue) {
        this.counter = counter;
        this.queue = queue;
    }

    @Override
    public void run() {
        while(counter.getCount() > 0) {
            try {
                processEvent();
                Thread.sleep(random.nextInt(100));
            } catch (InterruptedException e) {}
        }
    }

    void processEvent() throws InterruptedException {
        Event event = queue.take();
        Transaction transaction = event.getTransaction();

        synchronized (event.getTransaction()) {

            Account from = transaction.getFrom();
            Account to = transaction.getTo();

            if (event instanceof WithdrawEvent) {
                if(from.withdraw(transaction.getAmount())) {
                    // only for demo purposes, in real system use another status or sync object
                    transaction.setStatus(TransactionStatus.SUCCESS);
                }
                else {
                    transaction.setStatus(TransactionStatus.FAILURE);
                }
                transaction.notify();
            }
            else if (event instanceof DepositEvent) {
                while(transaction.getStatus().equals(TransactionStatus.NONE)) {
                    transaction.wait();
                }
                if (transaction.getStatus().equals(TransactionStatus.SUCCESS)) {
                    to.deposit(transaction.getAmount());
                }
                counter.countDown();
            }
        }
    }
}
