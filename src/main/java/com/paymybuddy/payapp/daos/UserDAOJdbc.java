package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.config.DatabaseConfiguration;
import com.paymybuddy.payapp.constants.DBStatements;
import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.tinylog.Logger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Repository
public class UserDAOJdbc implements UserDAO {

    private final DatabaseConfiguration databaseConfiguration;

    @Autowired
    public UserDAOJdbc(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }

    @Override
    public Optional<User> find(final String validMail) {
        Optional<User> user = Optional.empty();
        Connection con = databaseConfiguration.getConnection();
        if (con != null) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = con.prepareStatement(DBStatements.GET_USER_BY_MAIL_CLASSIC_JDBC);
                ps.setString(1, validMail);
                rs = ps.executeQuery();
                if (rs.next()) {
                    user = Optional.of(new User(rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("mail"),
                            rs.getString("password"),
                            getUserRolesByID(con, rs.getInt("id"))));
                }

                // else e.g (rs.next() == false), user remains an Optional.empty()

            } catch (SQLException e) {
                Logger.error("An error occurred : User could not be found.");
                user = Optional.empty();
            } finally {
                databaseConfiguration.closeResultSet(rs);
                databaseConfiguration.closePreparedStatement(ps);
                databaseConfiguration.closeConnection(con);
            }
        }

        return user;
    }

    @Override
    public boolean save(final User user) throws SQLException, IllegalArgumentException {

        Connection con = databaseConfiguration.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;

        if (con != null) {
            try {
                int userId;
                // Start transaction
                con.setAutoCommit(false);
                Logger.debug("Start transaction.");

                // New User is inserted in database
                ps = con.prepareStatement(DBStatements.INSERT_USER_CLASSIC_JDBC);
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getMail());
                ps.setString(3, user.getPassword());
                ps.execute();
                Logger.debug("New User inserted in User table.");

                // New User id is retrieved
                ps = con.prepareStatement(DBStatements.GET_USER_ID_BY_MAIL_CLASSIC_JDBC);
                ps.setString(1, user.getMail());
                rs = ps.executeQuery();

                if (rs.next()) {
                    userId = rs.getInt("id");
                    Logger.debug("Retrieved new User ID : {}", userId);
                } else {
                    throw new SQLException();
                }

                for (Role role : user.getRoles()) {
                    // User id is inserted as user_id with a USER role_id by default
                    ps = con.prepareStatement(DBStatements.INSERT_USER_ROLE_CLASSIC_JDBC);
                    ps.setInt(1, userId);
                    ps.setInt(2, role.getDatabaseId());
                    ps.execute();
                    Logger.debug("User role inserted in User_Role table.");
                }

                // Transaction is over
                con.commit();
                Logger.debug("Commit SQL transaction.");
                con.setAutoCommit(true); // autocommit set back to true

                result = true;
            } catch (SQLException e) {
                Logger.error(e.getMessage());

                if (e.getErrorCode() == 23505) { // Check unique constraint violation
                    Logger.debug("User mail or username unavailable.");
                    throw new IllegalArgumentException("A User with this mail or username already exists.");
                }

                throw e;
            } finally {
                databaseConfiguration.closeResultSet(rs);
                databaseConfiguration.closePreparedStatement(ps);
                databaseConfiguration.closeConnection(con);
            }
        }

        return result;
    }

    @Override
    public boolean update(final String userMail,
                          final String usernameToSet,
                          final String mailToSet,
                          final String passwordToSet) throws SQLException, IllegalArgumentException {

        Connection con = databaseConfiguration.getConnection();
        PreparedStatement ps = null;
        boolean result = false;

        if (con != null) {
            try {
                // Start transaction
                con.setAutoCommit(false);
                Logger.debug("Start transaction.");

                // User is modified
                ps = con.prepareStatement(DBStatements.UPDATE_USER_CLASSIC_JDBC);
                ps.setString(1, usernameToSet);
                ps.setString(2, mailToSet);
                ps.setString(3, passwordToSet);
                ps.setString(4, userMail);
                ps.execute();

                Logger.debug("User values updated.");
                // Transaction is over
                con.commit();
                Logger.debug("Commit SQL transaction.");
                con.setAutoCommit(true); // autocommit set back to true

                result = true;
            } catch (SQLException e) {
                Logger.error(e.getMessage());

                if (e.getErrorCode() == 23505) { // Check unique constraint violation
                    Logger.debug("User mail unavailable.");
                    throw new IllegalArgumentException("A User with this mail already exists.");
                }

                throw e;
            } finally {
                databaseConfiguration.closePreparedStatement(ps);
                databaseConfiguration.closeConnection(con);
            }
        }

        return result;
    }


    /**
     * @see UserDAO
     */
    @Override
    public Optional<BigDecimal> getBalance(final String userMail) {
        BigDecimal balance = BigDecimal.ZERO;
        Connection con = databaseConfiguration.getConnection();
        if (con != null) {

            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                // Start transaction
                con.setAutoCommit(false);
                Logger.debug("Start transaction for user balance.");

                // Add User credit balance
                ps = con.prepareStatement(DBStatements.GET_USER_BALANCE_CLASSIC_JDBC);
                ps.setString(1, userMail);
                ps.setString(2, userMail);
                ps.setString(3, userMail);
                rs = ps.executeQuery();
                if (rs.next()) {
                    balance = BigDecimal.valueOf(rs.getDouble("balance"));
                }

                con.commit();
                Logger.debug("Commit SQL transaction.");
                con.setAutoCommit(true); // autocommit set back to true
                // else e.g (rs.next() == false), balance remains null

            } catch (SQLException e) {
                balance = null;
                Logger.debug(e.getMessage());
                Logger.error("An error occurred : Balance could not be calculated.");
            } finally {
                databaseConfiguration.closeResultSet(rs);
                databaseConfiguration.closePreparedStatement(ps);
                databaseConfiguration.closeConnection(con);
            }
        }

        return Optional.ofNullable(balance);
    }

    /**
     * Get roles of User that was just retrieved.
     *
     * @param con    Database connection
     * @param userId Id of retrieved User
     * @return Roles as Collection
     * @throws SQLException if database exception occurs
     */
    private Collection<Role> getUserRolesByID(Connection con, int userId) throws SQLException {
        ArrayList<Role> roles = new ArrayList<>();
        PreparedStatement ps = con.prepareStatement(DBStatements.GET_USER_ROLES_CLASSIC_JDBC);
        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            roles.add(Role.getRoleFromDatabaseId(rs.getInt("role_id")));
            Logger.debug("User roles have been retrieved");
        }

        return roles;
    }
}
