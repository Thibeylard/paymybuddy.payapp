package com.paymybuddy.payapp.constants;

public final class DBStatements {

    public static final String GET_USER_BY_MAIL =
            "SELECT id, username, mail, password FROM User WHERE mail = ?";

    public static final String GET_USER_ID_BY_MAIL =
            "SELECT id FROM User WHERE mail = ?";

    public static final String GET_USER_ROLES =
            "Select role_id FROM User_Role WHERE user_id = ?";

    public static final String INSERT_USER =
            "INSERT INTO User (username, mail, password) VALUES ( ?, ?, ?)";

    public static final String INSERT_USER_ROLE =
            "INSERT INTO User_Role (user_id, role_id) VALUES ( ?, ?)";

    public static final String UPDATE_USER =
            "UPDATE User SET (username, mail, password) = ( ?, ?, ?) WHERE User.id = ?";

}
