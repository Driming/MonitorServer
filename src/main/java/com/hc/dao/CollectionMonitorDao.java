package com.hc.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.hc.entity.collection.application.CollectionApplication;
import com.hc.entity.collection.application.CollectionApplicationMapper;
import com.hc.entity.collection.application.running.CollectionApplicationRunning;
import com.hc.entity.collection.application.running.CollectionApplicationRunningMapper;
import com.hc.entity.collection.history.CollectionHistory;
import com.hc.entity.collection.history.CollectionHistoryMapper;
import com.hc.entity.collection.history.extra.StatusPercent.OneNode;
import com.hc.entity.collection.history.extra.TaskSchedule;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.entity.collection.server.CollectionServerMapper;
import com.hc.entity.collection.task.CollectionTask;
import com.hc.entity.collection.task.CollectionTaskMapper;
import com.hc.entity.collection.task.record.CollectionTaskUpdateRecord;
import com.hc.entity.collection.task.record.CollectionTaskUpdateRecordMapper;
import com.hc.entity.collection.task.status.CollectionTaskTimePointStatus;
import com.hc.entity.collection.task.status.CollectionTaskTimePointStatusMapper;
import com.hc.entity.config.RequestConfigCache;
import com.hc.entity.config.RequestConfigCacheMapper;
import com.hc.interceptor.MapResultHandler;
import com.hc.service.CollectionMonitorService.TimeType;
import com.hc.vo.MultiConditionVo;
import com.hc.vo.TaskHistoryPageInfoVo;
import com.hc.vo.TaskScheduleVo;

@Repository
public class CollectionMonitorDao {
	@Autowired
	private CollectionServerMapper collectionServerMapper;
	
	@Autowired
	private CollectionApplicationMapper collectionApplicationMapper;
	
	@Autowired
	private CollectionApplicationRunningMapper collectionApplicationRunningMapper;
	
	@Autowired
	private CollectionHistoryMapper collectionHistoryMapper;
	
	@Autowired
	private CollectionTaskMapper collectionTaskMapper;
	
	@Autowired
	private RequestConfigCacheMapper requestConfigCacheMapper;
	
	@Autowired
	private CollectionTaskUpdateRecordMapper collectionTaskUpdateRecordMapper;
	
	@Autowired 
	private SqlSessionFactory sqlSessionFactory;
	
	@Autowired
	private CollectionTaskTimePointStatusMapper collectionTaskTimePointStatusMapper;
	
	//服务器
	public CollectionServer findCollectionServer(String csid) {
		return collectionServerMapper.selectCollectionServer(csid);
	}
	
	public List<CollectionServer> findCollectionServers(Integer isUsedCs, Short type){
		return collectionServerMapper.selectCollectionServers(isUsedCs, type);
	}
	
	public int addCollectionServer(CollectionServer cs) {
		return collectionServerMapper.insertCollectionServer(cs);
	}

	public int updateCollectionServerSelective(CollectionServer cs) {
		return collectionServerMapper.updateCollectionServerSelective(cs);
	}
	
	public CollectionServer findCollectionServerByCtid(String ctid, String csid){
		return collectionServerMapper.selectCollectionServerByCtid(ctid, csid); 
	}
	
	public int addCollectionServerBatch(List<CollectionServer> servers){
		return collectionServerMapper.insertBatch(servers);
	}
	
	public int updateCollectionServerBatch(List<CollectionServer> servers){
		return collectionServerMapper.updateBatch(servers);
	}
	
	public int removeCollectionServerBatch(List<CollectionServer> servers){
		return collectionServerMapper.deleteBatch(servers);
	}
	
	public int upsertCollectionServer(CollectionServer cs) {
		return collectionServerMapper.upsert(cs);
	}
	
	//应用
	public List<CollectionApplication> findCollectionApplication(String name, String version){
		return collectionApplicationMapper.selectCollectionApplication(name, version);
	}
	
	public List<CollectionApplication> findCollectionApplications(int isUsedCa) {
		return collectionApplicationMapper.selectCollectionApplications(isUsedCa);
	}
	
	public List<CollectionApplication> findCompleteCollectionApplications(
			String csid, Integer isUsedCa, Integer isUsedCt) {
		return collectionApplicationMapper.selectCompleteCollectionApplications(csid, isUsedCa, isUsedCt);
	}
	
	public int addCollectionApplication(CollectionApplication ca){
		return collectionApplicationMapper.insertCollectionApplication(ca);
	}
	
	public int updateCollectionApplication(CollectionApplication ca){
		return collectionApplicationMapper.updateCollectionApplicationSelective(ca);
	}
	
	public int addCollectionApplicationBatch(List<CollectionApplication> insertCas) {
		return collectionApplicationMapper.insertCollectionApplicationBatch(insertCas);
	}
	
	public int updateCollectionApplicationBatch(List<CollectionApplication> updateCas) {
		return collectionApplicationMapper.updateCollectionApplicationBatch(updateCas);
	}
	
	public List<CollectionApplication> findApplicationTasks(
			String csid, Integer isUsedCa, Integer isUsedCt) {
		return collectionApplicationMapper.selectApplicationTasks(csid, isUsedCa, isUsedCt);
	}
	
	public List<CollectionApplication> findApplicationTasksByMultiCondition(
			MultiConditionVo mcv, Integer isUsedCa, Integer isUsedCt) {
		return collectionApplicationMapper.selectApplicationTasksByMultiCondition(
				mcv, isUsedCa, isUsedCt);
	}

	//运行的应用
	public List<CollectionApplicationRunning> selectCollectionApplicationRunnings(String csid, Short isUsed){
		return collectionApplicationRunningMapper.selectAll(csid, isUsed);
	}
	
	public int addCollectionApplicationRunningBatch(List<CollectionApplicationRunning> insertCars){
		return collectionApplicationRunningMapper.insertBatch(insertCars);
	}
	
	public int updateCollectionApplicationRunningBatch(List<CollectionApplicationRunning> updateCars){
		return collectionApplicationRunningMapper.updateSelectiveBatch(updateCars);
	}
	
	public List<CollectionApplicationRunning> findApplicationRunningsByMultiCondition(MultiConditionVo mcv, int isUsedCar, int isUsedCt) {
		return collectionApplicationRunningMapper.selectByMultiCondition(
				mcv, isUsedCar, isUsedCt);
	}
	
	//任务
	public Integer updateCollectionTaskSelective(CollectionTask ct) {
		return collectionTaskMapper.updateCollectionTaskSelective(ct);
	}
	
	public List<CollectionTask> findCollectionTasks(
			String csid, String label, String caName, String caVersion, Integer isUsedCt) {
		return collectionTaskMapper.selectCollectionTasks(csid, label, caName, caVersion, isUsedCt);
	}
	
	public int addCollectionTaskSelective(CollectionTask ct){
		return collectionTaskMapper.insertCollectionTaskSelective(ct);
	}

	public int addCollectionTaskBatch(List<CollectionTask> insertCts) {
		return collectionTaskMapper.insertCollectionTaskBatch(insertCts);
	}

	public int updateCollectionTaskBatch(List<CollectionTask> updateCts) {
		return collectionTaskMapper.updateCollectionTaskBatch(updateCts);
	}
	
	public int updateCollectionTaskByHistory(CollectionHistory ch) {
		return collectionTaskMapper.updateCollectionTaskByHistory(ch);
	}
	
	public CollectionTask findCollectionTaskByHistoryUpdate(String ctid, String csid) {
		return collectionTaskMapper.selectCollectionTaskByHistoryUpdate(ctid, csid);
	}
	
	public List<String> findAllDistinctTaskLabels(Integer isUsedCt) {
		return collectionTaskMapper.selectAllDistinctTaskLabels(isUsedCt);
	}
	
	public List<CollectionTask> findAllNotLabelTasks(Integer isUsedCt) {
		return collectionTaskMapper.selectAllNotLabelTasks(isUsedCt);
	}
	
	public int addCollectionTaskUpdateRecordSelective(CollectionTaskUpdateRecord record){
		return collectionTaskUpdateRecordMapper.insertCollectionTaskUpdateRecordSelective(record);
	}
	
	public int updateCollectionTaskUpdateRecordSelective(CollectionTaskUpdateRecord record){
		return collectionTaskUpdateRecordMapper.updateCollectionTaskUpdateRecordSelective(record);
	}
	
	public List<CollectionTaskUpdateRecord> selectCollectionTaskUpdateRecords(Integer type){
		return collectionTaskUpdateRecordMapper.selectAlls(type);
	}
	
	public CollectionTaskUpdateRecord selectCollectionTaskUpdateRecord(int id){
		return collectionTaskUpdateRecordMapper.selectOne(id);
	}
	
	
	//历史
	public List<CollectionHistory> findCollectionHistorys(String ctid, String csid, Boolean hasError) {
		PageHelper.startPage(1, 10000);
		return collectionHistoryMapper.selectCollectionHistorys(ctid, csid, hasError);
	}

	public int addCollectionHistory(CollectionHistory ch) {
		return collectionHistoryMapper.insertCollectionHistory(ch);
	}
	
	public List<OneNode> findTaskHistoryStatusPercent(long milli, String label) {
		return collectionHistoryMapper.selectAndCalcTaskHistoryStatusPercent(milli, label);
	}
	
	public List<CollectionHistory> calcTotalApplicationTasksHistoryResult(
			TimeType type, String label, long starttime, String order, int size) {
		return collectionHistoryMapper.selectTotalApplicationTaskHistoryResult(
				type, label, starttime, order, size);
	}
	
	public List<OneNode> findTaskHistoryStatusPercentByTimeNodes(TimeType type, long milli, String label) {
		return collectionHistoryMapper.selectTaskHistoryStatusPercentByTimeNodes(type, milli, label);
	}
	
	public List<OneNode> findTaskHistoryStatusPercentSumByTimeNodes(TimeType type, long milli, String label){
		return collectionHistoryMapper.selectTaskHistoryStatusPercentSumByTimeNodes(type, milli, label);
	}
	
	public List<CollectionHistory> findTaskHistoryResultNumAndSize(
			int interval, String label, long starttime, long endtime) {
		return collectionHistoryMapper.selectTaskHistoryResultNumAndSize(
				interval, label, starttime, endtime);
	}
	
	public List<TaskSchedule> findTaskHistoryInTaskSchedule(
			String label, Long milli, TaskScheduleVo tsv, int nodeSize) {
		return collectionHistoryMapper.selectTaskHistoryInTaskSchedule(
				label, milli, tsv, nodeSize);
	}
	
	public Page<CollectionHistory> findTaskHistoryPageByMultiCondition(MultiConditionVo mcv) {
		PageHelper.startPage(mcv.getCurrentPage(), mcv.getPageSize());
		return (Page<CollectionHistory>) collectionHistoryMapper.selectTaskHistoryPageByMultiCondition(mcv);
	}

	public List<CollectionHistory> findTaskHistoryResultNumAndSizeByHistoryTime(
			TimeType type, long milli, String label) {
		return collectionHistoryMapper.selectTaskHistoryResultNumAndSizeByHistoryTime(type, milli, label);
	}
	
	public Page<CollectionHistory> findCollectionHistorysPage(
			String ctid, String csid, int currentPage, int pageSize) {
		PageHelper.startPage(currentPage, pageSize);
		return (Page<CollectionHistory>) collectionHistoryMapper.selectCollectionHistorys(ctid, csid, null);
	}

	public int findTotalTaskHistorys(String ctid, String csid) {
		return collectionHistoryMapper.selectTotalTaskHistorysByCt(ctid, csid);
	}
	
	public List<TaskHistoryPageInfoVo> findTaskHistorysStandardPageInfo(
			String ctid, String csid, int pageSize) {
		return collectionHistoryMapper.selectTaskHistorysStandardPageInfo(ctid, csid, pageSize);
	}
	
	public List<TaskHistoryPageInfoVo> findTaskHistorysErrorNumPageInfo(
			String ctid, String csid, int pageSize) {
		return collectionHistoryMapper.selectTaskHistorysErrorNumPageInfo(ctid, csid, pageSize);
	}
	
	public CollectionHistory findCollectionHistory(String ctid, String csid, Long time){
		return collectionHistoryMapper.selectCollectionHistory(ctid, csid, time);
	}
	
	public int updateCollectionHistorySelective(CollectionHistory ch){
		return collectionHistoryMapper.updateCollectionHistorySelective(ch);
	}
	
	public Map<String, Long> selectCollectionHistoryServerMaxTime(){
		return collectionHistoryMapper.selectAllServerMaxTime();
	}
	
	public List<Long> findTaskHistoryDayInfo(String ctid, String csid, long time, Integer totalPage) {
		PageHelper.startPage(1, totalPage);
		return collectionHistoryMapper.selectDayInfo(ctid, csid, time);
	}

	public List<CollectionHistory> findTaskHistoryByDay(
			String ctid, String csid, int year, int month, int day) {
		return collectionHistoryMapper.selectByDay(ctid, csid, year, month, day);
	}
	
	public int upsertCollectionHistory(CollectionHistory ch) {
		return collectionHistoryMapper.upsert(ch);
	}
	
	//stomp配置
	public RequestConfigCache findConfigCache(String csid){
		return requestConfigCacheMapper.selectConfigCache(csid);
	}
	
	public List<RequestConfigCache> findConfigCache(){
		return requestConfigCacheMapper.selectConfigCaches();
	}
	
	public int addConfigCache(RequestConfigCache config){
		return requestConfigCacheMapper.insertConfigCache(config);
	}
	
	public int updateConfigCache(RequestConfigCache config){
		return requestConfigCacheMapper.updateConfigCache(config);
	}

	//任务时间点状态
	public int addCollectionTaskTimePointStatus(CollectionTaskTimePointStatus taskStatus) {
		return collectionTaskTimePointStatusMapper.insert(taskStatus);
	}

	public int upsertCollectionTaskTimePointStatus(CollectionTaskTimePointStatus taskStatus) {
		return collectionTaskTimePointStatusMapper.upsert(taskStatus);
	}

}
