package com.paymybuddy.payapp.integration;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.User;
import org.assertj.db.type.Table;
import org.flywaydb.test.annotation.FlywayTest;
import org.flywaydb.test.junit5.FlywayTestExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.SQLException;
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


    @BeforeEach
    public void databaseSetup() throws SQLException {
        /*Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dataSource.getConnection();

            // Reset Transaction Table
            ps = connection.prepareStatement("DELETE FROM TRANSACTION;");
            ps.execute();

            // Reset Contact Table
            ps = connection.prepareStatement("DELETE FROM CONTACT;");
            ps.execute();

            // Reset User_Role Table
            ps = connection.prepareStatement("DELETE FROM USER_ROLE;");
            ps.execute();

            // Reset User Table
            ps = connection.prepareStatement("DELETE FROM USER;");
            ps.execute();

            // Insert users
            ps = connection.prepareStatement("INSERT INTO User (id, username, mail, password) " +
                    "VALUES (1, 'user1', 'user1@mail.com', 'user1pass')," +
                    "(2, 'user2', 'user2@mail.com', 'user2pass')," +
                    "(3, 'user3', 'user3@mail.com', 'user3pass')," +
                    "(4, 'user4', 'user4@mail.com', 'user4pass')"
            );
            ps.execute();

            // Insert roles
            ps = connection.prepareStatement(
                    "INSERT INTO User_Role (user_id, role_id) " +
                            "VALUES (1,1)," +
                            "(2,1)," +
                            "(3,1)," +
                            "(4,1)");
            ps.execute();

            // Insert contacts
            ps = connection.prepareStatement(
                    "INSERT INTO User_Role (user_id, role_id) " +
                            "VALUES (1,1)," +
                            "(2,1)," +
                            "(3,1)," +
                            "(4,1)");
            ps.execute();

            // Insert transactions
            ps = connection.prepareStatement(
                    "INSERT INTO Transaction (ID, DEBTOR_ID, CREDITOR_ID, DESCRIPTION, AMOUNT, TOTAL, ZONED_DATE_TIME)" +
                            "    VALUES (1,1,2,'transaction1',10.00,9.50,'2020-01-01 00:00:00+01')," +
                            "    (2,3,1,'transaction2',10.00,9.50,'2020-01-02 00:00:00+01')," +
                            "    (3,2,3,'transaction3',10.00,9.50,'2020-01-03 00:00:00+01')," +
                            "    (4,3,4,'transaction4',10.00,9.50,'2020-01-04 00:00:00+01')," +
                            "    (5,1,4,'transaction5',10.00,9.50,'2020-01-05 00:00:00+01')," +
                            "    (6,3,2,'transaction6',10.00,9.50,'2020-01-06 00:00:00+01')"
            );
            ps.execute();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            assert connection != null;
            connection.close();
            assert ps != null;
            ps.close();
        }*/
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
    @DisplayName("Get user balance success")
    public void Given_validUserMail_When_calculatingUserBalance_Then_retrieveValue() {
        Optional<Double> balance = userDAO.getBalance("user4@mail.com");

        assertThat(balance)
                .isNotNull()
                .isPresent();

        assertThat(balance.get())
                .isEqualTo(19);
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

}