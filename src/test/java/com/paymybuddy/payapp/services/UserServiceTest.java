package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.dtos.BillDTO;
import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.validation.ConstraintViolationException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.Collection;
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

    @SpyBean
    private ClockService clockService;

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
    public void Given_authenticatedUser_When_getUserBalance_Then_returnDAOFindResult() {
        Optional<Double> balance = Optional.of(50.00);

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

    @Test
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    @DisplayName("getUserBills() results")
    public void Given_authenticatedUser_When_getUserBills_Then_returnDAOResult() {
        ZonedDateTime creationDate = ZonedDateTime.of(2020, 2, 1, 1, 0, 0, 0, clockService.getZone());
        ZonedDateTime startDate = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, clockService.getZone());
        ZonedDateTime endDate = ZonedDateTime.of(2020, 2, 1, 0, 0, 0, 0, clockService.getZone());
        Collection<BillDTO> bills = Collections.singletonList(new BillDTO(2, creationDate, startDate, endDate));

        when(mockUserDAO.getBills(anyString())).thenReturn(bills);
        assertThat(userService.getUserBills())
                .isEqualTo(bills);

        bills = Collections.emptyList();
        when(mockUserDAO.getBills(anyString())).thenReturn(bills);
        assertThat(userService.getUserBills())
                .isEqualTo(bills);

        when(mockUserDAO.getBills(anyString())).thenReturn(null);
        assertThat(userService.getUserBills())
                .isEqualTo(null);
    }

    @Test
    @DisplayName("createBill() success")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_validRequest_When_createBill_Then_returnDAOValue() throws Exception {
        ZonedDateTime creationDate = ZonedDateTime.of(2020, 2, 1, 1, 0, 0, 0, clockService.getZone());
        when(clockService.now()).thenReturn(creationDate);
        ZonedDateTime startDate = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, clockService.getZone());
        ZonedDateTime endDate = ZonedDateTime.of(2020, 2, 1, 0, 0, 0, 0, clockService.getZone());
        BillDTO billSaved = new BillDTO(3, 2, creationDate, startDate, endDate, BigDecimal.valueOf(0.50));

        when(mockUserDAO.saveBill(any(BillDTO.class))).thenReturn(billSaved);
        when(mockUserDAO.find(anyString())).thenReturn(
                Optional.of(new User(3,
                        "username3",
                        "user3@mail.com",
                        "user3pass",
                        Collections.singletonList(Role.USER))));

        assertThat(userService.createBill(startDate, endDate))
                .isEqualToComparingFieldByField(billSaved);
    }

    @Test
    @DisplayName("createBill() failures")
    @WithMockUser(username = "user@mail.com", password = ENCODED_USERPASS_1)
    public void Given_wrongParams_When_createBill_Then_throwIllegalArgumentException() throws Exception {

        // # Case 1 : Start date is greater than EndDate
        ZonedDateTime startDate = ZonedDateTime.of(2020, 2, 1, 0, 0, 0, 0, clockService.getZone());
        ZonedDateTime endDate = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, clockService.getZone());
        ZonedDateTime creationDate = ZonedDateTime.of(2020, 2, 1, 1, 0, 0, 0, clockService.getZone());
        when(clockService.now()).thenReturn(creationDate);

        when(mockUserDAO.find(anyString())).thenReturn(
                Optional.of(new User(3,
                        "username3",
                        "user3@mail.com",
                        "user3pass",
                        Collections.singletonList(Role.USER))));
        assertThrows(IllegalArgumentException.class, () -> userService.createBill(startDate, endDate));

        // # Case 2 : End date is greater than CreationDate
        ZonedDateTime startDate2 = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, clockService.getZone());
        ZonedDateTime endDate2 = ZonedDateTime.of(2020, 2, 1, 0, 0, 0, 0, clockService.getZone());
        ZonedDateTime creationDate2 = ZonedDateTime.of(2020, 1, 25, 1, 0, 0, 0, clockService.getZone());
        when(clockService.now()).thenReturn(creationDate2);

        when(mockUserDAO.find(anyString())).thenReturn(
                Optional.of(new User(3,
                        "username3",
                        "user3@mail.com",
                        "user3pass",
                        Collections.singletonList(Role.USER))));

        assertThrows(IllegalArgumentException.class, () -> userService.createBill(startDate2, endDate2));

        // # Case 3 : For unknown reason, Authenticated User is not found
        ZonedDateTime startDate3 = ZonedDateTime.of(2020, 1, 1, 0, 0, 0, 0, clockService.getZone());
        ZonedDateTime endDate3 = ZonedDateTime.of(2020, 2, 1, 0, 0, 0, 0, clockService.getZone());
        ZonedDateTime creationDate3 = ZonedDateTime.of(2020, 2, 25, 1, 0, 0, 0, clockService.getZone());
        when(clockService.now()).thenReturn(creationDate3);

        when(mockUserDAO.find(anyString())).thenReturn(
                Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.createBill(startDate3, endDate3));

    }
}
