package com.hc.entity.control.middle.ra;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface RoleAccessMiddleMapper {
	List<RoleAccessMiddleKey> selectRoleAccesses();
	
    int deleteRoleAccessBatch(@Param("rids")List<Integer> rids);

    int insertRoleAccessBatch(@Param("ras")List<RoleAccessMiddleKey> ras);
}