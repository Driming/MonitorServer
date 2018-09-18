package com.hc.entity.collection.history;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hc.entity.collection.history.extra.StatusPercent.OneNode;
import com.hc.entity.collection.history.extra.TaskSchedule;
import com.hc.service.CollectionMonitorService.TimeType;
import com.hc.vo.MultiConditionVo;
import com.hc.vo.TaskHistoryPageInfoVo;
import com.hc.vo.TaskScheduleVo;

public interface CollectionHistoryMapper {
	int insertCollectionHistory(CollectionHistory ch);
	
	int updateCollectionHistorySelective(CollectionHistory ch);
	
	int upsert(CollectionHistory ch);
	
	Map<String, Long> selectAllServerMaxTime();
	
	CollectionHistory selectCollectionHistory(
			@Param("ctid")String ctid, @Param("csid")String csid,
			@Param("time")Long time);
	
	List<CollectionHistory> selectCollectionHistorys(
			@Param("ctid")String ctid, @Param("csid")String csid,
			@Param("hasError")Boolean hasError);
	
	int selectTotalTaskHistorysByCt(
			@Param("ctid")String ctid, @Param("csid")String csid);
	
	
	List<TaskHistoryPageInfoVo> selectTaskHistorysErrorNumPageInfo(
			@Param("ctid")String ctid, @Param("csid")String csid,
			@Param("pageSize")int pageSize);
	
	List<TaskHistoryPageInfoVo> selectTaskHistorysStandardPageInfo(
			@Param("ctid")String ctid, @Param("csid")String csid,
			@Param("pageSize")int pageSize);

	List<OneNode> selectAndCalcTaskHistoryStatusPercent(
			@Param("milli")long milli, @Param("label")String label);
	
	List<OneNode> selectTaskHistoryStatusPercentByTimeNodes(
			@Param("type")TimeType type, @Param("milli")long milli,
			@Param("label")String label);
	
	List<OneNode> selectTaskHistoryStatusPercentSumByTimeNodes(
			@Param("type")TimeType type, @Param("milli")long milli,
			@Param("label")String label);
	
	List<CollectionHistory> selectTaskHistoryResultNumAndSizeByHistoryTime(
			@Param("type")TimeType type, @Param("milli")long milli,
			@Param("label")String label);
	
	List<CollectionHistory> selectTaskHistoryResultNumAndSize(
			@Param("interval")int interval, @Param("label")String label,
			@Param("starttime")long starttime, @Param("endtime")long endtime);
	
	List<TaskSchedule> selectTaskHistoryInTaskSchedule(
			@Param("label")String label, @Param("milli")Long milli,
			@Param("tsv")TaskScheduleVo tsv, @Param("nodeSize")int nodeSize);
	
	List<CollectionHistory> selectTotalApplicationTaskHistoryResult(
			@Param("type")TimeType type, @Param("label")String label,
			@Param("starttime")long starttime, @Param("order") String order,
			@Param("size")int size);
	
	List<CollectionHistory> selectTaskHistoryPageByMultiCondition(MultiConditionVo mcv);

	List<Long> selectDayInfo(
			@Param("ctid")String ctid, @Param("csid")String csid,
			@Param("time")long time);

	List<CollectionHistory> selectByDay(
			@Param("ctid")String ctid, @Param("csid")String csid,
			@Param("year")int year, @Param("month")int month,
			@Param("day")int day);

}