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
    public Collection<Transaction> getDebitTransactionsByUserMail(String userMail) throws DataAccessException {
        return null;
    }

    /**
     * @see TransactionDAO
     */

    @Override
    public Collection<Transaction> getCreditTransactionsByUserMail(String userMail) throws DataAccessException {
        return null;
    }

    /**
     * @see TransactionDAO
     */
    @Override
    public boolean save(String userMail, String recipientMail, ZonedDateTime dateTime, String description, double amount, double total) throws DataAccessException {
        return false;
    }
}
