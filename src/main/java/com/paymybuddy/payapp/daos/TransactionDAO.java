package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.Transaction;

import java.util.Collection;

public interface TransactionDAO {

    /**
     * Get all transactions where ID of user identified by userMail correspond either to creditorId or debtorId in Transaction table.
     *
     * @param userMail Mail of User to look for transactions
     * @return All User transactions as Transaction Collection
     */
    Collection<Transaction> getTransactionsByUserMail(String userMail);

    /**
     * Get all transactions where ID of user identified by userMail correspond to debtorId in Transaction table.
     *
     * @param userMail Mail of User to look for transactions
     * @return All User debit transactions as Transaction Collection
     */
    Collection<Transaction> getDebitTransactionsByUserMail(String userMail);

    /**
     * Get all transactions where ID of user identified by userMail correspond to creditorID in Transaction table.
     *
     * @param userMail Mail of User to look for transactions
     * @return All User credit transactions as Transaction Collection
     */
    Collection<Transaction> getCreditTransactionsByUserMail(String userMail);

    /**
     * Save transaction in Transaction table.
     *
     * @param transaction Transaction as object
     * @return true if operation succeed
     */
    boolean save(Transaction transaction);
}
