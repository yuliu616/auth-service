package com.yu.controller;

import com.yu.exception.RecordNotFoundException;
import com.yu.model.Role;
import com.yu.model.User;
import com.yu.modelMapper.UserMapper;
import com.yu.modelMapper.UserRoleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("${authService.apiBaseUrl}/role")
@RestController
public class RoleController {

    @Autowired
    protected UserMapper userMapper;

    @Autowired
    protected UserRoleMapper userRoleMapper;

    private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

    @Transactional
    @PostMapping("/{userId}/bind")
    public User bindRoleToUser(@PathVariable("userId") String userId,
                               @RequestBody List<Role> roleList)
    {
        logger.info("bindRoleToUser: user[{}] adding:{}", userId, roleList);
        User user = this.userMapper.findUserById(userId);
        if (user == null) {
            throw new RecordNotFoundException();
        }
        if (!roleList.isEmpty()) {
            this.userRoleMapper.insertUserRoleAsEntries(userId, roleList);
        }
        return user;
    }

    @Transactional
    @PostMapping("/{userId}/unbind")
    public User unbindRoleFromUser(@PathVariable("userId") String userId,
                                   @RequestBody List<Role> roleList)
    {
        logger.info("unbindRoleFromUser: user[{}] removing:{}", userId, roleList);
        User user = this.userMapper.findUserById(userId);
        if (user == null) {
            throw new RecordNotFoundException();
        }
        if (!roleList.isEmpty()) {
            this.userRoleMapper.deleteUserRoleAsEntries(userId, roleList);
        }
        return user;
    }

}
