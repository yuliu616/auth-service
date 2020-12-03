package com.yu.modelMapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper {

    List<String> findAllRoleOfUser(@Param("userId") String userId);

    long insertUserRoleAsEntries(@Param("userId") String userId,
                                 @Param("roleList") List<String> roleList);

    long deleteUserRoleAsEntries(@Param("userId") String userId,
                                 @Param("roleList") List<String> roleList);

}
