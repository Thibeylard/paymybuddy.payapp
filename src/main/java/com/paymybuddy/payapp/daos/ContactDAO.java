package com.paymybuddy.payapp.daos;

import com.paymybuddy.payapp.models.User;

import java.util.Collection;

public interface ContactDAO {

    /**
     * Get all contacts of User identified by userId from Contact table
     *
     * @param userId Id of User
     * @return All User contacts as User Collection
     */
    Collection<User> getContactsByUserId(int userId);

    /**
     * Save connection between two users in Contact table.
     *
     * @param userId      First user ID
     * @param contactMail Contact mail
     * @return true if operation succeed
     */
    boolean save(int userId, String contactMail);

    /**
     * Delete connection between two users in Contact table.
     *
     * @param userId      First user ID
     * @param contactMail Contact mail
     * @return true if operation succeed
     */
    boolean delete(int userId, String contactMail);
}
