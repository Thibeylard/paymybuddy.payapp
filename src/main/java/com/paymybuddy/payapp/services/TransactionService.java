package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.Transaction;

import java.util.Collection;

public interface TransactionService {
    /**
     * Get all transactions of authenticated User.
     *
     * @return All User transactions as Transaction Collection
     */
    Collection<Transaction> getUserTransactions();

    /**
     * Get all debit transactions of authenticated User.
     *
     * @return All User debit transactions as Transaction Collection
     */
    Collection<Transaction> getUserDebitTransactions();

    /**
     * Get all credit transactions of authenticated User.
     *
     * @return All User credit transactions as Transaction Collection
     */
    Collection<Transaction> getUserCreditTransactions();

    /**
     * Send money to User identified by recipientMail.
     *
     * @param recipientMail Mail of User that receives money
     * @param description   Transaction description
     * @param amount        Transaction amount
     */
    void makeTransaction(String recipientMail, String description, double amount);
}
