package com.hc.entity.control.user;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface UserMapper {
	
    int deleteUser(@Param("uid")Integer uid);

    int insertUserSelective(User record);
    
    User loginUser(User record);
    
    List<User> selectUserByUsername(
    		@Param("uid")Integer uid, @Param("username")String username,
    		@Param("phone")String phone);

    List<User> selectAll(@Param("isPass")Short isPass);

    int updateUsreSelective(User record);

	User selectUserByUidAndPhone(
			@Param("uid")Integer uid, @Param("phone")String phone);
}