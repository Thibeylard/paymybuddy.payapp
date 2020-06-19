package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.User;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

public interface UserService {

    /**
     * Retrieves authenticated User based on mail
     *
     * @return Optional empty or populated with retrieved User
     */
    Optional<User> getUserByMail();

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
    void updateUserProfile(final String password,
                           final @NotEmpty @Size(min = 5, max = 25) String usernameToSet,
                           final @Email String mailToSet,
                           @Nullable @Size(min = 8, max = 80) String passwordToSet)
            throws SQLException, IllegalArgumentException, BadCredentialsException, ConstraintViolationException;

    /**
     * Retrieves authenticated User balance calculated from its transactions.
     *
     * @return User balance as Optional<Double>, empty if error occurs
     */
    Optional<BigDecimal> getUserBalance();
}
