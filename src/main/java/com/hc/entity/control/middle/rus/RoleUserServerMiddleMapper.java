package com.hc.entity.control.middle.rus;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface RoleUserServerMiddleMapper {
	List<RoleUserServerMiddleKey> selectAll(
			@Param("csid")String csid, @Param("uid")Integer uid);
	
    int deleteRoleUserServer(RoleUserServerMiddleKey key);
    
    int deleteRoleUserServerByUid(Integer uid);

    int insertRoleUserServer(RoleUserServerMiddleKey record);
    
    int insertRoleUserServerBatch(
    		@Param("ruses")List<RoleUserServerMiddleKey> ruses);
    
    int updateRoleUserServer(@Param("oldRecord")RoleUserServerMiddleKey oldRecord, 
    		@Param("newRecord")RoleUserServerMiddleKey newRecord);

	List<Integer> selectUserAccesses(@Param("csid")String csid, @Param("uid")Integer uid);
}