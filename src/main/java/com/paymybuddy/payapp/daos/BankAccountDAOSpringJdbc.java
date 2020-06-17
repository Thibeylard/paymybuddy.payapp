package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collection;

@Repository
public class BankAccountDAOSpringJdbc implements BankAccountDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BankAccountDAOSpringJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public Collection<BankAccount> getBankAccounts(final String userMail) throws DataAccessException {
        return null;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean save(final String userMail,
                        final String ownerFullName,
                        final String description,
                        final String IBAN) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean update(final int bankAccountID,
                          final String ownerFullName,
                          final String description,
                          final String IBAN) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean delete(final int bankAccountID) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public Collection<BankOperation> getBankOperations(final int bankAccountID) throws DataAccessException {
        return null;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean saveTransferOperation(final int bankAccountID,
                                         final ZonedDateTime date,
                                         final BigDecimal amount) throws DataAccessException {
        return false;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean saveWithdrawOperation(final int bankAccountID,
                                         final ZonedDateTime date,
                                         final BigDecimal amount) throws DataAccessException {
        return false;
    }
}
