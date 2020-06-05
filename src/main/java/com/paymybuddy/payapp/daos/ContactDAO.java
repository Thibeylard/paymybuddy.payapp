package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.Contact;
import org.springframework.dao.DataAccessException;

import java.util.Collection;

public interface ContactDAO {

    /**
     * Get all contacts of User identified by userMail from Contact table
     *
     * @param userMail User mail
     * @return All User contacts as User Collection
     */
    Collection<Contact> getContactsByUserMail(String userMail)
            throws DataAccessException;

    /**
     * Save contact between two users in Contact table.
     *
     * @param userMail    User mail
     * @param contactMail Contact mail
     * @return true if operation succeed
     */
    boolean save(String userMail, String contactMail)
            throws DataAccessException;

    /**
     * Delete contact between two users in Contact table.
     *
     * @param userMail    User mail
     * @param contactMail Contact mail
     * @return true if operation succeed
     */
    boolean delete(String userMail, String contactMail)
            throws DataAccessException;
}
