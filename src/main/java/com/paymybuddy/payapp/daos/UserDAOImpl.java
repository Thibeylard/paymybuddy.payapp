package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.config.DatabaseConfiguration;
import com.paymybuddy.payapp.constants.DBStatements;
import com.paymybuddy.payapp.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.tinylog.Logger;

import javax.validation.constraints.NotNull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
                    // TODO ajouter les roles (vérifier la requete SQL nécessaire)
                }
            } catch (SQLException e) {
                Logger.error("An error occurred : User could not be found.");
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
    public boolean saveUser(@NotNull final String username, @NotNull final String mail, @NotNull final String encodedPassword) throws SQLException, IllegalArgumentException {

        Logger.debug("Asked to save user {}, {}, {}", username, mail, encodedPassword);
        if (findByMail(mail).isPresent()) {
            Logger.debug("User mail unavailable.");
            throw new IllegalArgumentException("A User with this mail already exists.");
        }

        Connection con = databaseConfiguration.getConnection();
        PreparedStatement ps = null;
        boolean result = false;

        if (con != null) {
            try {
                ps = con.prepareStatement(DBStatements.INSERT_USER);
                ps.setString(1, username);
                ps.setString(2, mail);
                ps.setString(3, encodedPassword);
                ps.execute();
                result = true;
            } catch (SQLException e) {
                Logger.error("Database error occurred.");
                e.printStackTrace();
                throw new SQLException("An error occurred : Registration could not be validated.");
            } finally {
                databaseConfiguration.closePreparedStatement(ps);
                databaseConfiguration.closeConnection(con);
            }
        }

        return result;
    }
}
