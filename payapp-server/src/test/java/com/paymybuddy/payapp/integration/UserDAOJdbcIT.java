package com.paymybuddy.payapp.integration;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.Bill;
import com.paymybuddy.payapp.models.User;
import org.assertj.db.type.Table;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.db.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ExtendWith({FlywayTestExtension.class})
@SpringBootTest
@ActiveProfiles("test_h2")
@DisplayName("User DAO tests on : ")
public class UserDAOJdbcIT {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private DataSource dataSource;

    private final String ZONE_ID;

    public UserDAOJdbcIT(@Value("${default.zoneID}") String ZONE_ID) {
        this.ZONE_ID = ZONE_ID;
    }

    @Test
    @FlywayTest
    @DisplayName("Get user by mail success")
    public void Given_validUserMail_When_searchingForUser_Then_retrieveCorrespondingUser() {
        Optional<User> user = userDAO.find("user1@mail.com");
        assertThat(user)
                .isNotNull()
                .isPresent();
        assertThat(user.get().getUsername()).isEqualTo("user1");
        assertThat(user.get().getMail()).isEqualTo("user1@mail.com");
        assertThat(user.get().getPassword()).isEqualTo("user1pass");
        assertThat(user.get().getRoles())
                .hasSize(1)
                .contains(Role.USER);
    }

    @Test
    @FlywayTest
    @DisplayName("Get user by mail failure")
    public void Given_invalidUserMail_When_searchingForUser_Then_getEmptyUser() {
        assertThat(userDAO.find("john@mail.com"))
                .isNotNull()
                .isNotPresent();
    }

    @Test
    @FlywayTest
    @DisplayName("Get user balance success") // Test based on data from migration 6.1
    public void Given_validUserMail_When_calculatingUserBalance_Then_retrieveValue() {
        Optional<BigDecimal> balance = userDAO.getBalance("user4@mail.com");

        assertThat(balance)
                .isNotNull()
                .isPresent();

        assertThat(balance.get())
                .isEqualTo(BigDecimal.valueOf(100.00));

        balance = userDAO.getBalance("user3@mail.com");

        assertThat(balance)
                .isNotNull()
                .isPresent();

        assertThat(balance.get())
                .isEqualTo(BigDecimal.valueOf(155.0));

        balance = userDAO.getBalance("user2@mail.com");

        assertThat(balance)
                .isNotNull()
                .isPresent();

        assertThat(balance.get())
                .isEqualTo(BigDecimal.valueOf(-10.00));

        balance = userDAO.getBalance("user1@mail.com");

        assertThat(balance)
                .isNotNull()
                .isPresent();

        assertThat(balance.get())
                .isEqualTo(BigDecimal.valueOf(10.0));
    }

    @Test
    @FlywayTest
    @DisplayName("Get user balance failure")
    public void Given_databaseError_When_calculatingUserBalance_Then_getEmptyDouble() {
        assertThat(userDAO.getBalance("john@mail.com"))
                .isNotNull()
                .isNotPresent();
    }

    @Test
    @FlywayTest
    @DisplayName("Save user success")
    public void Given_availableMail_When_savingUser_Then_userIsStoredInDatabase() throws SQLException {
        User user = new User("user5", "user5@mail.com", "user5pass", Collections.singletonList(Role.USER));
        assertThat(userDAO.save(user))
                .isTrue();

        Table userTable = new Table(dataSource, "User");
        assertThat(userTable).row(4)
                .value("username").isEqualTo("user5")
                .value("mail").isEqualTo("user5@mail.com")
                .value("password").isEqualTo("user5pass");
    }

    @Test
    @FlywayTest
    @DisplayName("Save user failure : redundant email")
    public void Given_existingMail_When_savingUser_Then_IllegalArgumentExceptionThrown() throws IllegalArgumentException {
        User user = new User("user1", "user1@mail.com", "user1pass", Collections.singletonList(Role.USER));
        assertThrows(IllegalArgumentException.class, () -> userDAO.save(user));
    }

    @Test
    @FlywayTest
    @DisplayName("Update profile success")
    public void Given_availableMail_When_updatingUser_Then_userSuccessfullyModified() throws SQLException {
        assertThat(userDAO.update(
                "user1@mail.com",
                "user6",
                "user6@mail.com",
                "user1pass"))
                .isTrue();

        Table userTable = new Table(dataSource, "User");
        assertThat(userTable).row(0)
                .value("username").isEqualTo("user6")
                .value("mail").isEqualTo("user6@mail.com")
                .value("password").isEqualTo("user1pass");
    }

    @Test
    @FlywayTest
    @DisplayName("Update profile failure : redundant email")
    public void Given_existingMail_When_updatingUser_Then_IllegalArgumentExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> userDAO.update(
                "user1@mail.com",
                "user1",
                "user2@mail.com", // Existing mail
                "newOtherPass"));
    }

    @Test
    @FlywayTest
    @DisplayName("Get user bills return values")
    public void Given_validUserMail_When_getUserBills_Then_retrieveCorrespondingBills() {

        // User 1 has one bill : Must return it in collection with good values
        ArrayList<Bill> bills = (ArrayList<Bill>) userDAO.getBills("user1@mail.com");
        assertThat(bills)
                .isNotNull()
                .hasSize(1);

        Table billTable = new Table(dataSource, "Bill");

        Timestamp creationDateTs =
                (Timestamp) billTable.getRow(0).getColumnValue("creation_date").getValue();
        Timestamp startDateTs =
                (Timestamp) billTable.getRow(0).getColumnValue("start_date").getValue();
        Timestamp endDateTs =
                (Timestamp) billTable.getRow(0).getColumnValue("end_date").getValue();

        // Check BillDTO IDs
        assertThat(bills.get(0).getId())
                .isPresent()
                .get().isEqualTo(1);
        assertThat(bills.get(0).getUserID()).isEqualTo(1);

        // Compare BillDTO ZonedDateTimes as LocalDateTimes to Bill table DateTime values
        assertThat(creationDateTs.toLocalDateTime()).isEqualTo(bills.get(0).getCreationDate().toLocalDateTime());
        assertThat(startDateTs.toLocalDateTime()).isEqualTo(bills.get(0).getStartDate().toLocalDateTime());
        assertThat(endDateTs.toLocalDateTime()).isEqualTo(bills.get(0).getEndDate().toLocalDateTime());

        // Check BillDTO Total
        assertThat(bills.get(0).getTotal())
                .isPresent()
                .get().isEqualTo(BigDecimal.valueOf(0.25));


        // User 2 has no bills : Must return empty collection
        assertThat(userDAO.getBills("user2@mail.com"))
                .isNotNull()
                .hasSize(0);

        // User 7 doesn't exist : Return empty
        assertThat(userDAO.getBills("user7@mail.com"))
                .isNotNull()
                .hasSize(0);

    }

    @Test
    @FlywayTest
    @DisplayName("Create user bill success")
    public void Given_validParams_When_createUserBill_Then_saveAndReturnBill() throws Exception {
        ZonedDateTime startDate = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        ZonedDateTime endDate = ZonedDateTime.of(2020, 2, 1, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        ZonedDateTime creationDate = ZonedDateTime.of(2020, 2, 10, 0, 0, 0, 0, ZoneId.of(ZONE_ID));
        Bill billToSave = new Bill(1, creationDate, startDate, endDate);

        Bill result = userDAO.saveBill(billToSave);

        assertThat(result.getUserID()).isEqualTo(billToSave.getUserID());
        assertThat(result.getCreationDate()).isEqualTo(billToSave.getCreationDate());
        assertThat(result.getStartDate()).isEqualTo(billToSave.getStartDate());
        assertThat(result.getEndDate()).isEqualTo(billToSave.getEndDate());

        Table billTable = new Table(dataSource, "Bill");

        assertThat(billTable).hasNumberOfRows(2);

        Timestamp creationDateTs =
                (Timestamp) billTable.getRow(1).getColumnValue("creation_date").getValue();
        Timestamp startDateTs =
                (Timestamp) billTable.getRow(1).getColumnValue("start_date").getValue();
        Timestamp endDateTs =
                (Timestamp) billTable.getRow(1).getColumnValue("end_date").getValue();

        // Check BillDTO IDs
        assertThat(billTable)
                .row(1)
                .value("id")
                .isEqualTo(2);

        assertThat(billTable)
                .row(1)
                .value("user_id")
                .isEqualTo(1);

        // Compare BillDTO ZonedDateTimes as LocalDateTimes to Bill table DateTime values
        assertThat(creationDateTs.toLocalDateTime()).isEqualTo(result.getCreationDate().toLocalDateTime());
        assertThat(startDateTs.toLocalDateTime()).isEqualTo(result.getStartDate().toLocalDateTime());
        assertThat(endDateTs.toLocalDateTime()).isEqualTo(result.getEndDate().toLocalDateTime());

        // Check BillDTO Total
        assertThat(billTable)
                .row(1)
                .value("total")
                .isEqualTo(BigDecimal.valueOf(0.10));
    }

}