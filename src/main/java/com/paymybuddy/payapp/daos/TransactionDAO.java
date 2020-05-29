package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.Transaction;

import java.util.Collection;

public interface TransactionDAO {

    /**
     * Get all transactions where userId correspond either to creditorId or debtorId in Transaction table.
     *
     * @param userId Id of User
     * @return All User transactions as Transaction Collection
     */
    Collection<Transaction> getTransactionsByUserId(int userId);

    /**
     * Get all transactions where userId correspond to debtorId in Transaction table.
     *
     * @param userId Id of User
     * @return All User debit transactions as Transaction Collection
     */
    Collection<Transaction> getDebitTransactionsByUserId(int userId);

    /**
     * Get all transactions where userId correspond to creditorID in Transaction table.
     *
     * @param userId Id of User
     * @return All User credit transactions as Transaction Collection
     */
    Collection<Transaction> getCreditTransactionsByUserId(int userId);

    /**
     * Save transaction in Transaction table.
     *
     * @param transaction Transaction as object
     * @return true if operation succeed
     */
    boolean save(Transaction transaction);
}
