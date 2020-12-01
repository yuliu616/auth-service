package com.yu.modelMapper;

import com.yu.model.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserRoleMapper {

    List<Role> findAllRoleOfUser(@Param("userId") String userId);

    long insertUserRoleAsEntries(@Param("userId") String userId,
                                 @Param("roleList") List<Role> roleList);

    long deleteUserRoleAsEntries(@Param("userId") String userId,
                                 @Param("roleList") List<Role> roleList);

}
