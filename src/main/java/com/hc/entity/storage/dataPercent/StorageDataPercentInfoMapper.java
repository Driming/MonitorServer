package com.hc.entity.storage.dataPercent;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface StorageDataPercentInfoMapper {
	List<StorageDataPercentInfo> selectTotal(@Param("id")String id);
	
    int deleteById(@Param("id")String id);

    int insertSelective(StorageDataPercentInfo record);
    
    int insertBatch(@Param("infos")List<StorageDataPercentInfo> infos);

	List<StorageDataPercentInfo> selectSizeInfos(@Param("id")String id);
}