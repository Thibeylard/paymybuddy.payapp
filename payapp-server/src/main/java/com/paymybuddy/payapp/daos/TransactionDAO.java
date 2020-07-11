package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.dtos.TransactionWithMailsDTO;
import com.paymybuddy.payapp.models.Transaction;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

public interface TransactionDAO {

    /**
     * Get all transactions where ID of user identified by userMail correspond either to creditorId or debtorId in Transaction table.
     *
     * @param userMail Mail of User to look for transactions
     * @return All User transactions as Transaction Collection
     */
    Collection<Transaction> getTransactionsByUserMail(final String userMail)
            throws DataAccessException;

    /**
     * Get all transactions where ID of user identified by userMail correspond to debtorId in Transaction table.
     *
     * @param userMail Mail of User to look for transactions
     * @return All User debit transactions as Transaction Collection
     */
    Collection<Transaction> getDebitTransactionsByUserMail(final String userMail)
            throws DataAccessException;

    /**
     * Get all transactions where ID of user identified by userMail correspond to creditorID in Transaction table.
     *
     * @param userMail Mail of User to look for transactions
     * @return All User credit transactions as Transaction Collection
     */
    Collection<Transaction> getCreditTransactionsByUserMail(final String userMail)
            throws DataAccessException;

    /**
     * Save new Transaction based on TransactionDTO object.
     *
     * @param transactionWithMailsDTO Transaction object used to save a Transaction in database
     * @return true if operation succeed
     */
    boolean save(final TransactionWithMailsDTO transactionWithMailsDTO)
            throws DataAccessException;
}
