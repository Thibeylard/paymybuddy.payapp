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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.nullable;
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


    @Test
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    @DisplayName("getUserByMail() results")
    public void Given_authenticatedUser_When_getUserByMail_Then_returnDAOFindResult() {
        Optional<User> user = Optional.of(new User("username",
                "user@mail.com",
                "userpass",
                Collections.singleton(Role.USER)));
        when(mockUserDAO.find(anyString())).thenReturn(user);
        assertThat(userService.getUserByMail())
                .isEqualTo(user);

        user = Optional.empty();
        when(mockUserDAO.find(anyString())).thenReturn(user);
        assertThat(userService.getUserByMail())
                .isEqualTo(user);
    }

    @Test
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    @DisplayName("getUserBalance() results")
    public void Given_authenticatedUser_When_getUserBalancel_Then_returnDAOFindResult() {
        Optional<BigDecimal> balance = Optional.of(BigDecimal.valueOf(50.00));

        when(mockUserDAO.getBalance(anyString())).thenReturn(balance);
        assertThat(userService.getUserBalance())
                .isEqualTo(balance);

        balance = Optional.empty();
        when(mockUserDAO.getBalance(anyString())).thenReturn(balance);
        assertThat(userService.getUserBalance())
                .isEqualTo(balance);
    }

    @Test
    @DisplayName("updateUserProfile() success")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_validRequest_When_updateUserSettings_Then_doesNotThrowExceptions() throws SQLException {
        when(mockUserDAO.update(anyString(), anyString(), anyString(), anyString())).thenReturn(true);
        userService.updateUserProfile("userpass", "newUsername", "user@mail.com", null);
    }

    @Test
    @DisplayName("updateUserProfile() existing mail")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_existingMail_When_updateUserSettings_Then_throwsIllegalArgumentException() throws SQLException {

        when(mockUserDAO.update(anyString(), anyString(), anyString(), anyString()))
                .thenThrow(IllegalArgumentException.class);
        assertThrows(IllegalArgumentException.class,
                () -> userService.updateUserProfile("userpass", "newUsername", "user@mail.com", ENCODED_USERPASS_1));
    }

    @Test
    @DisplayName("updateUserProfile() wrong password")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_invalidPassword_When_updateUserSettings_Then_throwsBadCredentialsException() {
        assertThrows(BadCredentialsException.class,
                () -> userService.updateUserProfile("wrongpass", "newUsername", "user@mail.com", ENCODED_USERPASS_1));
    }

    @Test
    @DisplayName("updateUserProfile() wrong params")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_wrongSettings_When_updateUserSettings_Then_throwsConstraintViolationException() {
        assertThrows(ConstraintViolationException.class,
                () -> userService.updateUserProfile("userpass", "username", "user", ENCODED_USERPASS_1));
    }

    @Test
    @DisplayName("updateUserProfile() server error")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_databaseError_When_updateUserSettings_Then_throwsSQLException() throws SQLException {
        when(mockUserDAO.update(anyString(), anyString(), anyString(), nullable(String.class))).thenThrow(SQLException.class);
        assertThrows(SQLException.class,
                () -> userService.updateUserProfile("userpass", "newUsername", "user@mail.com", ENCODED_USERPASS_1));
    }
}
