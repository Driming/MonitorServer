package com.hc.entity.collection.task.record;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface CollectionTaskUpdateRecordMapper {
    int insertCollectionTaskUpdateRecordSelective(CollectionTaskUpdateRecord record);

    List<CollectionTaskUpdateRecord> selectAlls(@Param("type")Integer type);
    
    CollectionTaskUpdateRecord selectOne(@Param("id")int id);

    int updateCollectionTaskUpdateRecordSelective(CollectionTaskUpdateRecord record);
}