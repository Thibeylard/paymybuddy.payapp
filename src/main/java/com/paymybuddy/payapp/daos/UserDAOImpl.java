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
                    user = Optional.of(new User(rs.getInt(1))
                            .withUsername(rs.getString(2))
                            .withMail(rs.getString(3))
                            .withPassword(rs.getString(4)));
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
    public boolean saveUser(@NotNull final String username, @NotNull final String mail, @NotNull final String encodedPassword) throws IllegalArgumentException {

        findByMail(mail).ifPresent(user -> {
            throw new IllegalArgumentException("A User with this mail already exists.");
        });

        Connection con = databaseConfiguration.getConnection();
        PreparedStatement ps = null;
        boolean result = false;

        if (con != null) {
            try {
                ps = con.prepareStatement(DBStatements.INSERT_USER);
                ps.setString(0, username);
                ps.setString(1, mail);
                ps.setString(2, encodedPassword);
                result = ps.execute();
            } catch (SQLException e) {
                Logger.error("An error occurred : Registration could not be validated.");
            } finally {
                databaseConfiguration.closePreparedStatement(ps);
                databaseConfiguration.closeConnection(con);
            }
        }

        return result;
    }
}
