package com.hc.entity.control.access;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface AccessControlMapper {
    int deleteAccessControl(@Param("acid")int acid);

    int insertAccessControlSelective(AccessControl record);

    Integer selectMaxAcid();
    
    List<AccessControl> selectAll();

    int updateAccessControlSelective(AccessControl record);

}