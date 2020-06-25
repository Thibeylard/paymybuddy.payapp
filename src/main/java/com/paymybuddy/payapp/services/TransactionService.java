package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.Transaction;
import org.springframework.dao.DataAccessException;

import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Collection;

public interface TransactionService {
    /**
     * Get all transactions of authenticated User.
     *
     * @return All User transactions as Transaction Collection
     */
    Collection<Transaction> getUserTransactions()
            throws DataAccessException;

    /**
     * Get all debit transactions of authenticated User.
     *
     * @return All User debit transactions as Transaction Collection
     */
    Collection<Transaction> getUserDebitTransactions()
            throws DataAccessException;

    /**
     * Get all credit transactions of authenticated User.
     *
     * @return All User credit transactions as Transaction Collection
     */
    Collection<Transaction> getUserCreditTransactions()
            throws DataAccessException;

    /**
     * Send money to User identified by recipientMail.
     *
     * @param recipientMail Mail of User that receives money
     * @param description   Transaction description
     * @param amount        Transaction amount
     */
    void makeTransaction(final String recipientMail,
                         final @Size(min = 5, max = 30) String description,
                         final BigDecimal amount)
            throws DataAccessException;
}
