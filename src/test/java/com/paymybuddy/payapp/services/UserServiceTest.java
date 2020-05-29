package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.enums.Role;
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
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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

    private final String ENCODED_USERPASS_1 = "$2y$10$58Wl5KsCtAeuKkLesypM3uvGf2zgtg..TKvGCsMgnDjnsN3qGDxGK"; // userpass
    private final String ENCODED_USERPASS_2 = "$2y$10$V3fEhtWLjKUZhLCFfGe59.nyMW5LnFb2iHmRu16P9x0vVU/BQKUby"; // hackerpass


    @Test
    @DisplayName("getUserById() results")
    public void Given_idValue_When_findUserById_Then_returnDAOFindByIDResult() {
        Optional<User> user = Optional.of(new User("username",
                "user@mail.com",
                "userpass",
                Collections.singleton(Role.USER)));
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
    public void Given_idValue_When_findUserByMail_Then_returnDAOFindByMailResult() {
        Optional<User> user = Optional.of(new User("username",
                "user@mail.com",
                "userpass",
                Collections.singleton(Role.USER)));
        when(mockUserDAO.findByMail(anyString())).thenReturn(user);
        assertThat(userService.getUserByMail("user"))
                .isEqualTo(user);

        user = Optional.empty();
        when(mockUserDAO.findByMail(anyString())).thenReturn(user);
        assertThat(userService.getUserByMail("user"))
                .isEqualTo(user);
    }

    private Optional<User> userToUpdate() {
        return Optional.of(new User("username",
                "user@mail.com",
                ENCODED_USERPASS_1,
                Collections.singleton(Role.USER)));
    }

    @Test
    @DisplayName("updateSettings() success")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_validRequest_When_updateUserSettings_Then_doesNotThrowExceptions() throws SQLException {
        when(mockUserDAO.updateSettings(anyString(), anyString(), anyString(), anyString())).thenReturn(true);
        userService.updateSettings("userpass", "newUsername", "user@mail.com", null);
    }

    @Test
    @DisplayName("updateSettings() existing mail")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_existingMail_When_updateUserSettings_Then_throwsIllegalArgumentException() throws SQLException {

        when(mockUserDAO.findById(anyInt())).thenReturn(userToUpdate());
        when(mockUserDAO.updateSettings(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateSettings("userpass", "newUsername", "user@mail.com", ENCODED_USERPASS_1));
    }

    @Test
    @DisplayName("updateSettings() wrong password")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_invalidPassword_When_updateUserSettings_Then_throwsBadCredentialsException() {
        when(mockUserDAO.findById(anyInt())).thenReturn(userToUpdate());
        assertThrows(BadCredentialsException.class,
                () -> userService.updateSettings("wrongpass", "newUsername", "user@mail.com", ENCODED_USERPASS_1));
    }

    @Test
    @DisplayName("updateSettings() wrong params")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_wrongSettings_When_updateUserSettings_Then_throwsConstraintViolationException() {
        when(mockUserDAO.findById(anyInt())).thenReturn(userToUpdate());
        assertThrows(ConstraintViolationException.class,
                () -> userService.updateSettings("userpass", "username", "user", ENCODED_USERPASS_1));
    }

    @Test
    @DisplayName("updateSettings() server error")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_databaseError_When_updateUserSettings_Then_throwsSQLException() throws SQLException {
        when(mockUserDAO.findById(anyInt())).thenReturn(userToUpdate());
        when(mockUserDAO.updateSettings(anyString(), anyString(), anyString(), nullable(String.class))).thenThrow(SQLException.class);
        assertThrows(SQLException.class,
                () -> userService.updateSettings("userpass", "newUsername", "user@mail.com", ENCODED_USERPASS_1));
    }
}
