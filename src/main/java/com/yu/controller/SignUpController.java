package com.yu.controller;

import com.yu.exception.RecordInsertionFailException;
import com.yu.exception.SecurityRiskException;
import com.yu.model.User;
import com.yu.model.dto.LoginDto;
import com.yu.modelMapper.UserMapper;
import com.yu.util.MyBatisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequestMapping("${auth-service.api-base-url}/signUp")
@RestController
public class SignUpController {

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected PasswordController passwordController;

    private static final Logger logger = LoggerFactory.getLogger(SignUpController.class);

    @Transactional
    @PostMapping("")
    public User signUp(
            @RequestBody @Valid LoginDto dto)
    {
        logger.debug("signUp request");
        if (!dto.getPassword().trim().equals(dto.getPassword())) {
            throw new SecurityRiskException("password should not be started/ended with space char");
        }
        User newUser = MyBatisUtil.generateIdForModel(new User(),
                o->this.userMapper.generateUserId(o));
        newUser.setUsername(dto.getUsername());
        String passwordHash = passwordController.hashPassword(dto.getPassword(), dto);
        newUser.setPasswordHash(passwordHash);
        newUser.setActive(true); // user is active by default
        long created = this.userMapper.insertUserWithModel(newUser);
        if (created <= 0) {
            throw new RecordInsertionFailException("signup");
        }
        // return the newly created user
        return this.userMapper.findUserById(newUser.getId());
    }

}
