package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.config.DatabaseConfiguration;
import com.paymybuddy.payapp.constants.DBStatements;
import com.paymybuddy.payapp.enums.Role;
import com.paymybuddy.payapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.tinylog.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO {

    private final DatabaseConfiguration databaseConfiguration;

    @Autowired
    public UserDAOImpl(DatabaseConfiguration databaseConfiguration) {
        this.databaseConfiguration = databaseConfiguration;
    }

    @Override
    public Optional<User> findById(final int userId) {
        return null;
    }

    @Override
    public Optional<User> findByMail(final String validMail) {
        Optional<User> user = Optional.empty();
        ArrayList<Role> roles = new ArrayList<>();
        Connection con = databaseConfiguration.getConnection();
        if (con != null) {
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                ps = con.prepareStatement(DBStatements.GET_USER_BY_MAIL);
                ps.setString(1, validMail);
                rs = ps.executeQuery();
                if (rs.next()) {
                    user = Optional.of(new User(rs.getInt("id"))
                            .withUsername(rs.getString("username"))
                            .withMail(rs.getString("mail"))
                            .withPassword(rs.getString("password")));
                    Logger.debug("User {} has been retrieved", validMail);

                    ps = con.prepareStatement(DBStatements.GET_USER_ROLES);
                    ps.setLong(1, user.get().getId());
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        roles.add(Role.getRoleFromDatabaseId(rs.getInt("role_id")));
                        Logger.debug("User {} roles have been retrieved and assigned", user.get().getMail());
                    }

                    user = Optional.of(user.get().withRoles(roles));
                }

                // else e.g (rs.next() == false), user remains an Optional.empty()

            } catch (SQLException e) {
                Logger.error("An error occurred : User could not be found.");
                e.printStackTrace();
            } finally {
                databaseConfiguration.closeResultSet(rs);
                databaseConfiguration.closePreparedStatement(ps);
                databaseConfiguration.closeConnection(con);
            }
        }

        return user;
    }

    @Override
    public boolean save(final String username,
                        final String mail,
                        final String encodedPassword) throws SQLException, IllegalArgumentException {

        Connection con = databaseConfiguration.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;

        if (con != null) {
            try {
                int userId = 0;
                // Start transaction
                con.setAutoCommit(false);
                Logger.debug("Start transaction.");

                // New User is inserted in database
                ps = con.prepareStatement(DBStatements.INSERT_USER);
                ps.setString(1, username);
                ps.setString(2, mail);
                ps.setString(3, encodedPassword);
                ps.execute();
                Logger.debug("New User inserted in User table.");

                // New User id is retrieved
                ps = con.prepareStatement(DBStatements.GET_USER_ID_BY_MAIL);
                ps.setString(1, mail);
                rs = ps.executeQuery();

                if (rs.next()) {
                    userId = rs.getInt("id");
                    Logger.debug("Retrieved new User ID : {}", userId);
                } else {
                    throw new SQLException();
                }

                // User id is inserted as user_id with a USER role_id by default
                ps = con.prepareStatement(DBStatements.INSERT_USER_ROLE);
                ps.setInt(1, userId);
                ps.setInt(2, Role.USER.getDatabaseId());
                ps.execute();
                Logger.debug("User role inserted in User_Role table.");

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

                throw new SQLException("An error occurred : Could not save data.");
            } finally {
                databaseConfiguration.closeResultSet(rs);
                databaseConfiguration.closePreparedStatement(ps);
                databaseConfiguration.closeConnection(con);
            }
        }

        return result;
    }

    @Override
    public boolean updateSettings(final String principalMail,
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
                ps = con.prepareStatement(DBStatements.UPDATE_USER);
                ps.setString(1, usernameToSet);
                ps.setString(2, mailToSet);
                ps.setString(3, passwordToSet);
                ps.setString(4, principalMail);
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

                throw new SQLException("An error occurred : Could not update data.");
            } finally {
                databaseConfiguration.closePreparedStatement(ps);
                databaseConfiguration.closeConnection(con);
            }
        }

        return result;
    }
}
