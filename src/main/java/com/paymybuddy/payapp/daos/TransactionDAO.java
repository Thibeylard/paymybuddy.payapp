package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.Transaction;
import org.springframework.dao.DataAccessException;

import java.time.ZonedDateTime;
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
     * Save new Transaction based on parameters.
     *
     * @param userMail      User as debtor
     * @param recipientMail Recipient as creditor
     * @param dateTime      Time of transaction
     * @param description   Simple description for transaction
     * @param amount        Amount of money
     * @param total         Real amount after monetization
     * @return true if operation succeed
     */
    boolean save(final String userMail,
                 final String recipientMail,
                 final ZonedDateTime dateTime,
                 final String description,
                 final double amount,
                 final double total)
            throws DataAccessException;
}
