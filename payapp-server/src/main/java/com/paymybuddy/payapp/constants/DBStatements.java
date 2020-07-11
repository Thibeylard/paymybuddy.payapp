package com.paymybuddy.payapp.constants;

public final class DBStatements {

    // UserDAO Classic JDBC Statements -------------------------------------------------

    // User Table statements
    private static final String GET_USER_ID =
            "SELECT u.id FROM User AS u " +
                    "WHERE u.mail = :userMail";

    public static final String GET_USER_BY_MAIL_CLASSIC_JDBC =
            "SELECT id, username, mail, password FROM User WHERE mail = ?";

    public static final String GET_USER_ID_BY_MAIL_CLASSIC_JDBC =
            "SELECT id FROM User WHERE mail = ?";

    public static final String INSERT_USER_CLASSIC_JDBC =
            "INSERT INTO User (username, mail, password) VALUES ( ?, ?, ?)";

    public static final String UPDATE_USER_CLASSIC_JDBC =
            "UPDATE User SET username = ?, mail = ?, password = ? WHERE mail = ?";

    public static final String GET_USER_BALANCE_CLASSIC_JDBC =
            "SELECT userID, SUM(balance) AS balance FROM (" +
                    "SELECT u.id AS userID, SUM(c.amount) AS balance FROM User AS u " +
                    "INNER JOIN Transaction AS c ON c.creditor_id = u.id " +
                    "WHERE u.mail = ? " +
                    "UNION " +
                    "SELECT u.id AS userID, SUM(-d.amount) AS balance FROM User AS u " +
                    "INNER JOIN Transaction AS d ON d.debtor_id = u.id " +
                    "WHERE u.mail = ? " +
                    "UNION " +
                    "SELECT u.id AS userID, SUM(op.amount) AS balance FROM User AS u " +
                    "INNER JOIN Bank_Account AS acc ON acc.user_id = u.id " +
                    "INNER JOIN Bank_Operation AS op ON op.bank_account_id = acc.id " +
                    "WHERE u.mail = ? " +
                    ") AS T " +
                    "GROUP BY userID";

    public static final String GET_USER_BILLS_CLASSIC_JDBC =
            "SELECT  b.id, u.id AS user_id, creation_date, start_date, end_date, total FROM User AS u " +
                    "INNER JOIN Bill AS b ON b.user_id = u.id " +
                    "WHERE u.mail = ?";

    public static final String INSERT_BILL_CLASSIC_JDBC =
            "INSERT INTO Bill (user_id, creation_date, start_date, end_date, total) VALUES ( ?, ?, ?, ?, ?)";

    public static final String GET_BILL_TOTAL_CLASSIC_JDBC =
            "SELECT SUM(t.commission) AS commission FROM Transaction as t " +
                    "INNER JOIN User AS u ON u.id = t.debtor_id " +
                    "WHERE t.date >= ? AND t.date <= ? AND u.id = ?";

    // User_Role Table statements
    public static final String GET_USER_ROLES_CLASSIC_JDBC =
            "SELECT role_id FROM User_Role WHERE user_id = ?";

    public static final String INSERT_USER_ROLE_CLASSIC_JDBC =
            "INSERT INTO User_Role (user_id, role_id) VALUES ( ?, ?)";

    //  Spring NamedParameterJDBCTemplate Statements -------------------------------------------------

    // Contact Table statements
    private static final String GET_CONTACT_ID =
            "SELECT c.id FROM User AS c " +
                    "WHERE c.mail = :contactMail";

    public static final String GET_CONTACTS_ID =
            "SELECT user_b_id AS contact_id FROM Contact AS c " +        // Get all contacts where user is user_a_id
                    "INNER JOIN User AS u ON c.user_a_id = u.id " +
                    "WHERE u.mail = :userMail " +
                    "UNION " +
                    "SELECT user_a_id AS contact_id FROM Contact AS c " + // Get all contacts where user is user_b_id
                    "INNER JOIN User AS u ON c.user_b_id = u.id " +
                    "WHERE u.mail = :userMail ";

    public static final String GET_CONTACTS =
            "SELECT id, username, mail FROM User WHERE id IN (" + GET_CONTACTS_ID + ")";

    public static final String GET_CONTACT_COUPLE =
            "SELECT user_a_id, user_b_id FROM Contact as c " +
                    "WHERE (user_a_id = (" + GET_USER_ID + ") AND user_b_id = (" + GET_CONTACT_ID + ")) OR " +
                    "(user_b_id = (" + GET_USER_ID + ") AND user_a_id = (" + GET_CONTACT_ID + "))";


    public static final String INSERT_CONTACT =
            "INSERT INTO Contact (user_a_id, user_b_id) " +
                    "SELECT u.id, c.id FROM User AS u " +
                    "INNER JOIN User AS c ON c.mail <> u.mail " +
                    "WHERE u.mail = :userMail AND c.mail = :contactMail";

    public static final String DELETE_CONTACT =
            "DELETE FROM Contact WHERE (user_a_id IN (" + GET_USER_ID + ") AND user_b_id IN (" + GET_CONTACT_ID + ")) " +
                    "OR (user_a_id IN (" + GET_CONTACT_ID + ") AND user_b_id IN (" + GET_USER_ID + "))";

    // Transaction Table statements
    private static final String GET_TRANSACTIONS_MODEL =
            "SELECT t.id, debtor_id, creditor_id, amount, description, date, commission FROM Transaction AS t ";

    public static final String GET_DEBIT_TRANSACTIONS =
            GET_TRANSACTIONS_MODEL +
                    "INNER JOIN User AS u ON u.id = debtor_id " +
                    "WHERE u.mail = :userMail ";

    public static final String GET_CREDIT_TRANSACTIONS =
            GET_TRANSACTIONS_MODEL +
                    "INNER JOIN User AS u ON u.id = creditor_id " +
                    "WHERE u.mail = :userMail ";

    public static final String GET_ALL_TRANSACTIONS =
            GET_DEBIT_TRANSACTIONS + " UNION " + GET_CREDIT_TRANSACTIONS;

    public static final String INSERT_TRANSACTION =
            "INSERT INTO Transaction (debtor_id, creditor_id, amount, description, date, commission) " +
                    "SELECT u.id, r.id, :amount, :description, :date, :commission FROM User AS u " +
                    "INNER JOIN User AS r ON r.mail <> u.mail " +
                    "WHERE u.mail = :userMail AND r.mail = :recipientMail";

    // Bank_Account Table statements
    public static final String GET_BANK_ACCOUNTS =
            "SELECT ba.id, user_id, owner_fullname, description, IBAN FROM Bank_Account AS ba " +
                    "INNER JOIN User AS u ON user_id = u.id " +
                    "WHERE u.mail = :userMail";

    public static final String GET_BANK_ACCOUNT =
            "SELECT id, user_id, owner_fullname, description, IBAN FROM Bank_Account " +
                    "WHERE id = :bankAccountID";

    public static final String INSERT_BANK_ACCOUNT =
            "INSERT INTO Bank_Account (user_id, owner_fullname, description, IBAN) " +
                    "SELECT u.id, :ownerFullName, :description, :IBAN FROM User AS u " +
                    "WHERE u.mail = :userMail";

    public static final String UPDATE_BANK_ACCOUNT =
            "UPDATE Bank_Account SET owner_fullname = :ownerFullName, description = :description, IBAN = :IBAN WHERE id = :bankAccountID";

    public static final String DELETE_BANK_ACCOUNT_OPERATIONS =
            "DELETE FROM Bank_Operation WHERE bank_account_id = :bankAccountID";

    public static final String DELETE_BANK_ACCOUNT =
            "DELETE FROM Bank_Account WHERE id = :bankAccountID";

    // Bank_Operation Table statements
    public static final String GET_BANK_OPERATIONS =
            "SELECT id, bank_account_id, date, amount FROM Bank_Operation " +
                    "WHERE bank_account_id = :bankAccountID";

    public static final String INSERT_BANK_OPERATION =
            "INSERT INTO Bank_Operation (bank_account_id, date, amount) VALUES(:bankAccountID, :date, :amount)";


}
