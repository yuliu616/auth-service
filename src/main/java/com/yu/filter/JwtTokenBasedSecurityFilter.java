package com.yu.filter;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.yu.controller.JwtTokenController;
import com.yu.model.Role;
import com.yu.model.dto.AuthResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JwtTokenBasedSecurityFilter implements Filter {

    private static final String HEADER_AUTHORIZATION = "authorization";
    private static final String HEADER_VALUE_BEARER_PREFIX = "Bearer: ";

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenBasedSecurityFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException
    {
        if (request instanceof HttpServletRequest) {
            String authorizationHeader = ((HttpServletRequest)request)
                    .getHeader(HEADER_AUTHORIZATION);
            if (authorizationHeader != null
                && authorizationHeader.startsWith(HEADER_VALUE_BEARER_PREFIX))
            {
                String token = authorizationHeader.substring(HEADER_VALUE_BEARER_PREFIX.length());
//                logger.debug("token = {}", token);

                JwtTokenController jwtTokenController = getJwtTokenController(request.getServletContext());
                SignedJWT verifiedToken = jwtTokenController.verifyToken(new AuthResultDto(token));
                if (verifiedToken == null){
                    logger.debug("token is invalid: {}", token);
                } else {
//                    logger.debug("token verified good: {}", verifiedToken.getPayload().toString());
                    Authentication auth = buildAuthObjForSecurity(verifiedToken, jwtTokenController);
                    SecurityContext securityContext = SecurityContextHolder.getContext();
                    securityContext.setAuthentication(auth);
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private JwtTokenController getJwtTokenController(ServletContext servletContext){
        WebApplicationContext applicationContext =
                WebApplicationContextUtils.getWebApplicationContext(servletContext);
        JwtTokenController jwtTokenController =
                (JwtTokenController)applicationContext.getBean("jwtTokenController");
        return jwtTokenController;
    }

    private Authentication buildAuthObjForSecurity(
            SignedJWT verifiedToken,
            JwtTokenController jwtTokenController)
    {
        try {
            JWTClaimsSet claimsSet = verifiedToken.getJWTClaimsSet();
            String username = jwtTokenController.getUsernameInToken(claimsSet);
            Role[] roleList = jwtTokenController.getRoleListInToken(claimsSet);
//            logger.debug("user[{}] having roles: {}", username, roleList);
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    username, null, //no password
                    Arrays.asList(roleList) //make the user have this role granted.
                            .stream()
                            .map(role -> new SimpleGrantedAuthority(role.name()))
                            .collect(Collectors.toList())
            );
            return auth;
        } catch (ParseException ex) {
            logger.error("verified token but failed in parse", ex);
            return null;
        }
    }

}
