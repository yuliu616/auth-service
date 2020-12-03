package com.yu.controller;

import com.yu.exception.UnhandledException;
import com.yu.model.dto.LoginDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class PasswordController {

    /**
     * charset of a password (normally, only plain english + ascii symbols will be used)
     */
    @Value("${auth-service.password.charset}")
    protected String passwordCharset;

    /**
     * algorithm used to hash(digital digest) a password
     */
    @Value("${auth-service.password.hash-algo}")
    protected String passwordHashAlgo;

    private static final Logger logger = LoggerFactory.getLogger(PasswordController.class);

    public String hashPassword(String password, LoginDto loginDto){
        String salt = loginDto.getUsername();
        return this.hashPassword(password, salt);
    }

    public String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance(passwordHashAlgo);
            String passwordWithSalt = password + "/" + salt;
            byte[] hash = md.digest(passwordWithSalt.getBytes(passwordCharset));
            String hashAsHex = DatatypeConverter.printHexBinary(hash);
            return hashAsHex.toUpperCase();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex ) {
            logger.error("hashPassword error", ex);
            throw new UnhandledException(ex);
        }
    }

}
