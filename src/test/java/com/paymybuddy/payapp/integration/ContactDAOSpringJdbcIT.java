package com.paymybuddy.payapp.integration;

import com.paymybuddy.payapp.daos.ContactDAO;
import org.assertj.db.api.Assertions;
import org.assertj.db.type.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.security.test.context.support.WithMockUser;
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
@DisplayName("ContactDAO tests on : ")
public class ContactDAOSpringJdbcIT {
    @Autowired
    private ContactDAO contactDAO;

    @Autowired
    private DataSource dataSource;

    @BeforeEach
    public void databaseSetup() throws SQLException {
        Connection connection = null;
        PreparedStatement ps = null;

        try {
            connection = dataSource.getConnection();

            // Reset Contact Table
            ps = connection.prepareStatement(
                    "DELETE FROM Contact;"
            );
            ps.execute();

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


            // Insert users
            ps = connection.prepareStatement(
                    "INSERT INTO User (id, username, mail, password) " +
                            "VALUES (1, 'user1', 'user1@mail.com', 'user1pass')," +
                            "(2, 'user2', 'user2@mail.com', 'user2pass')," +
                            "(3, 'user3', 'user3@mail.com', 'user3pass')," +
                            "(4, 'user4', 'user4@mail.com', 'user4pass')"
            );
            ps.execute();

            // Insert roles for users
            ps = connection.prepareStatement(
                    "INSERT INTO User_Role (user_id, role_id) " +
                            "VALUES (1,1)," +
                            "(2,1)," +
                            "(3,1)," +
                            "(4,1)"
            );
            ps.execute();

            // Insert contacts between users
            ps = connection.prepareStatement(
                    "INSERT INTO Contact (user_a_id, user_b_id) " +
                            "VALUES (1,2)," +
                            "(1,3)," +
                            "(2,3)," +
                            "(4,1)"
            );
            ps.execute();

        } catch (DataAccessException e) {
            e.printStackTrace();
        } finally {
            assert connection != null;
            connection.close();
            assert ps != null;
            ps.close();
        }
    }

    @Test
    @WithMockUser
    @DisplayName("getContacts() Success")
    public void Given_userId_When_getUserContacts_Then_returnUserContacts() throws Exception {
        assertThat(contactDAO.getContactsByUserId(1))
                .hasSize(3);
        assertThat(contactDAO.getContactsByUserId(2))
                .hasSize(2);
        assertThat(contactDAO.getContactsByUserId(3))
                .hasSize(2);
        assertThat(contactDAO.getContactsByUserId(4))
                .hasSize(1);
    }

    @Test
    @WithMockUser
    @DisplayName("addContact() Success")
    public void Given_userIdAndContactMail_When_addContact_Then_correspondingElementAddedToContactTable() throws Exception {
        Table contactTable = new Table(dataSource, "Contact");

        Assertions.assertThat(contactTable).hasNumberOfRows(4);

        assertThat(contactDAO.save(4, "user2@mail.com"))
                .isTrue();

        contactTable = new Table(dataSource, "Contact");

        Assertions.assertThat(contactTable)
                .hasNumberOfRows(5)
                .row(4)
                .value("user_a_id").isEqualTo(4)
                .value("user_b_id").isEqualTo(2);
    }

    @Test
    @WithMockUser
    @DisplayName("addContact() Exceptions")
    public void Given_databaseError_When_addContact_Then_throwsDataAccessException() throws Exception {
        assertThrows(DataAccessException.class, () -> contactDAO.save(1, "user2@mail.com")); // Already contacts
        assertThrows(DataAccessException.class, () -> contactDAO.save(6, "user2@mail.com")); // User 6 doesn't exist
        assertThrows(DataAccessException.class, () -> contactDAO.save(1, "user8@mail.com")); // User 8 doesn't exist
    }

    @Test
    @WithMockUser
    @DisplayName("deleteContact() Success")
    public void Given_userIdAndContactMail_When_deleteContact_Then_correspondingElementDeletedToContactTable() throws Exception {
        Table contactTable = new Table(dataSource, "Contact");

        Assertions.assertThat(contactTable)
                .hasNumberOfRows(4)
                .row(1)  // Second row is |1|3|
                .value("user_a_id").isEqualTo(1)
                .value("user_b_id").isEqualTo(3);

        assertThat(contactDAO.delete(3, "user1@mail.com")) // Params inverted to delete row |1|3|
                .isTrue();

        contactTable = new Table(dataSource, "Contact");

        Assertions.assertThat(contactTable)
                .hasNumberOfRows(3)
                .row(1)  // Second row |1|3| has been deleted. So third row |2|3| is supposed to be second now
                .value("user_a_id").isEqualTo(2)
                .value("user_b_id").isEqualTo(3);

        assertThat(contactDAO.delete(2, "user3@mail.com")) // Params to delete row |2|3|
                .isTrue();

        contactTable = new Table(dataSource, "Contact");

        Assertions.assertThat(contactTable)
                .hasNumberOfRows(2)
                .row(1)  // Second row |2|3| has been deleted. So third row |4|1| is supposed to be second now
                .value("user_a_id").isEqualTo(4)
                .value("user_b_id").isEqualTo(1);
    }

    @Test
    @WithMockUser
    @DisplayName("deleteContact() Exceptions")
    public void Given_databaseError_When_deleteContact_Then_throwsDataAccessException() throws Exception {
        assertThrows(DataAccessException.class, () -> contactDAO.delete(4, "user3@mail.com")); // Not contacts
        assertThrows(DataAccessException.class, () -> contactDAO.save(6, "user2@mail.com")); // User 6 doesn't exist at all
        assertThrows(DataAccessException.class, () -> contactDAO.save(1, "user8@mail.com")); // User 8 doesn't exist at all
    }
}
