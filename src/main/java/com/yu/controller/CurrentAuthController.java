package com.yu.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentAuthController {

    private static final Logger logger = LoggerFactory.getLogger(CurrentAuthController.class);

    public String getCurrentUsername() {
        UsernamePasswordAuthenticationToken auth = getCurrentAuth();
        return (String)auth.getPrincipal();
    }

    public String[] getCurrentRoleList() {
        UsernamePasswordAuthenticationToken auth = getCurrentAuth();
        return auth.getAuthorities().stream()
                .map(a->a.getAuthority())
                .toArray(String[]::new);
    }

    private UsernamePasswordAuthenticationToken getCurrentAuth(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication auth = securityContext.getAuthentication();
        return (UsernamePasswordAuthenticationToken)auth;
    }

}
