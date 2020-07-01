package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.constants.DBStatements;
import com.paymybuddy.payapp.dtos.ContactUserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataRetrievalFailureException;
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
    public Collection<ContactUserDTO> getContactsByUserMail(final String userMail) throws DataAccessException {
        return jdbcTemplate.query(DBStatements.GET_CONTACTS,
                new MapSqlParameterSource("userMail", userMail),
                (rs, rowNum) ->
                        new ContactUserDTO(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("mail")
                        ));
    }

    /**
     * @see ContactDAO
     */
    // TODO Empêcher la création d'un contact a b qui existe déjà en tant que b a
    @Override
    public boolean save(final String userMail,
                        final String contactMail) throws DataAccessException {
        Map<String, String> mails = new HashMap<>();
        mails.put("userMail", userMail);
        mails.put("contactMail", contactMail);
        if (jdbcTemplate.update(DBStatements.INSERT_CONTACT, mails) == 0) { // If no row affected, throw Exception
            throw new DataRetrievalFailureException("Contact cannot be added because some user does not exists.");
        }
        return true;
    }

    /**
     * @see ContactDAO
     */
    @Override
    public boolean delete(final String userMail,
                          final String contactMail) throws DataAccessException {
        Map<String, String> mails = new HashMap<>();
        mails.put("userMail", userMail);
        mails.put("contactMail", contactMail);
        if (jdbcTemplate.update(DBStatements.DELETE_CONTACT, mails) == 0) { // If no row affected, throw Exception
            throw new DataRetrievalFailureException("Contact cannot be deleted because it does not exists.");
        }
        return true;
    }
}
