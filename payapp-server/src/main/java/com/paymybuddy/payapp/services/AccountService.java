package com.paymybuddy.payapp.services;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.sql.SQLException;

public interface AccountService {

    /**
     * Save params as a User in the database
     *
     * @param username User pseudonym
     * @param mail     User mail as identifier
     * @param password User password to encrypt
     * @throws SQLException                 if SQL operation fails
     * @throws IllegalArgumentException     if mail is not available
     * @throws ConstraintViolationException if params do not match criteria
     */
    void registrateUser(final @NotEmpty @Size(min = 5, max = 25, message = "Username must contain between 5 and 25 characters.") String username,
                        final @Email(message = "Email has invalid format.") String mail,
                        @Size(min = 8, max = 80, message = "Password must contain between 8 and 80 characters.") String password) throws SQLException, IllegalArgumentException, ConstraintViolationException;
}
