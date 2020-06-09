package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.constants.DBStatements;
import com.paymybuddy.payapp.models.Contact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Repository
public class ContactDAOSpringJdbc implements ContactDAO {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public ContactDAOSpringJdbc(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * @see ContactDAO
     */
    @Override
    public Collection<Contact> getContactsByUserMail(final String userMail) throws DataAccessException {
        Integer userID = jdbcTemplate.queryForObject(DBStatements.GET_USER_BY_MAIL,
                new MapSqlParameterSource("userMail", userMail),
                (rs, rowNum) ->
                        rs.getInt("id"));

        assert userID != null;

        return jdbcTemplate.query(DBStatements.GET_CONTACTS,
                new MapSqlParameterSource("userID", userID),
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
    @Override
    public boolean save(final String userMail,
                        final String contactMail) throws DataAccessException {
        return jdbcTemplate.update(DBStatements.INSERT_CONTACT, getContactsIDs(userMail, contactMail)) == 1; // True if one row affected
    }

    /**
     * @see ContactDAO
     */
    @Override
    public boolean delete(final String userMail,
                          final String contactMail) throws DataAccessException {
        if (jdbcTemplate.update(DBStatements.DELETE_CONTACT, getContactsIDs(userMail, contactMail)) == 0) { // If still no row affected, throw Exception
            throw new InvalidDataAccessApiUsageException("Contact cannot be deleted because it does not exists.");
        }
        return true;
    }

    /**
     * Return as a map IDs of users to create contact between.
     *
     * @param userMail    first User
     * @param contactMail second User as Contact
     * @return Map of retrieved userID and contactID
     */
    private Map<String, Integer> getContactsIDs(final String userMail,
                                                final String contactMail) {
        Map<String, Integer> contactsIDs = new HashMap<>();
        Integer userID = jdbcTemplate.queryForObject(DBStatements.GET_USER_BY_MAIL,
                new MapSqlParameterSource("userMail", userMail),
                (rs, rowNum) ->
                        rs.getInt("id"));

        assert userID != null;
        contactsIDs.put("userID", userID);

        Integer contactID = jdbcTemplate.queryForObject(DBStatements.GET_USER_BY_MAIL,
                new MapSqlParameterSource("userMail", contactMail),
                (rs, rowNum) ->
                        rs.getInt("id"));

        assert contactID != null;
        contactsIDs.put("contactID", contactID);

        return contactsIDs;
    }
}
