package com.hc.entity.service.urlDir;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ServiceUrlDirMapper {
	List<ServiceUrlDir> selectAll(@Param("servers")List<String> servers);
	
	List<ServiceUrlDir> selectAllByRoot(@Param("servers")List<String> servers);
  
	int insertBatch(@Param("urlDirs")List<ServiceUrlDir> urlDirs);
	
	int updateServiceUrlDirBatch(@Param("updateUrlDirs")List<ServiceUrlDir> updateUrlDirs);

	int deleteServiceUrlDirBatch(@Param("removeUrlDirs")List<ServiceUrlDir> removeUrlDirs);
	
	int deleteByPrimaryKey();

    int insertSelective(ServiceUrlDir record);

    int updateByPrimaryKeySelective(ServiceUrlDir record);

    int updateByPrimaryKey(ServiceUrlDir record);

}