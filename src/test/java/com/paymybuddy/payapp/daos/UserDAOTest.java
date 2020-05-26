package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.User;
import org.assertj.db.type.Table;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.db.api.Assertions.assertThat;
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

            // Reset User_Role Table
            ps = connection.prepareStatement(
                    "DELETE FROM User_Role;"
            );
            ps.execute();

            // Reset User Table
            ps = connection.prepareStatement(
                    "DELETE FROM User;"
            );
            ps.execute();


            // Insert User 'user'
            ps = connection.prepareStatement(
                    "INSERT INTO User (id, username, mail, password) " +
                            "VALUES (1, 'username', 'user@mail.com', 'userpass')," +
                            "(2, 'otherUser', 'otherUser@mail.com', 'otherUserpass')"
            );
            ps.execute();

            // Insert role for 'user'
            ps = connection.prepareStatement(
                    "INSERT INTO User_Role (user_id, role_id) " +
                            "VALUES (1,1)," +
                            "(2,1)"
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
        Optional<User> user = userDAO.findByMail("user@mail.com");
        assertThat(user)
                .isNotNull()
                .isPresent();
        assertThat(user.get().getUsername()).isEqualTo("username");
        assertThat(user.get().getMail()).isEqualTo("user@mail.com");
        assertThat(user.get().getPassword()).isEqualTo("userpass");
        assertThat(user.get().getRoles())
                .hasSize(1)
                .contains(Role.USER);
    }

    @Test
    @DisplayName("Get user by mail failure")
    public void Given_invalidUserMail_When_searchingForUser_Then_getEmptyUser() {
        assertThat(userDAO.findByMail("john@mail.com"))
                .isNotNull()
                .isNotPresent();
    }

    @Test
    @DisplayName("Save user success")
    public void Given_availableMail_When_savingUser_Then_userIsStoredInDatabase() throws SQLException {
        assertThat(userDAO.save("username2", "user2@mail.com", "user2pass"))
                .isTrue();

        Table userTable = new Table(dataSource, "User");
        assertThat(userTable).row(2)
                .value("username").isEqualTo("username2")
                .value("mail").isEqualTo("user2@mail.com")
                .value("password").isEqualTo("user2pass");
    }

    @Test
    @DisplayName("Save user failure : redundant email")
    public void Given_existingMail_When_savingUser_Then_IllegalArgumentExceptionThrown() throws IllegalArgumentException {
        assertThrows(IllegalArgumentException.class, () -> userDAO.save("username", "user@mail.com", "userpass"));
    }

    @Test
    @DisplayName("Update settings success")
    public void Given_availableMail_When_updatingUser_Then_userSuccessfullyModified() throws SQLException {
        assertThat(userDAO.updateSettings(1, "username2", "user2@mail.com", "userpass"))
                .isTrue();
        Table userTable = new Table(dataSource, "User");
        assertThat(userTable).row(0)
                .value("username").isEqualTo("username2")
                .value("mail").isEqualTo("user2@mail.com")
                .value("password").isEqualTo("userpass");
    }

    @Test
    @DisplayName("Update settings failure : redundant email")
    public void Given_existingMail_When_updatingUser_Then_SQLExceptionThrown() {
        assertThrows(SQLException.class, () -> userDAO.updateSettings(2, "otherUser", "user@mail.com", "newOtherPass"));
    }

}
