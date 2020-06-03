package com.paymybuddy.payapp.constants;

public final class DBStatements {

    // USER Table statements
    public static final String GET_USER_BY_ID =
            "SELECT id, username, mail, password FROM User WHERE id = ?";

    public static final String GET_USER_BY_MAIL =
            "SELECT id, username, mail, password FROM User WHERE mail = ?";

    public static final String GET_USER_ID_BY_MAIL =
            "SELECT id FROM User WHERE mail = ?";

    public static final String INSERT_USER =
            "INSERT INTO User (username, mail, password) VALUES ( ?, ?, ?)";

    public static final String UPDATE_USER =
            "UPDATE User SET (username, mail, password) = ( ?, ?, ?) WHERE User.mail = ?";


    // USER_ROLE Table statements
    public static final String GET_USER_ROLES =
            "SELECT role_id FROM User_Role WHERE user_id = ?";

    public static final String INSERT_USER_ROLE =
            "INSERT INTO User_Role (user_id, role_id) VALUES ( ?, ?)";

    // CONTACT Table statements
    public static final String GET_CONTACTS_ID =
            "SELECT user_b_id AS contact_id FROM Contact WHERE user_a_id = ? UNION " +
                    "SELECT user_a_id AS contact_id FROM Contact WHERE user_b_id = ?";

    public static final String GET_CONTACTS =
            "SELECT id, username, mail FROM User WHERE id IN (" + GET_CONTACTS_ID + ")";

    public static final String INSERT_CONTACT =
            "INSERT INTO Contact (user_a_id, user_b_id) VALUES ( ?, ?)";

    public static final String DELETE_CONTACT =
            "DELETE FROM Contact WHERE user_a_id = ? AND user_b_id = ?";

}
