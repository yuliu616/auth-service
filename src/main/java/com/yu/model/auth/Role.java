package com.yu.model.auth;

/**
 * permission/definition of a role is defined by service themselves,
 * auth service only provide functionality to manage it.
 */
public class Role {

    /**
     * super role that means everything.
     */
    public static final String ROOT_ADMIN = "ROOT_ADMIN";

    /**
     * for auth service, allow add/remove role of user.
     */
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * nothing special
     */
    public static final String GENERAL_USER = "GENERAL_USER";

}
