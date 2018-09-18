package com.hc.entity.collection.application.running;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hc.vo.MultiConditionVo;

public interface CollectionApplicationRunningMapper {
	List<CollectionApplicationRunning> selectAll(
			@Param("csid")String csid, @Param("isUsed")Short isUsed);
	
    int insertBatch(@Param("insertCars")List<CollectionApplicationRunning> insertCars);

    int updateSelectiveBatch(@Param("updateCars")List<CollectionApplicationRunning> updateCars);
    
    List<CollectionApplicationRunning> selectByMultiCondition(
    		@Param("mcv")MultiConditionVo mcv, @Param("isUsedCar")int isUsedCar,
    		@Param("isUsedCt")int isUsedCt);
 
   
    
    int insertSelective(CollectionApplicationRunning record);

    int deleteByPrimaryKey(CollectionApplicationRunningKey key);

    int updateByPrimaryKey(CollectionApplicationRunning record);

}