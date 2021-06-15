package com.yu.model.auth;

/**
 * association between user and role
 */
public class UserRoleLink {

    public String userId;
    public Role role;

    public UserRoleLink(){}

    public UserRoleLink(String userId, Role role) {
        this.userId = userId;
        this.role = role;
    }
}
