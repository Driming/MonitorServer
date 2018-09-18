package com.hc.entity.config;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface RequestConfigCacheMapper {
	RequestConfigCache selectConfigCache(@Param("csid")String csid);
	
	List<RequestConfigCache> selectConfigCaches();
	
	int insertConfigCache(RequestConfigCache config);
	
    int updateConfigCache(RequestConfigCache config);

}