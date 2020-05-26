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
    Optional<User> getUserById(final int userId);

    Optional<User> getUserByMail(final String mail);

    // TODO les Service doivent-ils vraiment lancer une exception SQL ? Parait peu respectueux du principe SOLID de l'interface
    void updateSettings(final String password,
                        final @NotEmpty @Size(min = 5, max = 25) String usernameToSet,
                        final @Email String mailToSet,
                        @Nullable @Size(min = 8, max = 80) String passwordToSet)
            throws SQLException, IllegalArgumentException, BadCredentialsException, ConstraintViolationException;
}
