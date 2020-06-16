package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Collection;

@Repository
public class BankAccountDAOSpringJdbc implements BankAccountDAO {
    /**
     * @see BankAccountDAO
     */
    @Override
    public Collection<BankAccount> getBankAccounts(String userMail) throws DataAccessException {
        return null;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean save(String userMail, String description, String IBAN) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean update(int bankAccountID, String description, String IBAN) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean delete(int bankAccountID) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public Collection<BankOperation> getBankOperations(int bankAccountID) throws DataAccessException {
        return null;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean saveTransferOperation(int bankAccountID, BigDecimal amount) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean saveWithdrawOperation(int bankAccountID, BigDecimal amount) throws DataAccessException {
        return false;
    }
}
