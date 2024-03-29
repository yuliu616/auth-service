package com.yu.controller;

import com.yu.exception.AuthenticationFailureException;
import com.yu.model.auth.User;
import com.yu.model.dto.auth.AuthRefreshDto;
import com.yu.model.dto.auth.AuthResultDto;
import com.yu.model.dto.auth.LoginDto;
import com.yu.modelMapper.UserMapper;
import com.yu.modelMapper.UserRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("${auth-service.api-base-url}/login")
@RestController
public class LoginController {

    @Value("${auth-service.options.echo-login-info}")
    protected boolean echoLoginInfo;

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected UserRoleMapper userRoleMapper;

    @Autowired
    protected PasswordController passwordController;

    @Autowired
    protected JwtTokenController jwtTokenController;

    @Autowired
    protected CurrentAuthController currentAuthController;

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @PostMapping("")
    public AuthResultDto login(@RequestBody @Valid LoginDto dto) {
        String username = dto.getUsername();
        String passwordHash = this.passwordController.hashPassword(dto.getPassword(), dto);
        User found = this.userMapper.findUserWithPasswordMatched(username, passwordHash);
        if (found != null) {
            List<String> roleList = this.userRoleMapper.findAllRoleOfUser(found.getId());
            AuthResultDto token = this.jwtTokenController.createLoginToken(
                    username, roleList.toArray(new String[0]));
            if (echoLoginInfo) {
                logger.info("login: username = [{}], roleList = {}",
                        username, roleList);
            }
            return token;
        } else {
            throw new AuthenticationFailureException();
        }
    }

    @GetMapping("/aboutMe")
    public Map<String, Object> aboutMe(){
        String username = currentAuthController.getCurrentUsername();
        String[] roleList = currentAuthController.getCurrentRoleList();
        logger.info("aboutMe: username = [{}], roleList = {}",
                username, roleList);
        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("role", roleList);
        return map;
    }

    @PostMapping("/refreshToken")
    public AuthRefreshDto refreshToken(){
        AuthRefreshDto token = new AuthRefreshDto();
        String username = this.jwtTokenController.createAuthRefreshToken(token);
        if (echoLoginInfo) {
            logger.info("refresh: username = [{}]", username);
        }
        return token;
    }

}
