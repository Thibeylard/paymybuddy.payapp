package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.BankAccountDAO;
import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collection;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountDAO bankAccountDAO;

    @Autowired
    public BankAccountServiceImpl(BankAccountDAO bankAccountDAO) {
        this.bankAccountDAO = bankAccountDAO;
    }

    /**
     * @see BankAccountService
     */
    @Override
    public Collection<BankAccount> getUserBankAccounts() throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return bankAccountDAO.getBankAccounts(authUser.getUsername());
    }

    /**
     * @see BankAccountService
     */
    @Override
    public void addBankAccount(final String description, final String IBAN) throws DataAccessException {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        bankAccountDAO.save(authUser.getUsername(), description, IBAN);
    }

    /**
     * @see BankAccountService
     */
    @Override
    public void updateBankAccount(final int bankAccountID, final String description, final String IBAN) throws DataAccessException {
        bankAccountDAO.update(bankAccountID, description, IBAN);
    }

    /**
     * @see BankAccountService
     */
    @Override
    public void deleteBankAccount(final int bankAccountID) throws DataAccessException {
        bankAccountDAO.delete(bankAccountID);
    }

    /**
     * @see BankAccountService
     */
    @Override
    public Collection<BankOperation> getBankAccountOperations(int bankAccountID) throws DataAccessException {
        return bankAccountDAO.getBankOperations(bankAccountID);
    }


    /**
     * @see BankAccountService
     */
    @Override
    public void transferMoney(final int bankAccountID, final BigDecimal amount) throws DataAccessException {
        // TODO Ajouter la méthode de relation à la banque (via une interface)
        bankAccountDAO.saveTransferOperation(bankAccountID, amount);
    }

    /**
     * @see BankAccountService
     */
    @Override
    public void withdrawMoney(final int bankAccountID, final BigDecimal amount) throws DataAccessException {
        // TODO Ajouter la méthode de relation à la banque (via une interface)
        bankAccountDAO.saveWithdrawOperation(bankAccountID, amount);
    }
}
