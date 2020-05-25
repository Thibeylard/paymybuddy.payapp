package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("User Service tests on : ")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserDAO mockUserDAO;

    @Test
    @DisplayName("getUserById() results")
    public void Given_idValue_When_findUserById_Then_returnDAOFindByIDResult() throws SQLException {
        Optional<User> user = Optional.of(new User(3));
        when(mockUserDAO.findById(anyInt())).thenReturn(user);
        assertThat(userService.getUserById(3))
                .isEqualTo(user);

        user = Optional.empty();
        when(mockUserDAO.findById(anyInt())).thenReturn(user);
        assertThat(userService.getUserById(3))
                .isEqualTo(user);
    }

    @Test
    @DisplayName("getUserByMail() results")
    public void Given_idValue_When_findUserByMail_Then_returnDAOFindByMailResult() throws SQLException {
        Optional<User> user = Optional.of(new User(3));
        when(mockUserDAO.findByMail(anyString())).thenReturn(user);
        assertThat(userService.getUserByMail("user"))
                .isEqualTo(user);

        user = Optional.empty();
        when(mockUserDAO.findByMail(anyString())).thenReturn(user);
        assertThat(userService.getUserByMail("user"))
                .isEqualTo(user);
    }

    @Test
    @DisplayName("updateSettings() success")
    @WithMockUser(username = "user", password = "userpass")
    // SecurityContext is needed to check password before modification
    public void Given_validRequest_When_updateUserSettings_Then_doesNotThrowExceptions() throws SQLException {
        userService.updateSettings("userpass", "user@mail.com", "newUsername", "");
    }

    @Test
    @DisplayName("updateSettings() wrong password")
    @WithMockUser(username = "user", password = "userpass")
    public void Given_invalidPassword_When_updateUserSettings_Then_throwsBadCredentialsException() {
        assertThrows(BadCredentialsException.class,
                () -> userService.updateSettings("wrongpass", "user@mail.com", "newUsername", ""));
    }

    @Test
    @DisplayName("updateSettings() wrong params")
    @WithMockUser(username = "user", password = "userpass")
    public void Given_wrongSettings_When_updateUserSettings_Then_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class,
                () -> userService.updateSettings("userpass", "user", "newUsername", ""));
    }

    @Test
    @DisplayName("updateSettings() server error")
    @WithMockUser(username = "user", password = "userpass")
    public void Given_databaseError_When_updateUserSettings_Then_throwsSQLException() throws SQLException {
        when(mockUserDAO.updateSettings(anyString(), anyString(), anyString())).thenThrow(SQLException.class);
        assertThrows(SQLException.class,
                () -> userService.updateSettings("userpass", "user", "newUsername", ""));
    }
}
