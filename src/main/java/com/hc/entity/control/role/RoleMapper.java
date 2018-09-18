package com.hc.entity.control.role;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface RoleMapper {
    int deleteRole(@Param("rid")Integer rid);

    int insertRoleSelective(Role record);

    List<Role> selectAll();

    int updateRoleSelective(Role record);
}