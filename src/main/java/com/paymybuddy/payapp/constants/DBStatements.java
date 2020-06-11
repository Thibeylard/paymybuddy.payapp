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
    public static final String GET_CONTACTS_ID =
            "SELECT user_b_id AS contact_id FROM Contact " +        // Get all contacts where user is user_a_id
                    "INNER JOIN USER ON Contact.user_a_id = User.id " +
                    "WHERE user.mail = :userMail " +
                    "UNION " +
                    "SELECT user_a_id AS contact_id FROM Contact " + // Get all contacts where user is user_b_id
                    "INNER JOIN USER ON Contact.user_b_id = User.id " +
                    "WHERE user.mail = :userMail ";

    public static final String GET_CONTACTS =
            "SELECT id, username, mail FROM User WHERE id IN (" + GET_CONTACTS_ID + ")";

    public static final String INSERT_CONTACT =
            "INSERT INTO Contact (user_a_id, user_b_id) " +
                    "SELECT u.id, c.id FROM USER AS u " +
                    "INNER JOIN USER AS c ON c.mail <> u.mail " +
                    "WHERE u.mail = :userMail AND c.mail = :contactMail";

    private static final String GET_CONTACT_ID_FOR_DELETE =
            "SELECT c.id FROM USER AS c " +
                    "WHERE c.mail = :contactMail";

    private static final String GET_USER_ID_FOR_DELETE =
            "SELECT u.id FROM USER AS u " +
                    "WHERE u.mail = :userMail";

    public static final String DELETE_CONTACT =
            "DELETE FROM Contact WHERE (user_a_id IN (" + GET_USER_ID_FOR_DELETE + ") AND user_b_id IN (" + GET_CONTACT_ID_FOR_DELETE + ")) " +
                    "OR (user_a_id IN (" + GET_CONTACT_ID_FOR_DELETE + ") AND user_b_id IN (" + GET_USER_ID_FOR_DELETE + "))";

    public static final String INSERT_TRANSACTION =
            "INSERT INTO TRANSACTION (debtor_id, creditor_id, amount, description, zoned_date_time, total) " +
                    "SELECT u.id, r.id, :amount, :description, :date, :total FROM USER AS u " +
                    "INNER JOIN USER AS r ON r.mail <> u.mail " +
                    "WHERE u.mail = :userMail AND r.mail = :recipientMail";

    // TRANSACTION Table statements
    private static final String GET_TRANSACTIONS_MODEL =
            "SELECT t.id, debtor_id, creditor_id, amount, description, zoned_date_time, total FROM TRANSACTION AS t ";

    public static final String GET_DEBIT_TRANSACTIONS =
            GET_TRANSACTIONS_MODEL +
                    "INNER JOIN USER AS u ON u.id = debtor_id " +
                    "WHERE u.mail = :userMail ";

    public static final String GET_CREDIT_TRANSACTIONS =
            GET_TRANSACTIONS_MODEL +
                    "INNER JOIN USER AS u ON u.id = creditor_id " +
                    "WHERE u.mail = :userMail ";

    public static final String GET_ALL_TRANSACTIONS =
            GET_DEBIT_TRANSACTIONS + " UNION " + GET_CREDIT_TRANSACTIONS;

    // TODO Ajouter la requête du solde utilisateur.

}
