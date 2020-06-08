package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.Transaction;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public class TransactionDAOSpringJdbc implements TransactionDAO {
    /**
     * @see TransactionDAO
     */
    @Override
    public Collection<Transaction> getTransactionsByUserMail(String userMail) {
        return null;
    }

    /**
     * @see TransactionDAO
     */

    @Override
    public Collection<Transaction> getDebitTransactionsByUserMail(String userMail) {
        return null;
    }

    /**
     * @see TransactionDAO
     */

    @Override
    public Collection<Transaction> getCreditTransactionsByUserMail(String userMail) {
        return null;
    }

    /**
     * @see TransactionDAO
     */

    @Override
    public boolean save(Transaction transaction) {
        return false;
    }
}
