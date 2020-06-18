package com.paymybuddy.payapp.integration;

import com.paymybuddy.payapp.daos.TransactionDAO;
import com.paymybuddy.payapp.dtos.TransactionToSaveDTO;
import com.paymybuddy.payapp.models.Transaction;
import com.paymybuddy.payapp.services.ClockService;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.Table;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.h2.api.TimestampWithTimeZone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
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

    private final ZonedDateTime newTransactionTime =
            ZonedDateTime.of(2020, 1, 7, 0, 0, 0, 0, ZoneId.of(ClockService.ZONE_ID));

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("getTransactions() Successes")
    public void Given_authenticatedUser_When_getAnyUserTransactions_Then_returnUserTransactions() {
        // All types of Transactions for user2
        // Thorough tests on all values retrieved
        ZonedDateTime firstDate = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of(ClockService.ZONE_ID));
        ZonedDateTime secondDate = ZonedDateTime.of(2020, 1, 3, 0, 0, 0, 0, ZoneId.of(ClockService.ZONE_ID));
        ZonedDateTime thirdDate = ZonedDateTime.of(2020, 1, 6, 0, 0, 0, 0, ZoneId.of(ClockService.ZONE_ID));
        Collection<Transaction> result = transactionDAO.getTransactionsByUserMail("user2@mail.com");
        assertThat(result)
                .hasSize(3)
                .extracting("id").containsExactly(Optional.of(1), Optional.of(3), Optional.of(6));
        assertThat(result)
                .extracting("debtorID").containsExactly(1, 2, 3);
        assertThat(result)
                .extracting("creditorID").containsExactly(2, 3, 2);
        assertThat(result)
                .extracting("initialAmount").containsExactly(10.00, 10.0, 10.0);
        assertThat(result)
                .extracting("total").containsExactly(9.5, 9.5, 9.5);
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
        TransactionToSaveDTO transactionDTO = new TransactionToSaveDTO(
                "user4@mail.com",
                "user1@mail.com",
                newTransactionTime,
                50.00,
                47.50,
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
                .value("total").isEqualTo(47.50);

        TimestampWithTimeZone timestampWithTimeZoneExample =
                (TimestampWithTimeZone) transactionTable.getRow(6).getColumnValue("zoned_date_time").getValue();

        // Check that zoned_date_time in database perfectly match with passed ZonedDateTime object
        assertThat(timestampWithTimeZoneExample.getDay()).isEqualTo(newTransactionTime.getDayOfMonth());
        assertThat(timestampWithTimeZoneExample.getMonth()).isEqualTo(newTransactionTime.getMonthValue());
        assertThat(timestampWithTimeZoneExample.getYear()).isEqualTo(newTransactionTime.getYear());
        assertThat(timestampWithTimeZoneExample.getNanosSinceMidnight()).isEqualTo(newTransactionTime.getLong(ChronoField.NANO_OF_DAY));
        assertThat(timestampWithTimeZoneExample.getTimeZoneOffsetSeconds()).isEqualTo(newTransactionTime.getOffset().get(ChronoField.OFFSET_SECONDS));

    }

    @Test
    @FlywayTest
    @WithMockUser
    @DisplayName("makeTransaction() Exception")
    public void Given_databaseError_When_makeTransaction_Then_throwException() {
        TransactionToSaveDTO transactionDTO = new TransactionToSaveDTO(
                "user6@mail.com",
                "user1@mail.com",
                newTransactionTime,
                50.00,
                47.50,
                "transaction7");

        // User 6 doesn't exist
        assertThrows(DataAccessException.class, () -> transactionDAO.save(transactionDTO));
    }
}
