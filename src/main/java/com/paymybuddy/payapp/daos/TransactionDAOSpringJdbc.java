package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.constants.DBStatements;
import com.paymybuddy.payapp.dtos.TransactionWithMailsDTO;
import com.paymybuddy.payapp.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TransactionDAOSpringJdbc implements TransactionDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final String ZONE_ID;

    @Autowired
    public TransactionDAOSpringJdbc(NamedParameterJdbcTemplate jdbcTemplate,
                                    @Value("${default.zoneID}") String zoneID) {
        this.jdbcTemplate = jdbcTemplate;
        this.ZONE_ID = zoneID;
    }

    /**
     * @see TransactionDAO
     */
    @Override
    public Collection<Transaction> getTransactionsByUserMail(final String userMail) throws DataAccessException {
        return getTransactions(userMail, DBStatements.GET_ALL_TRANSACTIONS);
    }

    /**
     * @see TransactionDAO
     */
    @Override
    public Collection<Transaction> getDebitTransactionsByUserMail(final String userMail) throws DataAccessException {
        return getTransactions(userMail, DBStatements.GET_DEBIT_TRANSACTIONS);
    }

    /**
     * @see TransactionDAO
     */
    @Override
    public Collection<Transaction> getCreditTransactionsByUserMail(final String userMail) throws DataAccessException {
        return getTransactions(userMail, DBStatements.GET_CREDIT_TRANSACTIONS);
    }

    private Collection<Transaction> getTransactions(final String userMail, final String statement) {
        return jdbcTemplate.query(statement,
                new MapSqlParameterSource("userMail", userMail),
                (rs, rowNum) ->
                        new Transaction(
                                rs.getInt("id"),
                                rs.getInt("debtor_id"),
                                rs.getInt("creditor_id"),
                                ZonedDateTime.of(rs.getTimestamp("zoned_date_time").toLocalDateTime(), ZoneId.of(this.ZONE_ID)),
                                BigDecimal.valueOf(rs.getDouble("amount")),
                                BigDecimal.valueOf(rs.getDouble("commission")),
                                rs.getString("description")
                        ));
    }

    /**
     * @see TransactionDAO
     */
    @Override
    public boolean save(final TransactionWithMailsDTO transactionWithMailsDTO) throws DataAccessException {
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("userMail", transactionWithMailsDTO.getUserMail());
        parameterMap.put("recipientMail", transactionWithMailsDTO.getRecipientMail());
        parameterMap.put("description", transactionWithMailsDTO.getDescription());
        parameterMap.put("zoned_date_time", transactionWithMailsDTO.getDate());
        parameterMap.put("amount", transactionWithMailsDTO.getAmount());
        parameterMap.put("commission", transactionWithMailsDTO.getCommission());

        if (jdbcTemplate.update(DBStatements.INSERT_TRANSACTION, parameterMap) == 0) {
            throw new DataRetrievalFailureException("One of requested user does not exists.");
        }
        return true;
    }
}
