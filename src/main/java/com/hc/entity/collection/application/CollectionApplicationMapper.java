package com.hc.entity.collection.application;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hc.vo.MultiConditionVo;

public interface CollectionApplicationMapper {
	List<CollectionApplication> selectCollectionApplication(
			@Param("name")String name, @Param("version")String version);
	
	List<CollectionApplication> selectCollectionApplications(
			@Param("isUsedCa")Integer isUsedCa);
	
	List<CollectionApplication> selectApplicationTasks(
			@Param("csid")String csid, @Param("isUsedCa")Integer isUsedCa,
			@Param("isUsedCt")Integer isUsedCt);
	
	List<CollectionApplication> selectCompleteCollectionApplications(
			@Param("csid")String csid, @Param("isUsedCa")Integer isUsedCa,
			@Param("isUsedCt")Integer isUsedCt);
	
	int insertCollectionApplication(CollectionApplication ca);
	
	int updateCollectionApplicationSelective(CollectionApplication ca);
	
	int insertCollectionApplicationBatch(
			@Param("insertCas")List<CollectionApplication> insertCas);
	
	int updateCollectionApplicationBatch(
			@Param("updateCas")List<CollectionApplication> updateCas);
	
	int deleteCollectionApplicationBatch(
			@Param("removeCas")List<CollectionApplication> removeCas);
	
	List<CollectionApplication> selectApplicationTasksByMultiCondition(
			@Param("mcv")MultiConditionVo mcv, @Param("isUsedCa") Integer isUsedCa,
			@Param("isUsedCt") Integer isUsedCt);

}