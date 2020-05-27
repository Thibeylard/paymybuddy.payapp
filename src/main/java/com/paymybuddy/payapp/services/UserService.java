package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.User;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.SQLException;
import java.util.Optional;

@Service
public interface UserService {
    /**
     * Retrieved User base on ID
     *
     * @param userId User database id
     * @return Optional empty or populated with retrieved User
     */
    Optional<User> getUserById(final int userId);

    /**
     * Retrieved User base on mail
     *
     * @param mail User mail
     * @return Optional empty or populated with retrieved User
     */
    Optional<User> getUserByMail(final String mail);

    // TODO les Service doivent-ils vraiment lancer une exception SQL ? Parait peu respectueux du principe SOLID de l'interface

    /**
     * Update either User username, mail and/or password, secured by additional password check.
     *
     * @param password      current User password
     * @param usernameToSet new or current username
     * @param mailToSet     new or current mail
     * @param passwordToSet new password or null
     * @throws SQLException                 if SQL operation fails
     * @throws IllegalArgumentException     if mail is unavailable
     * @throws BadCredentialsException      if password check fails
     * @throws ConstraintViolationException if params do not match criteria
     */
    void updateSettings(final String password,
                        final @NotEmpty @Size(min = 5, max = 25) String usernameToSet,
                        final @Email String mailToSet,
                        @Nullable @Size(min = 8, max = 80) String passwordToSet)
            throws SQLException, IllegalArgumentException, BadCredentialsException, ConstraintViolationException;
}
