package com.paymybuddy.payapp.integration;

import com.paymybuddy.payapp.daos.BankAccountDAO;
import com.paymybuddy.payapp.models.BankAccount;
import com.paymybuddy.payapp.models.BankOperation;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.Table;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.h2.api.TimestampWithTimeZone;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ExtendWith({FlywayTestExtension.class})
@SpringBootTest
@ActiveProfiles("test_h2")
@DisplayName("BankAccountDAO tests on : ")
public class BankAccountDAOSpringJdbcIT {
    @Autowired
    private BankAccountDAO bankAccountDAO;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    private final String ZONE_ID;

    private final ZonedDateTime operationDate;

    public BankAccountDAOSpringJdbcIT(@Value("${default.zoneID}") String ZONE_ID) {
        this.ZONE_ID = ZONE_ID;
        operationDate = ZonedDateTime.of(2020, 12, 25, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
    }

    @Test
    @FlywayTest
    @DisplayName("getBankAccounts() Successes")
    public void Given_userMail_When_getBankAccounts_Then_returnBankAccounts() {
        // Thorough test on most "complex" case
        Collection<BankAccount> result = bankAccountDAO.getBankAccounts("user1@mail.com");
        assertThat(result)
                .hasSize(2)
                .extracting("id").containsExactly(1, 2);
        assertThat(result)
                .extracting("ownerID").containsExactly(1, 1);
        assertThat(result)
                .extracting("ownerFullName").containsExactly(
                "user1 NAME",
                "user1 NAME");
        assertThat(result)
                .extracting("description").containsExactly(
                "main bank account",
                "second bank account");
        assertThat(result)
                .extracting("IBAN").containsExactly(
                "FR784356918887DW0BS628ARY69",
                "FR58664306261240R1H3658AT90");

        // Basic tests on others
        assertThat(bankAccountDAO.getBankAccounts("user2@mail.com"))
                .hasSize(1);
        assertThat(bankAccountDAO.getBankAccounts("user3@mail.com"))
                .hasSize(1);
        assertThat(bankAccountDAO.getBankAccounts("user4@mail.com"))
                .hasSize(1);
    }

    @Test
    @FlywayTest
    @DisplayName("getBankAccount() Success")
    public void Given_bankAccountID_When_getBankAccount_Then_returnAccordingBankAccount() {
        BankAccount result = bankAccountDAO.getBankAccount(5);
        BankAccount bankAccount5 = new BankAccount(5, 4, "user4 NAME", "my main bank account", "FR3522190814281GZ6G972RO306");
        assertThat(result).isEqualToComparingFieldByField(bankAccount5);
    }

    @Test
    @FlywayTest
    @DisplayName("getBankAccount() Exception")
    public void Given_inexistantBankAccountID_When_getBankAccount_Then_throwDataRetrievalException() {
        assertThrows(DataRetrievalFailureException.class, () -> bankAccountDAO.getBankAccount(7));
    }

    @Test
    @FlywayTest
    @DisplayName("save() Success")
    public void Given_validData_When_saveBankAccount_Then_tableTransactionUpdated() {
        Table bankAccountTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Bank_Account");

        Assertions.assertThat(bankAccountTable).hasNumberOfRows(5);

        assertThat(bankAccountDAO.save("user4@mail.com",
                "user4 NAME",
                "my side bank account",
                "FR2500398647619W635M30CFR37"))
                .isTrue();

        bankAccountTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Bank_Account");

        Assertions.assertThat(bankAccountTable)
                .hasNumberOfRows(6)
                .row(5)
                .value("user_id").isEqualTo(4)
                .value("owner_fullname").isEqualTo("user4 NAME")
                .value("description").isEqualTo("my side bank account")
                .value("IBAN").isEqualTo("FR2500398647619W635M30CFR37");
    }

    @Test
    @FlywayTest
    @DisplayName("save() Exception")
    public void Given_databaseError_When_saveBankAccount_Then_throwException() {
        // IBAN already used
        assertThrows(DataAccessException.class, () -> bankAccountDAO.save("user4@mail.com",
                "user4 NAME",
                "my third bank account",
                "FR784356918887DW0BS628ARY69"));
        // User 6 doesn't exist
        assertThrows(DataAccessException.class, () -> bankAccountDAO.save("user6@mail.com",
                "user6 NAME",
                "my inexistant bank account",
                "FR2500398647619W635M30CFR37"));
    }

    @Test
    @FlywayTest
    @DisplayName("update() Success")
    public void Given_validData_When_updateBankAccount_Then_tableTransactionUpdated() {
        Table bankAccountTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Bank_Account");

        Assertions.assertThat(bankAccountTable)
                .hasNumberOfRows(5)
                .row(2)
                .value("id").isEqualTo(3)
                .value("user_id").isEqualTo(2)
                .value("owner_fullname").isEqualTo("user2 NAME")
                .value("description").isEqualTo("my bank account")
                .value("IBAN").isEqualTo("FR975205942120TXG3F6799TZ87");

        assertThat(bankAccountDAO.update(3,
                "user2 NAME",
                "my new bank account",
                "FR2954066162259M8TE7EGBZR32"))
                .isTrue();

        bankAccountTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Bank_Account");

        Assertions.assertThat(bankAccountTable)
                .hasNumberOfRows(5)
                .row(2)
                .value("id").isEqualTo(3)
                .value("user_id").isEqualTo(2)
                .value("owner_fullname").isEqualTo("user2 NAME")
                .value("description").isEqualTo("my new bank account")
                .value("IBAN").isEqualTo("FR2954066162259M8TE7EGBZR32");
    }

    @Test
    @FlywayTest
    @DisplayName("update() Exception")
    public void Given_databaseError_When_updateBankAccount_Then_throwException() {
        // IBAN already used by bank account 1
        assertThrows(DataAccessException.class, () -> bankAccountDAO.update(3,
                "user2 NAME",
                "my stealed bank account",
                "FR784356918887DW0BS628ARY69"));
        // BankAccount 6 doesn't exist
        assertThrows(DataAccessException.class, () -> bankAccountDAO.update(6,
                "user6 NAME",
                "my inexistant bank account",
                "FR2500398647619W635M30CFR37"));
    }

    @Test
    @FlywayTest
    @DisplayName("delete() Success")
    public void Given_validData_When_deleteBankAccount_Then_tableTransactionUpdated() {
        Table bankAccountTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Bank_Account");

        Assertions.assertThat(bankAccountTable)
                .hasNumberOfRows(5)
                .row(3)
                .value("id").isEqualTo(4)
                .value("user_id").isEqualTo(3)
                .value("owner_fullname").isEqualTo("user3 NAME")
                .value("description").isEqualTo("my account from this bank")
                .value("IBAN").isEqualTo("FR6300846780983EVO96L558099");

        assertThat(bankAccountDAO.delete(4))
                .isTrue();

        bankAccountTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Bank_Account");

        Assertions.assertThat(bankAccountTable)
                .hasNumberOfRows(4)
                .row(3)
                .value("id").isEqualTo(5)
                .value("user_id").isEqualTo(4)
                .value("owner_fullname").isEqualTo("user4 NAME")
                .value("description").isEqualTo("my main bank account")
                .value("IBAN").isEqualTo("FR3522190814281GZ6G972RO306");
    }

    @Test
    @FlywayTest
    @DisplayName("delete() Exception")
    public void Given_databaseError_When_deleteBankAccount_Then_throwException() {
        // BankAccount 6 doesn't exist
        assertThrows(DataAccessException.class, () -> bankAccountDAO.delete(6));
    }

    @Test
    @FlywayTest
    @DisplayName("getBankOperations() Successes")
    public void Given_bankAccountID_When_getBankOperations_Then_returnBankOperations() {
        // Thorough test on most "complex" case
        ZonedDateTime firstOpDate = ZonedDateTime.of(2020, 4, 9, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        ZonedDateTime secondOpDate = ZonedDateTime.of(2020, 10, 17, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        Collection<BankOperation> result = bankAccountDAO.getBankOperations(3);
        assertThat(result)
                .hasSize(2)
                .extracting("id").containsExactly(3, 6);
        assertThat(result)
                .extracting("bankAccountID").containsExactly(3, 3);
        assertThat(result)
                .extracting("amount").containsExactly(
                BigDecimal.valueOf(30.0),
                BigDecimal.valueOf(-50.0));
        assertThat(result).extracting("date")
                .element(0).usingRecursiveComparison().isEqualTo(firstOpDate);
        assertThat(result).extracting("date")
                .element(1).usingRecursiveComparison().isEqualTo(secondOpDate);

        // Basic tests on others
        assertThat(bankAccountDAO.getBankOperations(1))
                .hasSize(1);
        assertThat(bankAccountDAO.getBankOperations(2))
                .hasSize(1);
        assertThat(bankAccountDAO.getBankOperations(4))
                .hasSize(2);
    }

    @Test
    @FlywayTest
    @DisplayName("saveTransfer() Success")
    public void Given_validData_When_saveTransfer_Then_tableTransactionUpdated() {
        Table bankOperationTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Bank_Operation");

        Assertions.assertThat(bankOperationTable).hasNumberOfRows(6);

        assertThat(bankAccountDAO.saveTransferOperation(1,
                operationDate,
                new BigDecimal(50)))
                .isTrue();

        bankOperationTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Bank_Operation");

        Assertions.assertThat(bankOperationTable)
                .hasNumberOfRows(7)
                .row(6)
                .value("id").isEqualTo(7)
                .value("bank_account_id").isEqualTo(1)
                .value("amount").isEqualTo(-50);

        TimestampWithTimeZone timestampWithTimeZoneExample =
                (TimestampWithTimeZone) bankOperationTable.getRow(6).getColumnValue("date").getValue();

        // Check that zoned_date_time in database perfectly match with passed ZonedDateTime object
        assertThat(timestampWithTimeZoneExample.getDay()).isEqualTo(25);
        assertThat(timestampWithTimeZoneExample.getMonth()).isEqualTo(12);
        assertThat(timestampWithTimeZoneExample.getYear()).isEqualTo(2020);
        assertThat(timestampWithTimeZoneExample.getNanosSinceMidnight()).isEqualTo(0);
        assertThat(timestampWithTimeZoneExample.getTimeZoneOffsetSeconds()).isEqualTo(operationDate.getOffset().get(ChronoField.OFFSET_SECONDS));
    }

    @Test
    @FlywayTest
    @DisplayName("saveTransfer() Exception")
    public void Given_databaseError_When_saveTransfer_Then_throwException() {
        // BankAccount 6 doesn't exist
        assertThrows(DataAccessException.class, () -> bankAccountDAO.saveTransferOperation(6,
                operationDate,
                new BigDecimal(40)));
    }


    @Test
    @FlywayTest
    @DisplayName("saveWithdraw() Success")
    public void Given_authenticatedUser_When_saveWithdraw_Then_tableTransactionUpdated() {
        Table bankOperationTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Bank_Operation");

        Assertions.assertThat(bankOperationTable).hasNumberOfRows(6);

        assertThat(bankAccountDAO.saveWithdrawOperation(2,
                operationDate,
                new BigDecimal(60)))
                .isTrue();

        bankOperationTable = new Table(jdbcTemplate.getJdbcTemplate().getDataSource(), "Bank_Operation");

        Assertions.assertThat(bankOperationTable)
                .hasNumberOfRows(7)
                .row(6)
                .value("id").isEqualTo(7)
                .value("bank_account_id").isEqualTo(2)
                .value("amount").isEqualTo(60);

        TimestampWithTimeZone timestampWithTimeZoneExample =
                (TimestampWithTimeZone) bankOperationTable.getRow(6).getColumnValue("date").getValue();

        // Check that zoned_date_time in database perfectly match with passed ZonedDateTime object
        assertThat(timestampWithTimeZoneExample.getDay()).isEqualTo(25);
        assertThat(timestampWithTimeZoneExample.getMonth()).isEqualTo(12);
        assertThat(timestampWithTimeZoneExample.getYear()).isEqualTo(2020);
        assertThat(timestampWithTimeZoneExample.getNanosSinceMidnight()).isEqualTo(0);
        assertThat(timestampWithTimeZoneExample.getTimeZoneOffsetSeconds()).isEqualTo(operationDate.getOffset().get(ChronoField.OFFSET_SECONDS));
    }

    @Test
    @FlywayTest
    @DisplayName("saveWithdraw() Exception")
    public void Given_databaseError_When_saveWithdraw_Then_throwException() {
        // BankAccount 6 doesn't exist
        assertThrows(DataAccessException.class, () -> bankAccountDAO.saveWithdrawOperation(6,
                operationDate,
                new BigDecimal(40)));
    }
}
