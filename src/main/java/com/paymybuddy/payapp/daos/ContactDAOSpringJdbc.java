package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.constants.DBStatements;
import com.paymybuddy.payapp.models.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.Collection;

@Repository
public class ContactDAOSpringJdbc implements ContactDAO {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ContactDAOSpringJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @see ContactDAO
     */
    @Override
    public Collection<Contact> getContactsByUserId(int userId) throws DataAccessException {
        return jdbcTemplate.query(DBStatements.GET_CONTACTS,
                new Object[]{userId, userId},
                (rs, rowNum) ->
                        new Contact(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("mail")
                        ));
    }

    /**
     * @see ContactDAO
     */
    //TODO Régler le problème d'inversion des id
    @Override
    public boolean save(int userId, String contactMail) throws DataAccessException {
        Integer contact_id = jdbcTemplate.queryForObject(DBStatements.GET_USER_BY_MAIL,
                new Object[]{contactMail},
                (rs, rowNum) ->
                        rs.getInt("id"));
        return jdbcTemplate.update(DBStatements.INSERT_CONTACT, userId, contact_id) == 1; // True if one row affected
    }

    /**
     * @see ContactDAO
     */
    @Override
    public boolean delete(int userId, String contactMail) throws SQLException {
        Integer contact_id = jdbcTemplate.queryForObject(DBStatements.GET_USER_BY_MAIL,
                new Object[]{contactMail},
                (rs, rowNum) ->
                        rs.getInt("id"));
        return jdbcTemplate.update(DBStatements.DELETE_CONTACT, userId, contact_id) == 1; // True if one row affected
    }
}
