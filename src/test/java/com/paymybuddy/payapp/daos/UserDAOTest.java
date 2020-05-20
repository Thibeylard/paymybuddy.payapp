package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.config.DatabaseConfiguration;
import com.paymybuddy.payapp.models.User;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("User DAO tests on : ")
public class UserDAOTest {

    @SpyBean
    private UserDAO spyUserDAO;

    @MockBean
    private DatabaseConfiguration mockDbConfig;

    @Mock
    private Connection mockCon;

    @Mock
    private PreparedStatement mockPs;

    @Mock
    private ResultSet mockRs;

    @Test
    public void Given_validUserMail_When_searchingForUser_Then_retrieveCorrespondingUser() throws SQLException {
        User user = new User(5674)
                .withUsername("Philip")
                .withMail("philip@mail.com")
                .withPassword("philippass");

        when(mockDbConfig.getConnection()).thenReturn(mockCon);
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(true);
        when(mockRs.getInt("id")).thenReturn(5674);
        when(mockRs.getString("username")).thenReturn("Philip");
        when(mockRs.getString("mail")).thenReturn("philip@mail.com");
        when(mockRs.getString("password")).thenReturn("philippass");


        assertThat(spyUserDAO.findByMail("philip@mail.com"))
                .isNotNull()
                .isPresent()
                .get()
                .isEqualToComparingFieldByField(user);

        verify(mockDbConfig).getConnection();
        verify(mockCon).prepareStatement(anyString());
        verify(mockPs).executeQuery();
        verify(mockRs).next();
    }

    @Test
    public void Given_invalidUserMail_When_searchingForUser_Then_getEmptyUser() throws SQLException {
        when(mockDbConfig.getConnection()).thenReturn(mockCon);
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.executeQuery()).thenReturn(mockRs);
        when(mockRs.next()).thenReturn(false);

        assertThat(spyUserDAO.findByMail("john@mail.com"))
                .isNotNull()
                .isNotPresent();
    }

    @Test
    public void Given_databaseError_When_searchingForUser_Then_getEmptyUser() throws SQLException {
        when(mockDbConfig.getConnection()).thenReturn(mockCon);
        when(mockCon.prepareStatement(anyString())).thenThrow(SQLException.class);

        assertThat(spyUserDAO.findByMail("error@mail.com"))
                .isNotNull()
                .isNotPresent();
    }

    @Test
    public void Given_availableMail_When_savingUser_Then_noIllegalArgumentExceptionThrown() throws SQLException {
        doReturn(Optional.empty()).when(spyUserDAO).findByMail(anyString());
        when(mockDbConfig.getConnection()).thenReturn(mockCon);
        when(mockCon.prepareStatement(anyString())).thenReturn(mockPs);
        when(mockPs.execute()).thenReturn(true);

        spyUserDAO.saveUser("userToSave", "user@mail.com", "userpass");
        verify(spyUserDAO).findByMail(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void Given_existingMail_When_savingUser_Then_IllegalArgumentExceptionThrown() throws SQLException {
        doReturn(Optional.of(new User(12474))).when(spyUserDAO).findByMail(anyString());

        spyUserDAO.saveUser("userToSave", "user@mail.com", "userpass");

        verify(spyUserDAO).findByMail(anyString());

    }

    @Test(expected = SQLException.class)
    public void Given_databaseError_When_savingUser_Then_SQLExceptionThrown() throws SQLException {
        doReturn(Optional.empty()).when(spyUserDAO).findByMail(anyString());
        when(mockDbConfig.getConnection()).thenReturn(mockCon);
        when(mockCon.prepareStatement(anyString())).thenThrow(SQLException.class);

        spyUserDAO.saveUser("userToSave", "user@mail.com", "userpass");
    }
}
