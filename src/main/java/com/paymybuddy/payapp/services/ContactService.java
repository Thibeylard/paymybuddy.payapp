package com.paymybuddy.payapp.services;

import com.paymybuddy.payapp.models.User;

import java.util.Collection;

public interface ContactService {

    /**
     * Get all contacts of User identified by userId
     *
     * @param userId Id of User
     * @return All User contacts as User Collection
     */
    Collection<User> getContactsByUserId(int userId);

    /**
     * Add to User identified by userId the User identified by contactID as Contact.
     *
     * @param userId      Reference User ID
     * @param contactMail Contact mail
     */
    void addContact(int userId, String contactMail);

    /**
     * Delete from User identified by userId contacts the User identified by contactID.
     *
     * @param userId      Reference User ID
     * @param contactMail Contact mail
     */
    void deleteContact(int userId, String contactMail);
}
