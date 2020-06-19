package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.daos.UserDAO;
import com.paymybuddy.payapp.models.User;
import com.paymybuddy.payapp.models.UserCredentials;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.tinylog.Logger;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

@Service
@Validated
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, PasswordEncoder passwordEncoder) {
        this.userDAO = userDAO;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * @see UserService
     */
    @Override
    public Optional<User> getUserByMail() {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDAO.find(authUser.getUsername());
    }

    /**
     * @see UserService
     */
    @Override
    public void updateUserProfile(final String password,
                                  final @NotEmpty @Size(min = 5, max = 25) String usernameToSet,
                                  final @Email String mailToSet,
                                  @Nullable @Size(min = 8, max = 80) String passwordToSet)
            throws SQLException, IllegalArgumentException, ConstraintViolationException, BadCredentialsException {

        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (passwordEncoder.matches(password, authUser.getPassword())) {
            if (passwordToSet != null) {
                Logger.debug("Encoding new password.");
                passwordToSet = passwordEncoder.encode(passwordToSet);
            } else {
                passwordToSet = authUser.getPassword();
            }

            if (userDAO.update(authUser.getUsername(), usernameToSet, mailToSet, passwordToSet)) {
                Logger.debug("User update OK. Update authentication principal.");
                authUser = new UserCredentials(mailToSet, passwordToSet, authUser.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(authUser, passwordToSet));
            }
        } else {
            throw new BadCredentialsException("");
        }
    }

    /**
     * @see UserService
     */
    @Override
    public Optional<BigDecimal> getUserBalance() {
        UserDetails authUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userDAO.getBalance(authUser.getUsername());
    }
}
