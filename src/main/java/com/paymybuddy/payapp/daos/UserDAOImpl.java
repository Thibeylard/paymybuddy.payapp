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
    public Optional<User> findById(final int id) {
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
                    Logger.debug("User with {} as mail has been found", validMail);
                    user = Optional.of(new User(rs.getInt("id"))
                            .withUsername(rs.getString("username"))
                            .withMail(rs.getString("mail"))
                            .withPassword(rs.getString("password")));

                    ps = con.prepareStatement(DBStatements.GET_USER_ROLES);
                    ps.setLong(1, user.get().getId());
                    rs = ps.executeQuery();

                    while (rs.next()) {
                        roles.add(Role.getRoleFromDatabaseId(rs.getInt("role_id")));
                    }

                    user = Optional.of(user.get().withRoles(roles));
                }

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
    public Optional<User> findByUsername(final String username) {
        return null;
    }

    @Override
    public boolean save(final String username,
                        final String mail,
                        final String encodedPassword) throws SQLException, IllegalArgumentException {

        //TODO Remplacer cette gestion de l'erreur par l'analyse du code d'erreur de l'exception SQL
        if (findByMail(mail).isPresent()) {
            Logger.debug("User mail unavailable.");
            throw new IllegalArgumentException("A User with this mail already exists.");
        }

        Connection con = databaseConfiguration.getConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean result = false;

        if (con != null) {
            try {
                long userId = 0;
                // Start transaction
                con.setAutoCommit(false);

                // New User is inserted in database
                ps = con.prepareStatement(DBStatements.INSERT_USER);
                ps.setString(1, username);
                ps.setString(2, mail);
                ps.setString(3, encodedPassword);
                ps.execute();

                // New User id is retrieved
                ps = con.prepareStatement(DBStatements.GET_USER_ID_BY_MAIL);
                ps.setString(1, mail);
                rs = ps.executeQuery();

                if (rs.next()) {
                    userId = rs.getLong("id");
                }

                // User id is inserted as user_id with a USER role_id by default
                ps = con.prepareStatement(DBStatements.INSERT_USER_ROLE);
                ps.setLong(1, userId);
                ps.setInt(2, Role.USER.getDatabaseId());
                ps.execute();

                // Transaction is over
                con.commit();
                con.setAutoCommit(true); // autocommit set back to true

                result = true;
            } catch (SQLException e) {
                Logger.error("Database error occurred.");
                throw new SQLException("An error occurred : Registration could not be validated.");
            } finally {
                databaseConfiguration.closeResultSet(rs);
                databaseConfiguration.closePreparedStatement(ps);
                databaseConfiguration.closeConnection(con);
            }
        }

        return result;
    }

    @Override
    public boolean updateSettings(final int id,
                                  final String mail,
                                  final String username,
                                  final String newPassword) throws SQLException {
        return false;
    }
}
