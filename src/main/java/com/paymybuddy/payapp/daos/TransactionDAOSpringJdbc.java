package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.Transaction;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Collection;

@Repository
public class TransactionDAOSpringJdbc implements TransactionDAO {
    /**
     * @see TransactionDAO
     */
    @Override
    public Collection<Transaction> getTransactionsByUserMail(String userMail) throws DataAccessException {
        return null;
    }

    /**
     * @see TransactionDAO
     */

    @Override
    public Collection<Transaction> getDebitTransactionsByUserMail(final String userMail) throws DataAccessException {
        return null;
    }

    /**
     * @see TransactionDAO
     */

    @Override
    public Collection<Transaction> getCreditTransactionsByUserMail(final String userMail) throws DataAccessException {
        return null;
    }

    /**
     * @see TransactionDAO
     */
    @Override
    public boolean save(final String userMail,
                        final String recipientMail,
                        final ZonedDateTime dateTime,
                        final String description,
                        final double amount,
                        final double total) throws DataAccessException {
        return false;
    }
}
