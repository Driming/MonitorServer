package com.hc.entity.collection.task.status;

import com.hc.entity.collection.task.status.CollectionTaskTimePointStatus;
import com.hc.entity.collection.task.status.CollectionTaskTimePointStatusKey;

public interface CollectionTaskTimePointStatusMapper {
	int insert(CollectionTaskTimePointStatus record);
	
	int update(CollectionTaskTimePointStatus taskStatus);
	
	
    int deleteByPrimaryKey(CollectionTaskTimePointStatusKey key);

    int insertSelective(CollectionTaskTimePointStatus record);

    CollectionTaskTimePointStatus selectByPrimaryKey(CollectionTaskTimePointStatusKey key);

    int updateByPrimaryKeySelective(CollectionTaskTimePointStatus record);

    int updateByPrimaryKey(CollectionTaskTimePointStatus record);


}