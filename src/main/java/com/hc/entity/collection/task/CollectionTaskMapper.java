package com.hc.entity.collection.task;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hc.entity.collection.history.CollectionHistory;

public interface CollectionTaskMapper {
	List<String> selectAllDistinctTaskLabels(
			@Param("isUsedCt")Integer isUsedCt);
	
	List<CollectionTask> selectAllNotLabelTasks(
			@Param("isUsedCt")Integer isUsedCt);

	List<CollectionTask> selectCollectionTasks(
			@Param("csid")String csid, @Param("label")String label,
			@Param("caName")String caName, @Param("caVersion")String caVersion,
			@Param("isUsedCt")Integer isUsedCt);
	
	CollectionTask selectCollectionTaskByHistoryUpdate(
			@Param("ctid")String ctid, @Param("csid")String csid);
	
	int updateCollectionTaskByHistory(CollectionHistory ch);

	int updateCollectionTaskSelective(CollectionTask ct);
	
	int insertCollectionTaskSelective(CollectionTask ct);
	
	int insertCollectionTaskBatch(@Param("insertCts")List<CollectionTask> insertCts);

	int updateCollectionTaskBatch(@Param("updateCts")List<CollectionTask> updateCts);

	int deleteCollectionTaskBatch(@Param("removeCts")List<CollectionTask> removeCts);
	
}