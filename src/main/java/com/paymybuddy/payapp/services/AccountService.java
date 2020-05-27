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
    void registrateUser(@NotEmpty @Size(min = 5, max = 25) String username,
                        @Email String mail,
                        @Size(min = 8, max = 80) String password) throws SQLException, IllegalArgumentException, ConstraintViolationException;
}
