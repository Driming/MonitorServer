package com.hc.entity.storage.dataField;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hc.entity.storage.dataPercent.StorageDataPercentInfo;

public interface StorageDataFieldInfoMapper {
	List<StorageDataFieldInfo> selectAllById(@Param("id")String id);
	
    int deleteById(@Param("id")String id);

    int insertBatch(@Param("infos")List<StorageDataFieldInfo> infos);

	List<StorageDataFieldInfo> selectCollectNames(@Param("id")String id);

}