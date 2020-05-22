package com.paymybuddy.payapp.enums;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {

    ADMIN(Role.ADMIN_DB_ID),
    USER(Role.USER_DB_ID);

    private static final int ADMIN_DB_ID = 0;
    private static final int USER_DB_ID = 1;

    private final int databaseId;

    Role(int databaseId) {
        this.databaseId = databaseId;
    }

    public static Role getRoleFromDatabaseId(int databaseId) {
        switch (databaseId) {
            case ADMIN_DB_ID:
                return ADMIN;
            case USER_DB_ID:
                return USER;
            default:
                return null;
        }
    }

    @Override
    public String getAuthority() {
        return this.name();
    }

    public int getDatabaseId() {
        return databaseId;
    }
}
