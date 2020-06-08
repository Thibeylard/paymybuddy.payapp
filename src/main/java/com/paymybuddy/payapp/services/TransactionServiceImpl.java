package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.TransactionDAO;
import com.paymybuddy.payapp.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Collection;

@Service
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;
    private final ClockService clockService;
    private final MonetizationService monetizationService;

    @Autowired
    public TransactionServiceImpl(TransactionDAO transactionDAO, ClockService clockService, MonetizationService monetizationService) {
        this.transactionDAO = transactionDAO;
        this.clockService = clockService;
        this.monetizationService = monetizationService;
    }

    /**
     * @see TransactionService
     */
    @Override
    public Collection<Transaction> getUserTransactions() throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transactionDAO.getTransactionsByUserMail(authUser.getUsername());
    }

    /**
     * @see TransactionService
     */
    @Override
    public Collection<Transaction> getUserDebitTransactions() throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transactionDAO.getDebitTransactionsByUserMail(authUser.getUsername());
    }

    /**
     * @see TransactionService
     */
    @Override
    public Collection<Transaction> getUserCreditTransactions() throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return transactionDAO.getCreditTransactionsByUserMail(authUser.getUsername());
    }

    /**
     * @see TransactionService
     */
    @Override
    @Transactional(readOnly = false)
    public void makeTransaction(String recipientMail, String description, double amount) throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ZonedDateTime time = clockService.now();
        double total = amount - monetizationService.monetize(amount);
        transactionDAO.save(authUser.getUsername(), recipientMail, time, description, amount, total);

    }
}
