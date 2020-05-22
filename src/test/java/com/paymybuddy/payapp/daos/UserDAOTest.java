package com.paymybuddy.payapp.daos;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test_h2")
@DisplayName("User DAO tests on : ")
public class UserDAOTest {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void databaseSetup() throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;
        try {
            connection = dataSource.getConnection();
            ps = connection.prepareStatement(
                    "INSERT INTO User (username, mail, password) " +
                            "VALUES ('user', 'user@mail.com', 'userpass')"
            );
            ps.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            assert connection != null;
            connection.close();
            assert ps != null;
            ps.close();
        }
    }

    @Test
    @DisplayName("Get user by mail success")
    public void Given_validUserMail_When_searchingForUser_Then_retrieveCorrespondingUser() {
        assertThat(userDAO.findByMail("user@mail.com"))
                .isNotNull()
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("username", "user")
                .hasFieldOrPropertyWithValue("mail", "user@mail.com")
                .hasFieldOrPropertyWithValue("password", "userpass");
    }

    @Test
    @DisplayName("Get user by mail fail")
    public void Given_invalidUserMail_When_searchingForUser_Then_getEmptyUser() {
        assertThat(userDAO.findByMail("john@mail.com"))
                .isNotNull()
                .isNotPresent();
    }

    @Test
    @DisplayName("Save user success")
    public void Given_availableMail_When_savingUser_Then_userIsStoredInDatabase() throws SQLException {
        assertThat(userDAO.saveUser("user2", "user2@mail.com", "user2pass"))
                .isTrue();

        assertThat(userDAO.findByMail("user2@mail.com"))
                .isNotNull()
                .isPresent()
                .get()
                .hasFieldOrPropertyWithValue("username", "user2")
                .hasFieldOrPropertyWithValue("mail", "user2@mail.com")
                .hasFieldOrPropertyWithValue("password", "user2pass");
    }

    @Test
    @DisplayName("Save user fail : redundant email")
    public void Given_existingMail_When_savingUser_Then_IllegalArgumentExceptionThrown() throws SQLException, IllegalArgumentException {
        assertThrows(IllegalArgumentException.class, () -> userDAO.saveUser("user", "user@mail.com", "userpass"));
    }

}
