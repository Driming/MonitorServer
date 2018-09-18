package com.hc.entity.storage.space;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface StorageSpaceInfoMapper {

    int insertStorageSpaceInfoSelective(StorageSpaceInfo record);

    List<StorageSpaceInfo> selectStorageSpaceInfos(@Param("id")String id);
    
    int updateStorageSpaceInfoSelective(StorageSpaceInfo record);
}