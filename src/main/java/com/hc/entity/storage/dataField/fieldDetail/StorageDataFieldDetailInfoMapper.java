package com.hc.entity.storage.dataField.fieldDetail;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface StorageDataFieldDetailInfoMapper {
    int deleteByid(@Param("id")String id);

    int insertBatch(@Param("infos")List<StorageDataFieldDetailInfo> infos);

    List<StorageDataFieldDetailInfo> selectById(
    		@Param("id")String id, @Param("collectName")String collectName);

    int updateByPrimaryKeySelective(StorageDataFieldDetailInfo record);

    int updateByPrimaryKey(StorageDataFieldDetailInfo record);
}