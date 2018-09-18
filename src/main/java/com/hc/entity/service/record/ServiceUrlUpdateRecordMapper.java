package com.hc.entity.service.record;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ServiceUrlUpdateRecordMapper {
	List<ServiceUrlUpdateRecord> selectAll(
			@Param("year")Integer year, @Param("month")Integer month,
			@Param("day")Integer day, @Param("record")ServiceUrlUpdateRecord record,
			@Param("pageSize")int pageSize);
	
	int insertBatch(@Param("records")List<ServiceUrlUpdateRecord> records);
	
	int updateOne(ServiceUrlUpdateRecord record);
	
	
	
    int deleteByPrimaryKey(Integer id);

    int insert(ServiceUrlUpdateRecord record);

    int insertSelective(ServiceUrlUpdateRecord record);

    ServiceUrlUpdateRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ServiceUrlUpdateRecord record);

    int updateByPrimaryKeyWithBLOBs(ServiceUrlUpdateRecord record);

    int updateByPrimaryKey(ServiceUrlUpdateRecord record);

	



}