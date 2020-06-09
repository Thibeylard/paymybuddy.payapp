package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.constants.DBStatements;
import com.paymybuddy.payapp.models.Transaction;
import com.paymybuddy.payapp.services.ClockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.Collection;

@Repository
public class TransactionDAOSpringJdbc implements TransactionDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final ClockService clockService;

    @Autowired
    public TransactionDAOSpringJdbc(NamedParameterJdbcTemplate jdbcTemplate, ClockService clockService) {
        this.jdbcTemplate = jdbcTemplate;
        this.clockService = clockService;
    }

    /**
     * @see TransactionDAO
     */
    @Override
    public Collection<Transaction> getTransactionsByUserMail(final String userMail) throws DataAccessException {
        return getTransactions(userMail, DBStatements.GET_TRANSACTIONS);
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
        // TODO y a t-il un moyen d'éviter cette duplication de code ?
        Integer userID = jdbcTemplate.queryForObject(DBStatements.GET_USER_BY_MAIL,
                new MapSqlParameterSource("userMail", userMail),
                (rs, rowNum) ->
                        rs.getInt("id"));

        assert userID != null;

        return jdbcTemplate.query(statement,
                new MapSqlParameterSource("userID", userID),
                (rs, rowNum) ->
                        new Transaction(
                                rs.getInt("id"),
                                rs.getInt("debtor_id"),
                                rs.getInt("creditor_id"),
                                ZonedDateTime.of(rs.getTimestamp("date").toLocalDateTime(), clockService.getZone()),
                                rs.getDouble("amount"),
                                rs.getDouble("total"),
                                rs.getString("description")
                        ));
    }

    /**
     * @see TransactionDAO
     */
    @Override
    public boolean save(final String userMail,
                        final String recipientMail,
                        final String description,
                        final double amount,
                        final double total) throws DataAccessException {
        return false;
    }
}
