package com.paymybuddy.payapp.constants;

public final class DBStatements {

    public static final String GET_USER_BY_MAIL =
            "SELECT id, username, mail, password FROM User WHERE mail = ?";
}
