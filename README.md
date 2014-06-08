Bank Problem
============

Slow Bank
-------------

Imagine we need to process millions of random transaction that we have received in an array. Each transaction transfers some amount of money from one account to another.
A simple single thread program will process those transactions in the order they are present in array.
After executing that program account balances were changed, some transaction were successful,  some transactions have failed because withdrawal account didn’t have enough money.

High Performance Bank
-------------

You need to write a multithreaded program that will process those transactions faster and the result of executing (account balances and transaction success/failure) should be the same as from Slow Bank program

To explain the complexity bear in mind  that if you have 2 consecutive transactions:
  1. Transfer 100$ from account A to account B
  2. Transfer 100$ from account B to account A
and you try to execute second transaction before the first transaction in your parallel program, it may fail because B don’t have money.

Another example is that in the following situation: 
  * A->B
  * B->C
  * C->D
Transactions should be executed consecutively.

At the same time the following transactions can be executed in parallel:
  * E->F
  * K->J
  * R->F
