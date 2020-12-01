package com.yu.modelMapper;

import com.yu.model.IntegerId;
import com.yu.model.User;
import org.apache.ibatis.annotations.Param;

/**
 * model mapper interface for model User
 */
public interface UserMapper {

    User findUserById(@Param("id") String id);

    long generateUserId(@Param("o") IntegerId integerId);

    long insertUserWithModel(@Param("u") User user);

    User findUserWithPasswordMatched(
            @Param("username") String username,
            @Param("passwordHash") String passwordHash);

}
