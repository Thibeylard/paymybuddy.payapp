package com.paymybuddy.payapp.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.tinylog.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class DatabaseConfiguration {

    DataSource dataSource;

    @Autowired
    public DatabaseConfiguration(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Get connection to referenced database.
     *
     * @return New connection
     */
    public Connection getConnection() {
        Connection con = null;
        try {
            con = dataSource.getConnection();
//            Logger.debug("Connected to database.");
        } catch (SQLException e) {
            Logger.error("Error while getting connection to database.");
        }
        return con;
    }

    /**
     * Close connection con.
     *
     * @param con Connection to close
     */
    public void closeConnection(@Nullable Connection con) {
        if (con == null)  // If error occurred during SQL operation
            return;

        try {
            con.close();
//            Logger.debug("Database connection closed.");
        } catch (SQLException e) {
            Logger.error("Error while closing connection to database.");
        }
    }

    /**
     * Close PreparedStatement ps.
     *
     * @param ps PreparedStatement to close
     */
    public void closePreparedStatement(@Nullable PreparedStatement ps) {
        if (ps == null)  // If error occurred during SQL operation
            return;

        try {
            ps.close();
//            Logger.debug("PreparedStatement closed.");
        } catch (SQLException e) {
            Logger.error("Error while closing PreparedStatement.");
        }
    }

    /**
     * Close ResultSet rs.
     *
     * @param rs ResultSet to close
     */
    public void closeResultSet(@Nullable ResultSet rs) {
        if (rs == null)  // If error occurred during SQL operation
            return;

        try {
            rs.close();
//            Logger.debug("ResultSet closed.");
        } catch (SQLException e) {
            Logger.error("Error while closing ResultSet.");
        }
    }
}
