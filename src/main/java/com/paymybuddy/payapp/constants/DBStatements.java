package com.paymybuddy.payapp.constants;

public final class DBStatements {

    // UserDAO Classic JDBC Statements -------------------------------------------------

    // USER Table statements
    public static final String GET_USER_BY_MAIL_CLASSIC_JDBC =
            "SELECT id, username, mail, password FROM User WHERE mail = ?";

    public static final String GET_USER_ID_BY_MAIL_CLASSIC_JDBC =
            "SELECT id FROM User WHERE mail = ?";

    public static final String INSERT_USER_CLASSIC_JDBC =
            "INSERT INTO User (username, mail, password) VALUES ( ?, ?, ?)";

    public static final String UPDATE_USER_CLASSIC_JDBC =
            "UPDATE User SET (username, mail, password) = ( ?, ?, ?) WHERE User.mail = ?";

    public static final String GET_USER_CREDIT_BALANCE_CLASSIC_JDBC =
            "SELECT u.id, SUM(c.total) AS balance FROM USER AS u " +
                    "INNER JOIN TRANSACTION AS c ON c.creditor_id = u.id " +
                    "WHERE u.mail = ?";

    public static final String GET_USER_DEBIT_BALANCE_CLASSIC_JDBC =
            "SELECT u.id, SUM(d.amount) AS balance FROM USER AS u " +
                    "INNER JOIN TRANSACTION AS d ON d.debtor_id = u.id " +
                    "WHERE u.mail = ?";

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

    public static final String INSERT_TRANSACTION =
            "INSERT INTO TRANSACTION (debtor_id, creditor_id, amount, description, zoned_date_time, total) " +
                    "SELECT u.id, r.id, :amount, :description, :zoned_date_time, :total FROM USER AS u " +
                    "INNER JOIN USER AS r ON r.mail <> u.mail " +
                    "WHERE u.mail = :userMail AND r.mail = :recipientMail";

    // BANK_ACCOUNT Table statements
    public static final String GET_BANK_ACCOUNTS =
            "SELECT ba.id, user_id, owner_fullname, description, IBAN FROM BANK_ACCOUNT AS ba " +
                    "INNER JOIN USER AS u ON user_id = u.id " +
                    "WHERE u.mail = :userMail";

    public static final String GET_BANK_ACCOUNT =
            "SELECT id, user_id, owner_fullname, description, IBAN FROM BANK_ACCOUNT " +
                    "WHERE id = :bankAccountID";

    public static final String INSERT_BANK_ACCOUNT =
            "INSERT INTO BANK_ACCOUNT (user_id, owner_fullname, description, IBAN) " +
                    "SELECT u.id, :ownerFullName, :description, :IBAN, FROM USER AS u " +
                    "WHERE u.mail = :userMail";

    public static final String UPDATE_BANK_ACCOUNT =
            "UPDATE BANK_ACCOUNT SET (owner_fullname, description, IBAN) = ( :ownerFullName, :description, :IBAN) WHERE id = :bankAccountID";

    public static final String DELETE_BANK_ACCOUNT =
            "DELETE FROM BANK_OPERATION WHERE bank_account_id = :bankAccountID; " +
                    "DELETE FROM BANK_ACCOUNT WHERE id = :bankAccountID";

    // BANK_OPERATION Table statements
    public static final String GET_BANK_OPERATIONS =
            "SELECT id, bank_account_id, date, amount FROM BANK_OPERATION " +
                    "WHERE bank_account_id = :bankAccountID";

    public static final String INSERT_BANK_OPERATION =
            "INSERT INTO BANK_OPERATION (bank_account_id, date, amount) VALUES(:bankAccountID, :date, :amount)";

}
