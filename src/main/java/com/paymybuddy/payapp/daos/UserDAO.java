package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.dtos.BillDTO;
import com.paymybuddy.payapp.models.User;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

@Repository
public interface UserDAO {


    /**
     * Retrieved User base on mail
     *
     * @param mail User mail
     * @return Optional empty or populated with retrieved User
     */
    Optional<User> find(final String mail);

    /**
     * Save new User in database
     *
     * @param user User object
     * @throws SQLException             if SQL operation fails
     * @throws IllegalArgumentException if mail is not available
     */
    boolean save(final User user) throws IllegalArgumentException, SQLException;

    /**
     * Update User where mail = principalMail with values usernameToSet, mailToSet, passwordToSet
     *
     * @param userMail      current User.mail
     * @param usernameToSet User.username
     * @param mailToSet     new User.mail
     * @param passwordToSet User.password
     * @throws SQLException             if SQL operation fails
     * @throws IllegalArgumentException if mail is unavailable
     */
    boolean update(final String userMail,
                   final String usernameToSet,
                   final String mailToSet,
                   final String passwordToSet) throws IllegalArgumentException, SQLException;

    /**
     * Get User balance calculated from its transactions.
     *
     * @param userMail Mail of User to get balance of
     * @return User balance as Optional<Double>, empty if error occurs
     */
    Optional<BigDecimal> getBalance(final String userMail);

    /**
     * Get User bills from Bill table.
     *
     * @param userMail Mail of User to get bills of
     * @return Collection of BillDTO, empty if no Bills, null if no user.
     */
    Collection<BillDTO> getBills(final String userMail);

    /**
     * Create new bill for User.
     *
     * @param bill BillDTO object to save
     * @return BillDTO object just saved
     */
    BillDTO saveBill(final BillDTO bill) throws SQLException;
}
