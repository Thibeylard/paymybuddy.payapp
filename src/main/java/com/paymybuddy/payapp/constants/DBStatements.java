package com.paymybuddy.payapp.constants;

public final class DBStatements {

    // UserDAO Classic JDBC Statements -------------------------------------------------

    // USER Table statements
    public static final String GET_USER_BY_ID_CLASSIC_JDBC =
            "SELECT id, username, mail, password FROM User WHERE id = ?";

    public static final String GET_USER_BY_MAIL_CLASSIC_JDBC =
            "SELECT id, username, mail, password FROM User WHERE mail = ?";

    public static final String GET_USER_ID_BY_MAIL_CLASSIC_JDBC =
            "SELECT id FROM User WHERE mail = ?";

    public static final String INSERT_USER_CLASSIC_JDBC =
            "INSERT INTO User (username, mail, password) VALUES ( ?, ?, ?)";

    public static final String UPDATE_USER_CLASSIC_JDBC =
            "UPDATE User SET (username, mail, password) = ( ?, ?, ?) WHERE User.mail = ?";

    // USER_ROLE Table statements
    public static final String GET_USER_ROLES_CLASSIC_JDBC =
            "SELECT role_id FROM User_Role WHERE user_id = ?";

    public static final String INSERT_USER_ROLE_CLASSIC_JDBC =
            "INSERT INTO User_Role (user_id, role_id) VALUES ( ?, ?)";

    //  Spring NamedParameterJDBCTemplate Statements -------------------------------------------------

    // CONTACT Table statements
    public static final String GET_USER_BY_MAIL =
            "SELECT id, username, mail, password FROM User WHERE mail = :userMail";

    public static final String GET_CONTACTS_ID =
            "SELECT user_b_id AS contact_id FROM Contact WHERE user_a_id = :userID UNION " +
                    "SELECT user_a_id AS contact_id FROM Contact WHERE user_b_id = :userID";

    public static final String GET_CONTACTS =
            "SELECT id, username, mail FROM User WHERE id IN (" + GET_CONTACTS_ID + ")";

    public static final String INSERT_CONTACT =
            "INSERT INTO Contact (user_a_id, user_b_id) VALUES ( :userID, :contactID)";

    public static final String DELETE_CONTACT =
            "DELETE FROM Contact WHERE (user_a_id = :userID AND user_b_id = :contactID) " +
                    "OR (user_a_id = :contactID AND user_b_id = :userID)";

}
