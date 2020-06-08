package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.TransactionDAO;
import com.paymybuddy.payapp.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;

    @Autowired
    public TransactionServiceImpl(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }

    /**
     * @see TransactionService
     */
    @Override
    public Collection<Transaction> getUserTransactions() throws DataAccessException {
        return null;
    }

    /**
     * @see TransactionService
     */
    @Override
    public Collection<Transaction> getUserDebitTransactions() throws DataAccessException {
        return null;
    }

    /**
     * @see TransactionService
     */
    @Override
    public Collection<Transaction> getUserCreditTransactions() throws DataAccessException {
        return null;
    }

    /**
     * @see TransactionService
     */
    @Override
    @Transactional(readOnly = false)
    public void makeTransaction(String recipientMail, String description, double amount) throws DataAccessException {

    }
}