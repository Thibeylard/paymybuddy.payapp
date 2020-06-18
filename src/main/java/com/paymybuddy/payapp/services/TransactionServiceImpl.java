package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.TransactionDAO;
import com.paymybuddy.payapp.dtos.TransactionToSaveDTO;
import com.paymybuddy.payapp.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Size;
import java.util.Collection;

@Service
@Validated
@Transactional(readOnly = true)
public class TransactionServiceImpl implements TransactionService {

    private final TransactionDAO transactionDAO;
    private final MonetizationService monetizationService;
    private final ClockService clockService;

    @Autowired
    public TransactionServiceImpl(TransactionDAO transactionDAO, MonetizationService monetizationService, ClockService clockService) {
        this.transactionDAO = transactionDAO;
        this.monetizationService = monetizationService;
        this.clockService = clockService;
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
    public void makeTransaction(final String recipientMail,
                                final @Size(min = 5, max = 30) String description,
                                final double amount) throws DataAccessException {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount to send can't be negative or null.");

        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        double total = amount - monetizationService.monetize(amount);

        transactionDAO.save(new TransactionToSaveDTO(authUser.getUsername(), recipientMail, clockService.now(), amount, total, description));

    }
}
