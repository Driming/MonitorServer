package com.hc.entity.service.urlInfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ServiceUrlInfoMapper {
	List<ServiceUrlInfo> selectAll(@Param("servers")List<String> servers);
   
	int insertBatch(@Param("urlInfos")List<ServiceUrlInfo> urlInfos);

	int updateServiceUrlInfoBatch(@Param("updateUrlInfos")List<ServiceUrlInfo> updateUrlInfos);
	
	int deleteServiceUrlInfoBatch(@Param("removeUrlInfos")List<ServiceUrlInfo> removeUrlInfos);
	
	int deleteByPrimaryKey();

    int insertSelective(ServiceUrlInfo record);

    int updateByPrimaryKeySelective(ServiceUrlInfo record);

    int updateByPrimaryKeyWithBLOBs(ServiceUrlInfo record);

    int updateByPrimaryKey(ServiceUrlInfo record);


}