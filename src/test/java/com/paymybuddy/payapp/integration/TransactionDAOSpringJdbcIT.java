package com.paymybuddy.payapp.integration;

import com.paymybuddy.payapp.daos.TransactionDAO;
import com.paymybuddy.payapp.dtos.TransactionWithMailsDTO;
import com.paymybuddy.payapp.models.Transaction;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.Table;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ExtendWith({FlywayTestExtension.class})
@SpringBootTest
@ActiveProfiles("test_h2")
@DisplayName("TransactionDAO tests on : ")
public class TransactionDAOSpringJdbcIT {
    @Autowired
    private TransactionDAO transactionDAO;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String ZONE_ID;

    private final ZonedDateTime newTransactionTime;

    public TransactionDAOSpringJdbcIT(@Value("${default.zoneID}") String ZONE_ID) {
        this.ZONE_ID = ZONE_ID;
        newTransactionTime = ZonedDateTime.of(2020, 1, 7, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
    }


    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("getTransactions() Successes")
    public void Given_authenticatedUser_When_getAnyUserTransactions_Then_returnUserTransactions() {
        // All types of Transactions for user2
        // Thorough tests on all values retrieved
        ZonedDateTime firstDate = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        ZonedDateTime secondDate = ZonedDateTime.of(2020, 1, 3, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        ZonedDateTime thirdDate = ZonedDateTime.of(2020, 1, 6, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        Collection<Transaction> result = transactionDAO.getTransactionsByUserMail("user2@mail.com");
        assertThat(result)
                .hasSize(3)
                .extracting("id").containsExactly(Optional.of(1), Optional.of(3), Optional.of(6));
        assertThat(result)
                .extracting("debtorID").containsExactly(1, 2, 3);
        assertThat(result)
                .extracting("creditorID").containsExactly(2, 3, 2);
        assertThat(result)
                .extracting("amount").containsExactly(BigDecimal.valueOf(10.00), BigDecimal.valueOf(10.00), BigDecimal.valueOf(10.00));
        assertThat(result)
                .extracting("commission").containsExactly(BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.05), BigDecimal.valueOf(0.05));
        assertThat(result)
                .extracting("description").containsExactly("transaction1", "transaction3", "transaction6");

        assertThat(result)
                .extracting("date").element(0).usingRecursiveComparison().isEqualTo(firstDate);
        assertThat(result)
                .extracting("date").element(1).usingRecursiveComparison().isEqualTo(secondDate);
        assertThat(result)
                .extracting("date").element(2).usingRecursiveComparison().isEqualTo(thirdDate);

        // Basic tests
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
    @FlywayTest
    @WithMockUser
    @DisplayName("makeTransaction() Success")
    public void Given_authenticatedUser_When_makeTransaction_Then_tableTransactionUpdated() {
        TransactionWithMailsDTO transactionDTO = new TransactionWithMailsDTO(
                "user4@mail.com",
                "user1@mail.com",
                newTransactionTime,
                BigDecimal.valueOf(50.00),
                BigDecimal.valueOf(0.25),
                "transaction7");

        Table transactionTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Transaction");

        Assertions.assertThat(transactionTable).hasNumberOfRows(6);

        assertThat(transactionDAO.save(transactionDTO))
                .isTrue();

        transactionTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Transaction");

        Assertions.assertThat(transactionTable)
                .hasNumberOfRows(7)
                .row(6)
                .value("debtor_id").isEqualTo(4)
                .value("creditor_id").isEqualTo(1)
                .value("description").isEqualTo("transaction7")
                .value("amount").isEqualTo(50.00)
                .value("commission").isEqualTo(0.25);

        Timestamp timestampExample =
                (Timestamp) transactionTable.getRow(6).getColumnValue("date").getValue();

        // Check that date in database perfectly match with passed ZonedDateTime object
        assertThat(timestampExample.toLocalDateTime()).isEqualTo(newTransactionTime.toLocalDateTime());

    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("makeTransaction() Exception")
    public void Given_databaseError_When_makeTransaction_Then_throwException() {
        TransactionWithMailsDTO transactionDTO = new TransactionWithMailsDTO(
                "user6@mail.com",
                "user1@mail.com",
                newTransactionTime,
                BigDecimal.valueOf(50.00),
                BigDecimal.valueOf(0.25),
                "transaction7");

        // User 6 doesn't exist
        assertThrows(DataAccessException.class, () -> transactionDAO.save(transactionDTO));
    }
}
