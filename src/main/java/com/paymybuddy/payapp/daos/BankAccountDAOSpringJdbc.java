package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.constants.DBStatements;
import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class BankAccountDAOSpringJdbc implements BankAccountDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String ZONE_ID;

    public BankAccountDAOSpringJdbc(NamedParameterJdbcTemplate jdbcTemplate,
                                    @Value("${default.zoneID}") String zoneID) {
        this.jdbcTemplate = jdbcTemplate;
        this.ZONE_ID = zoneID;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public Collection<BankAccount> getBankAccounts(final String userMail) throws DataAccessException {
        return jdbcTemplate.query(DBStatements.GET_BANK_ACCOUNTS,
                new MapSqlParameterSource("userMail", userMail),
                (rs, rowNum) ->
                        new BankAccount(
                                rs.getInt("id"),
                                rs.getInt("user_id"),
                                rs.getString("owner_fullname"),
                                rs.getString("description"),
                                rs.getString("IBAN")
                        ));
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public BankAccount getBankAccount(final int bankAccountID) throws DataAccessException {
        BankAccount bankAccount = jdbcTemplate.queryForObject(DBStatements.GET_BANK_ACCOUNT,
                new MapSqlParameterSource("bankAccountID", bankAccountID),
                (rs, rowNum) ->
                        new BankAccount(
                                rs.getInt("id"),
                                rs.getInt("user_id"),
                                rs.getString("owner_fullname"),
                                rs.getString("description"),
                                rs.getString("IBAN")
                        ));
        if (bankAccount == null) {
            throw new DataRetrievalFailureException("There is no bank account with bankAccountID");
        }

        return bankAccount;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean save(final String userMail,
                        final String ownerFullName,
                        final String description,
                        final String IBAN) throws DataAccessException {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("userMail", userMail);
        parameterMap.put("ownerFullName", ownerFullName);
        parameterMap.put("description", description);
        parameterMap.put("IBAN", IBAN);

        if (jdbcTemplate.update(DBStatements.INSERT_BANK_ACCOUNT, parameterMap) == 0) {
            throw new DataRetrievalFailureException("Requested user does not exists.");
        }
        return true;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean update(final int bankAccountID,
                          final String ownerFullName,
                          final String description,
                          final String IBAN) throws DataAccessException {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("bankAccountID", bankAccountID);
        parameterMap.put("ownerFullName", ownerFullName);
        parameterMap.put("description", description);
        parameterMap.put("IBAN", IBAN);

        if (jdbcTemplate.update(DBStatements.UPDATE_BANK_ACCOUNT, parameterMap) == 0) {
            throw new DataRetrievalFailureException("Requested Bank Account does not exists.");
        }
        return true;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean delete(final int bankAccountID) throws DataAccessException {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("bankAccountID", bankAccountID);

        if (jdbcTemplate.update(DBStatements.DELETE_BANK_ACCOUNT, parameterMap) == 0) {
            throw new DataRetrievalFailureException("Requested Bank Account does not exists.");
        }
        return true;
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public Collection<BankOperation> getBankOperations(final int bankAccountID) throws DataAccessException {
        return jdbcTemplate.query(DBStatements.GET_BANK_OPERATIONS,
                new MapSqlParameterSource("bankAccountID", bankAccountID),
                (rs, rowNum) ->
                        new BankOperation(
                                rs.getInt("id"),
                                rs.getInt("bank_account_id"),
                                ZonedDateTime.of(rs.getTimestamp("date").toLocalDateTime(),
                                        ZoneId.of(this.ZONE_ID)),
                                BigDecimal.valueOf(rs.getDouble("amount"))
                        ));
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean saveTransferOperation(final int bankAccountID,
                                         final ZonedDateTime date,
                                         final BigDecimal amount) throws DataAccessException {
        return saveOperation(bankAccountID, date, amount.negate());
    }

    /**
     * @see BankAccountDAO
     */
    @Override
    public boolean saveWithdrawOperation(final int bankAccountID,
                                         final ZonedDateTime date,
                                         final BigDecimal amount) throws DataAccessException {
        return saveOperation(bankAccountID, date, amount);
    }

    private boolean saveOperation(int bankAccountID, ZonedDateTime date, BigDecimal amount) {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("bankAccountID", bankAccountID);
        parameterMap.put("date", date);
        parameterMap.put("amount", amount);

        if (jdbcTemplate.update(DBStatements.INSERT_BANK_OPERATION, parameterMap) == 0) {
            throw new DataRetrievalFailureException("Requested bank account does not exists.");
        }
        return true;
    }
}
