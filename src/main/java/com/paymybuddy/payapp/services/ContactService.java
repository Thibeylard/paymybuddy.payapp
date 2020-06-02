package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.Contact;

import java.sql.SQLException;
import java.util.Collection;

public interface ContactService {

    /**
     * Get all contacts of User identified by userId
     *
     * @param userId Id of User
     * @return All User contacts as User Collection
     * @throws SQLException if error occurs during database operation
     */
    Collection<Contact> getContactsByUserId(int userId)
            throws SQLException;

    /**
     * Add to User identified by userId the User identified by contactID as Contact.
     *
     * @param userId      Reference User ID
     * @param contactMail Contact mail
     * @throws SQLException if error occurs during database operation
     */
    void addContact(int userId, String contactMail)
            throws SQLException;

    /**
     * Delete from User identified by userId contacts the User identified by contactID.
     *
     * @param userId      Reference User ID
     * @param contactMail Contact mail
     * @throws SQLException if error occurs during database operation
     */
    void deleteContact(int userId, String contactMail)
            throws SQLException;
}
