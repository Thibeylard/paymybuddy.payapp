package com.paymybuddy.payapp.integration;

import com.paymybuddy.payapp.daos.TransactionDAO;
import com.paymybuddy.payapp.services.ClockService;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test_h2")
@DisplayName("TransactionDAO tests on : ")
public class TransactionDAOSpringJdbcIT {
    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    @SpyBean
    ClockService clockService;
    private Map<String, ZonedDateTime> transactionTimes;

    @BeforeEach
    public void databaseSetup() {

        transactionTimes = new HashMap<>();
        transactionTimes.put("transaction1Time",
                ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault()));
        transactionTimes.put("transaction2Time",
                ZonedDateTime.of(2020, 1, 2, 0, 0, 0, 0, ZoneId.systemDefault()));
        transactionTimes.put("transaction3Time",
                ZonedDateTime.of(2020, 1, 3, 0, 0, 0, 0, ZoneId.systemDefault()));
        transactionTimes.put("transaction4Time",
                ZonedDateTime.of(2020, 1, 4, 0, 0, 0, 0, ZoneId.systemDefault()));
        transactionTimes.put("transaction5Time",
                ZonedDateTime.of(2020, 1, 5, 0, 0, 0, 0, ZoneId.systemDefault()));
        transactionTimes.put("transaction6Time",
                ZonedDateTime.of(2020, 1, 6, 0, 0, 0, 0, ZoneId.systemDefault()));
        transactionTimes.put("transaction7Time",
                ZonedDateTime.of(2020, 1, 7, 0, 0, 0, 0, ZoneId.systemDefault()));

        try {
            // Reset Transaction Table
            jdbcTemplate.getJdbcTemplate().update("DELETE FROM TRANSACTION;");

            // Reset Contact Table
            jdbcTemplate.getJdbcTemplate().update("DELETE FROM CONTACT;");

            // Reset User_Role Table
            jdbcTemplate.getJdbcTemplate().update("DELETE FROM USER_ROLE;");

            // Reset User Table
            jdbcTemplate.getJdbcTemplate().update("DELETE FROM USER;");

            // Insert users
            jdbcTemplate.getJdbcTemplate().update("INSERT INTO User (id, username, mail, password) " +
                    "VALUES (1, 'user1', 'user1@mail.com', 'user1pass')," +
                    "(2, 'user2', 'user2@mail.com', 'user2pass')," +
                    "(3, 'user3', 'user3@mail.com', 'user3pass')," +
                    "(4, 'user4', 'user4@mail.com', 'user4pass')");

            // Insert roles for users
            jdbcTemplate.getJdbcTemplate().update("INSERT INTO User_Role (user_id, role_id) " +
                    "VALUES (1,1)," +
                    "(2,1)," +
                    "(3,1)," +
                    "(4,1)");

            // Insert contacts between users
            jdbcTemplate.getJdbcTemplate().update("INSERT INTO Contact (user_a_id, user_b_id) " +
                    "VALUES (1,2)," +
                    "(1,3)," +
                    "(2,3)," +
                    "(4,1)");

            // Insert few transactions between users
            jdbcTemplate.update("INSERT INTO Transaction (ID, DEBTOR_ID, CREDITOR_ID, DESCRIPTION, AMOUNT, TOTAL, DATE) " +
                            "VALUES (1,1,2,'transaction1',10.00,9.50,:transaction1Time)," +
                            "(2,3,1,'transaction2',10.00,9.50,:transaction2Time)," +
                            "(3,2,3,'transaction3',10.00,9.50,:transaction3Time)," +
                            "(4,3,4,'transaction4',10.00,9.50,:transaction4Time)," +
                            "(5,1,4,'transaction5',10.00,9.50,:transaction5Time)," +
                            "(6,3,2,'transaction6',10.00,9.50,:transaction6Time)",
                    transactionTimes);

        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Test
    @WithMockUser
    @DisplayName("getTransactions() Successes")
    public void Given_authenticatedUser_When_getAnyUserTransactions_Then_returnUserTransactions() {
        // All types of Transactions for user2
        assertThat(transactionDAO.getTransactionsByUserMail("user2@mail.com"))
                .hasSize(3);
        assertThat(transactionDAO.getDebitTransactionsByUserMail("user2@mail.com"))
                .hasSize(1);
        assertThat(transactionDAO.getCreditTransactionsByUserMail("user2@mail.com"))
                .hasSize(2);

        // All types of Transactions for user4
        assertThat(transactionDAO.getTransactionsByUserMail("user4@mail.com"))
                .hasSize(2);
        assertThat(transactionDAO.getDebitTransactionsByUserMail("user4@mail.com"))
                .hasSize(0);
        assertThat(transactionDAO.getCreditTransactionsByUserMail("user4@mail.com"))
                .hasSize(2);

    }

    @Test
    @WithMockUser
    @DisplayName("getTransactions() Exceptions")
    public void Given_inexistantUser_When_getAnyUserTransactions_Then_throwException() {
        assertThrows(DataAccessException.class, () -> transactionDAO.getTransactionsByUserMail("user5@mail.com"));
    }

    @Test
    @WithMockUser
    @DisplayName("makeTransaction() Success")
    public void Given_authenticatedUser_When_makeTransaction_Then_tableTransactionUpdated() {
        doReturn(transactionTimes.get("transaction7time")).when(clockService).now();

        Table transactionTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Transaction");

        Assertions.assertThat(transactionTable).hasNumberOfRows(6);

        assertThat(transactionDAO.save(
                "user4@mail.com",
                "user1@mail.com",
                "transaction7",
                50.00,
                47.50))
                .isTrue();

        transactionTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Transaction");

        Assertions.assertThat(transactionTable)
                .hasNumberOfRows(7)
                .row(6)
                .value("debtor_id").isEqualTo(4)
                .value("creditor_id").isEqualTo(1)
                .value("date").isEqualTo(transactionTimes.get("transaction7time"))
                .value("description").isEqualTo("transaction7")
                .value("amount").isEqualTo(50.00)
                .value("total").isEqualTo(47.50);

    }

    @Test
    @WithMockUser
    @DisplayName("makeTransaction() Exception")
    public void Given_databaseError_When_makeTransaction_Then_throwException() {
        // User 6 doesn't exist
        assertThrows(DataAccessException.class, () -> transactionDAO.save(
                "user4@mail.com",
                "user6@mail.com",
                "transaction7",
                50.00,
                47.50));
    }
}
