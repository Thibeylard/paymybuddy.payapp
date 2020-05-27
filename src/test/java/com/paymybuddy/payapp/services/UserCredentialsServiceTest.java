package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.User;
import com.paymybuddy.payapp.models.UserCredentials;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("User Credentials Service tests on : ")
public class UserCredentialsServiceTest {

    @Autowired
    private UserCredentialsService userCredentialsService;

    @MockBean
    private UserDAO userDAO;

    @Test
    @DisplayName("loadUserByUsername() success")
    public void Given_validMail_When_loadUserByUsername_Then_returnUser() {
        User foundUser = new User(1)
                .withUsername("username")
                .withMail("user@mail.com")
                .withPassword("userpass")
                .withRoles(Collections.singletonList(Role.USER));
        UserCredentials principal = new UserCredentials(foundUser.getMail(), foundUser.getPassword(), foundUser.getRoles());
        Optional<User> daoResponse = Optional.of(foundUser);

        when(userDAO.findByMail(anyString())).thenReturn(daoResponse);
        assertThat(userCredentialsService.loadUserByUsername("user@mail.com"))
                .isEqualToComparingFieldByField(principal);
    }

    @Test
    @DisplayName("loadUserByUsername() failure")
    public void Given_invalidMail_When_loadUserByUsername_Then_throwsUsernameNotFoundException() {
        when(userDAO.findByMail(anyString())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userCredentialsService.loadUserByUsername("user@mail.com"));
    }

}
