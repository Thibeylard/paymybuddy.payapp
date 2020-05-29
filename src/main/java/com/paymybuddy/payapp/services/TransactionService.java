package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.Transaction;

import java.util.Collection;

public interface TransactionService {
    /**
     * Get all transactions where userId correspond either to creditorId or debtorId in Transaction table.
     *
     * @param userId Id of User
     * @return All User transactions as Transaction Collection
     */
    Collection<Transaction> getTransactionsByUserId(int userId);

    /**
     * Get all debit transactions of User identified by userId.
     *
     * @param userId Id of User
     * @return All User debit transactions as Transaction Collection
     */
    Collection<Transaction> getDebitTransactionsByUserId(int userId);

    /**
     * Get all credit transactions of User identified by userId.
     *
     * @param userId Id of User
     * @return All User credit transactions as Transaction Collection
     */
    Collection<Transaction> getCreditTransactionsByUserId(int userId);

    boolean makeTransaction(String creditorUsername, String description, double amount);
}
