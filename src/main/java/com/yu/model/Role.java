package com.yu.model;

/**
 * permission/definition of a role is defined by service themselves,
 * auth service only provide functionality to manage it.
 */
public enum Role {

    /**
     * super role that means everything.
     */
    ROOT_ADMIN,

    /**
     * for auth service, allow managing user record.
     */
    USER_ADMIN,

    /**
     * for auth service, allow add/remove role of user.
     */
    ROLE_ADMIN,

    /**
     * nothing special
     */
    GENERAL_USER,

}
