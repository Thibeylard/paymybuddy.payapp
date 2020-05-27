package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.User;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Optional;

@Repository
public interface UserDAO {

    /**
     * Retrieved User base on ID
     *
     * @param userId User database id
     * @return Optional empty or populated with retrieved User
     */
    Optional<User> findById(final int userId);

    /**
     * Retrieved User base on mail
     *
     * @param mail User mail
     * @return Optional empty or populated with retrieved User
     */
    Optional<User> findByMail(final String mail);

    /**
     * Save new User in database
     *
     * @param username User.username
     * @param mail     User.mail UNIQUE
     * @param password User.password (encrypted)
     * @throws SQLException             if SQL operation fails
     * @throws IllegalArgumentException if mail is not available
     */
    boolean save(final String username,
                 final String mail,
                 final String password) throws IllegalArgumentException, SQLException;

    /**
     * Update User where mail = principalMail with values usernameToSet, mailToSet, passwordToSet
     *
     * @param principalMail current User.mail
     * @param usernameToSet User.username
     * @param mailToSet     new User.mail
     * @param passwordToSet User.password
     * @throws SQLException             if SQL operation fails
     * @throws IllegalArgumentException if mail is unavailable
     */
    boolean updateSettings(final String principalMail,
                           final String usernameToSet,
                           final String mailToSet,
                           final String passwordToSet) throws IllegalArgumentException, SQLException;
}
