package com.hc.service;

import java.text.ParseException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.hc.dao.CollectionMonitorDao;
import com.hc.entity.collection.application.CollectionApplication;
import com.hc.entity.collection.application.running.CollectionApplicationRunning;
import com.hc.entity.collection.history.CollectionHistory;
import com.hc.entity.collection.history.extra.StatusPercent.OneNode;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.entity.collection.task.CollectionTask;
import com.hc.entity.collection.task.record.CollectionTaskUpdateRecord;
import com.hc.entity.control.user.User;
import com.hc.task.MonitorTask;
import com.hc.util.MessageUtils;
import com.hc.util.StringUtils;
import com.hc.util.TimeUtils;
import com.hc.util.map.ServerMap;
import com.hc.util.map.TaskMap;
import com.hc.util.map.cache.MonitorIndexCaches;
import com.hc.util.page.CommonPageListInfo;
import com.hc.util.page.PageErrorException;
import com.hc.util.page.template.PageListInfo;
import com.hc.util.verification.AccessController;
import com.hc.util.verification.builder.TaskModifyFlagControllerBuilder;
import com.hc.util.websocket.feedback.WebSocketFeedbackFlag;
import com.hc.vo.CollectionAllInfoVo;
import com.hc.vo.MultiConditionVo;
import com.hc.vo.NodeResultVo;
import com.hc.vo.RegisterServerVo;
import com.hc.vo.RegisterServerVo.Driver;
import com.hc.vo.RegisterServerVo.Task;
import com.hc.vo.StatusPercentVo;
import com.hc.vo.TaskHistoryPageInfoVo;
import com.hc.vo.TaskScheduleVo;
import com.hc.vo.TaskUpdateReplyVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
public class CollectionMonitorService {
	@Autowired
	private CollectionMonitorDao collectionMonitorDao;
	
	@Autowired
	private WebSocketSendService webSocketSendService;

	private MonitorTask monitorTask;

	public void setCommTask(MonitorTask monitorTask) {
		this.monitorTask = monitorTask;
	}
	
	public void addCollectionServers(List<CollectionServer> servers, Integer isUsedCs, Short type){
		if(!servers.isEmpty())
			cudCollectionServer(servers, isUsedCs, type);
	}
	
	public void cudCollectionServers(List<CollectionServer> addCollectionServers, List<CollectionServer> updateCollectionServers, List<CollectionServer> removeCollectionServers){
		cudCollectionServer(addCollectionServers, updateCollectionServers, removeCollectionServers);
	}
	
	// 获取所有采集监控信息
	public Object findAllCollectionInfo(Integer isUsedCs, Integer isUsedCa,
			Integer isUsedCt, Short type) {
		CollectionAllInfoVo civ = new CollectionAllInfoVo();
		List<CollectionServer> css = collectionMonitorDao.findCollectionServers(isUsedCs, type);
		List<CollectionApplication> cas = collectionMonitorDao.findCompleteCollectionApplications(null, isUsedCa, isUsedCt);
		List<CollectionTask> cts = collectionMonitorDao.findCollectionTasks(null, null, null, null, isUsedCt);
		List<String> labels = collectionMonitorDao.findAllDistinctTaskLabels(isUsedCt);
		civ.setServers(css);
		civ.setDrivers(cas);
		civ.setTasks(cts);
		civ.setLabels(labels);
		return MessageUtils.returnSuccess(civ);
	}

	// 注册服务/应用/任务信息
	public Object addCollectionServer(String message, String ip, int port) {
		if (message == null)
			return MessageUtils.parameterNullError();

		CollectionServer cs = new CollectionServer();
		Map<String, Object> classMap = new HashMap<String, Object>();
		classMap.put("driver", Driver.class);
		classMap.put("task", Task.class);
		JsonConfig jsonConfig = new JsonConfig();
		jsonConfig.setExcludes(new String[] { "dispatchConfig", "config" });
		RegisterServerVo rsv = (RegisterServerVo) JSONObject.toBean(JSONObject.fromObject(message, jsonConfig),
				RegisterServerVo.class, classMap);
		JSONArray taskJson = JSONObject.fromObject(message).getJSONArray("task");
		JSONArray driverJson = JSONObject.fromObject(message).getJSONArray("driver");
		for (int i = 0; i < taskJson.size(); i++){
			rsv.getTask().get(i).setDispatchConfig(JSONObject.fromObject(taskJson.get(i)).getString("dispatchConfig"));
			rsv.getTask().get(i).setConfig(JSONObject.fromObject(taskJson.get(i)).getString("config"));
		}
		for (int i = 0; i < driverJson.size(); i++)
			rsv.getDriver().get(i).setConfig(JSONObject.fromObject(driverJson.get(i)).getString("config"));
		cs.setCsid(rsv.getId());
		cs.setServer(StringUtils.join(ip, ":", port));
		cs.setType(ServerMap.COLLECT_SERVER);

		collectionMonitorDao.upsertCollectionServer(cs);

		cudCollectionApplication(rsv.getDriver(), cs.getCsid());
		cudCollectionTask(rsv.getTask(), cs.getCsid());

		return MessageUtils.operationSuccess();
	}

	// 注册应用信息
	private void cudCollectionApplication(List<Driver> drivers, String csid) {
		List<CollectionApplication> insertCas = new LinkedList<CollectionApplication>();
		List<CollectionApplication> updateCas = new LinkedList<CollectionApplication>();
		List<CollectionApplication> existCas = collectionMonitorDao.findCompleteCollectionApplications(null, null, null);
		if (drivers == null || drivers.isEmpty())
			return;
		for (int i = 0; i < drivers.size(); i++) {
			Driver driver = drivers.get(i);
			CollectionApplication ca = new CollectionApplication();
			ca.setEgName(driver.getName());
			ca.setVersion(driver.getVersion());
			ca.setChName(driver.getAlias());
			ca.setConfig(driver.getConfig());
			
			ca.setIsUsed((short) 1);
			ca.setCsid(csid);
			if (existCas.isEmpty())
				insertCas.add(ca);
			for (CollectionApplication existCa : existCas) {
				if (existCa.getEgName().equals(ca.getEgName()) 
						&& existCa.getVersion().equals(ca.getVersion())) {
					updateCas.add(ca);
					break;
				}
				if (existCas.indexOf(existCa) == existCas.size() - 1)
					insertCas.add(ca);
			}
		}
		if (!insertCas.isEmpty())
			collectionMonitorDao.addCollectionApplicationBatch(insertCas);
		if (!updateCas.isEmpty())
			collectionMonitorDao.updateCollectionApplicationBatch(updateCas);
	}

	// 注册任务信息
	public void cudCollectionTask(List<Task> tasks, String csid) {
		List<CollectionTask> removeCts = new LinkedList<CollectionTask>();
		List<CollectionTask> insertCts = new LinkedList<CollectionTask>();
		List<CollectionTask> updateCts = new LinkedList<CollectionTask>();
		List<CollectionTask> existCts = collectionMonitorDao.findCollectionTasks(csid, null, null, null, null);
		removeCts.addAll(existCts);
		if (tasks == null || tasks.isEmpty())
			return;
		for (int i = 0; i < tasks.size(); i++) {
			Task task = tasks.get(i);
			CollectionTask ct = new CollectionTask();
			String ctid = null;
			if (task != null) {
				ctid = task.getId();
				ct.setCtid(ctid);
				ct.setCsid(csid);
				ct.setName(task.getName());
				ct.setDriverName(task.getDriverName());
				ct.setDriverVersion(task.getDriverVersion());
				ct.setType(task.getType());
				ct.setConfig(task.getConfig());
				ct.setDispatchConfig(task.getDispatchConfig());
			}
			if (existCts.isEmpty())
				insertCts.add(ct);
			for (CollectionTask existCt : existCts) {
				if (existCt.getCtid().equals(ctid) 
						&& existCt.getCsid().equals(csid)) {
					removeCts.remove(existCt);
					ct.setIsUsed((short) 1);
					updateCts.add(ct);
					break;
				}
				if (existCts.indexOf(existCt) == existCts.size() - 1)
					insertCts.add(ct);
			}
		}
		if (!insertCts.isEmpty())
			collectionMonitorDao.addCollectionTaskBatch(insertCts);
		if (!updateCts.isEmpty())
			collectionMonitorDao.updateCollectionTaskBatch(updateCts);
		
		for(CollectionTask removeCt : removeCts)
			removeCt.setIsUsed((short) 0);
		if (!removeCts.isEmpty())
			collectionMonitorDao.updateCollectionTaskBatch(removeCts);
	}
	
	//更改服务器信息
	public Object updateCollectionServer(CollectionServer cs) {
		if(cs.getCsid() == null)
			return MessageUtils.parameterNullError();
		
		int result = collectionMonitorDao.updateCollectionServerSelective(cs);
		if(result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}
	
	//更改应用信息
	public Object updateCollectionApplication(CollectionApplication ca) {
		if(ca.getEgName() == null || ca.getVersion() == null){
			return MessageUtils.parameterNullError();
		}
		int result = collectionMonitorDao.updateCollectionApplication(ca);
		if(result <= 0){
			return MessageUtils.operationFailedError();
		}else{
			return MessageUtils.operationSuccess();
		}
	}

	
	//创建任务
	public Object addCollectionTask(CollectionTask ct, User user) {
		if(ct.getCsid() == null 
				|| ct.getDriverName() == null
				|| ct.getDriverVersion() == null
				|| ct.getConfig() == null){
			return MessageUtils.parameterNullError();	//判断是否有传入信息，否则返回参数为空错误
		}

		//权限控制
		if(user == null || !AccessController.createTask(ct.getCsid(), user.getUid()))
			return MessageUtils.permissionDeniedError();
		/*boolean isAccess = AccessController.createTask(ct.getCsid(), user.getUid());
		if(!isAccess)
			return MessageUtils.permissionDeniedError();*/

		//判断是否拥有写权限
		Integer eid;
		boolean isWritabled = TaskModifyFlagControllerBuilder.buildTaskCreateController()
				.isWritabled(ct.getCsid());
		if(isWritabled){
			int type = TaskMap.CREATE_TASK_RECORD;
			eid = WebSocketFeedbackFlag.put(type, user, ct);
			//添加创建任务记录
			user.setPassword(null);
			CollectionTaskUpdateRecord record = new CollectionTaskUpdateRecord(eid, type,
					JSONObject.fromObject(user, StringUtils.jsonIgnoreNull()).toString(),
					JSONObject.fromObject(ct, StringUtils.jsonIgnoreNull()).toString());
			collectionMonitorDao.addCollectionTaskUpdateRecordSelective(record);
			// webSocket发送任务状态给监控客户端
			ct.setCtid("0");
			webSocketSendService.updateTaskConfigure(eid, ct);
		}else{
			return MessageUtils.otherUserWorkingThisTaskError();
		}
		return MessageUtils.returnSuccess(MessageUtils.writeMessage("id", eid));
	}

	// 更改任务状态
	public Object updateTaskStatus(CollectionTask ct, User user) {
		if (ct == null 
				|| ct.getCtid() == null 
				|| ct.getCsid() == null 
				|| ct.getStatus() == null)
			return MessageUtils.parameterNullError();

		// 权限控制
		if (user == null)
			return MessageUtils.permissionDeniedError();
		boolean isAccess = AccessController.controlTask(ct.getCsid(), user.getUid(), ct.getStatus());
		if(!isAccess)
			return MessageUtils.permissionDeniedError();

		//判断是否拥有写权限
		Integer eid = null;
		boolean isWritabled = TaskModifyFlagControllerBuilder.buildTaskStatusController()
				.isWritabled(ct.getCsid(), ct.getCtid());
		if(isWritabled){
			int type = TaskMap.UPDATE_TASK_STATUS_RECORD;
			eid = WebSocketFeedbackFlag.put(type, user, ct);
			//添加创建任务记录
			user.setPassword(null);
			CollectionTaskUpdateRecord record = new CollectionTaskUpdateRecord(eid, type,
					JSONObject.fromObject(user, StringUtils.jsonIgnoreNull()).toString(),
					JSONObject.fromObject(ct, StringUtils.jsonIgnoreNull()).toString());
			collectionMonitorDao.addCollectionTaskUpdateRecordSelective(record);
			// webSocket发送任务状态给监控客户端 
			webSocketSendService.updateTaskStatus(eid, ct);
		}else
			return MessageUtils.otherUserWorkingThisTaskError();

		return MessageUtils.returnSuccess(MessageUtils.writeMessage("id", eid));
	}

	// 删除服务器
	public Object deleteCollectionServer(String csid) {
		if (csid == null)
			return MessageUtils.parameterNullError();

		CollectionServer cs = new CollectionServer();
		cs.setCsid(csid);
		cs.setIsUsed((short) 0);
		Integer result = collectionMonitorDao.updateCollectionServerSelective(cs);
		if (result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	@Deprecated
	// 多条件获取任务信息
	public Object findApplicationTasksByMultiCondition(
			MultiConditionVo mcv, Integer isUsedCa, Integer isUsedCt) {
		if (mcv.getStarttime() == null)
			mcv.setStarttime(0l);
		if (mcv.getEndtime() == null)
			mcv.setEndtime(System.currentTimeMillis());
		return collectionMonitorDao.findApplicationTasksByMultiCondition(mcv, isUsedCa, isUsedCt);
	}
	
	// 多条件获取运行应用信息
	public Object findApplicationRunningsByMultiCondition(MultiConditionVo mcv, int isUsedCar, int isUsedCt) {
		if (mcv.getStarttime() == null)
			mcv.setStarttime(0l);
		if (mcv.getEndtime() == null)
			mcv.setEndtime(System.currentTimeMillis());
		return collectionMonitorDao.findApplicationRunningsByMultiCondition(mcv, isUsedCar, isUsedCt);
	}

	public Object controlCollectDispatchSystem(String csid, String command, User user) {
		if(user == null)
			return MessageUtils.permissionDeniedError();
		
		boolean isAccess = AccessController.controlSystem(csid, user.getUid());
		if(!isAccess)
			return MessageUtils.permissionDeniedError();
		
		// webSocket发送任务配置修改给采集监控客户端
		webSocketSendService.controlCollectDispatchSystem(csid, command);
		return MessageUtils.operationSuccess();
	}
	
	// 更改任务配置
	public Object updateTaskConfigure(CollectionTask ct, User user) {
		if (ct == null || ct.getCtid() == null || ct.getCsid() == null)
			return MessageUtils.parameterNullError();

		// 权限控制
		if (user == null)
			return MessageUtils.permissionDeniedError();
		boolean isAccess = AccessController.updateConfig(ct.getCsid(), user.getUid());
		if(!isAccess)
			return MessageUtils.permissionDeniedError();

		//判断是否拥有写权限
		Integer eid  = null;
		boolean isWritabled = TaskModifyFlagControllerBuilder.buildTaskConfigController()
				.isWritabled(ct.getCsid(), ct.getCtid());
		if(isWritabled){
			int type = TaskMap.UPDATE_TASK_CONFIG_RECORD;
			eid = WebSocketFeedbackFlag.put(type, user, ct);
			//添加更改任务配置记录
			user.setPassword(null);
			CollectionTaskUpdateRecord record = new CollectionTaskUpdateRecord(eid, type,
					JSONObject.fromObject(user, StringUtils.jsonIgnoreNull()).toString(),
					JSONObject.fromObject(ct, StringUtils.jsonIgnoreNull()).toString());
			collectionMonitorDao.addCollectionTaskUpdateRecordSelective(record);
			// webSocket发送任务配置修改给采集监控客户端
			webSocketSendService.updateTaskConfigure(eid, ct);
			
		}else
			return MessageUtils.otherUserWorkingThisTaskError();

		return MessageUtils.returnSuccess(MessageUtils.writeMessage("id", eid));
	}

	// 更改任务标签
	public Object updateTaskLabel(CollectionTask ct) {
		if (ct == null || ct.getCtid() == null || 
				ct.getCsid() == null || ct.getLabel() == null)
			return MessageUtils.parameterNullError();

		int result = collectionMonitorDao.updateCollectionTaskSelective(ct);
		if (result <= 0)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}
	
	//获取任务更改的返回状态
	public Object findCollectionTaskUpdateRecord(Integer id) {
		if(id == null)
			return MessageUtils.parameterNullError();
		
		CollectionTaskUpdateRecord record = collectionMonitorDao.selectCollectionTaskUpdateRecord(id);
		if(record == null)
			return MessageUtils.parameterNotStandardVauleError();
		
		if(record.getResponseMessage() == null)
			return MessageUtils.returnSuccess(null);
		
		TaskUpdateReplyVo replyVo = new TaskUpdateReplyVo();
		replyVo.setId(record.getId());
		replyVo.setType(record.getType());
		JSONObject requestJson = JSONObject.fromObject(record.getRequestMessage());
		if(requestJson.containsKey("ctid"))
			replyVo.setCtid(requestJson.getString("ctid"));
		if(requestJson.containsKey("csid"))
			replyVo.setCsid(requestJson.getString("csid"));
		JSONObject responseJson = JSONObject.fromObject(record.getResponseMessage());
		if(responseJson.containsKey("ctid"))
			replyVo.setCtid(responseJson.getString("ctid"));
		if(responseJson.containsKey("code"))
			replyVo.setStatusCode(responseJson.getInt("code"));
		if(responseJson.containsKey("msg"))
			replyVo.setMessage(responseJson.getString("msg"));
		return MessageUtils.returnSuccess(replyVo);
	}

	// 根据任务,分页获取任务历史数据
	@Deprecated
	public Object findApplicationTaskHistorysPage(
			String ctid, String csid, int currentPage, Integer pageSize, Integer totalPage) {
		int totalCount = collectionMonitorDao.findTotalTaskHistorys(ctid, csid);
		if (totalCount / totalPage < pageSize)
			pageSize = (int) Math.ceil(((float) totalCount) / totalPage);
		
		Page<CollectionHistory> chs = collectionMonitorDao.findCollectionHistorysPage(
				ctid, csid, currentPage, pageSize);
		PageListInfo<CollectionHistory> pageInfo = new PageListInfo<CollectionHistory>();
		pageInfo.setCurrentPage(currentPage);
		pageInfo.setPerPageCount(pageSize);
		pageInfo.setTotalCount(totalCount);
		pageInfo.setPageCount(chs.getPages());
		pageInfo.setObjs(chs);

		if (currentPage <= 0 || (currentPage > chs.getPages() && currentPage != 1))
			return MessageUtils.requestPageError();
		return MessageUtils.returnSuccess(pageInfo);
	}

	// 根据任务,获取分页的任务历史数据概要信息
	@Deprecated
	public Object findApplicationTaskHistorysPageInfo(
			String ctid, String csid, Integer pageSize,
			Integer totalPage, Integer currentPage, Integer perPageSize) {
		int totalCount = collectionMonitorDao.findTotalTaskHistorys(ctid, csid);
		if (totalCount / totalPage < pageSize)
			pageSize = (int) Math.ceil(((float) totalCount) / totalPage);

		List<TaskHistoryPageInfoVo> pageInfoVos = collectionMonitorDao.findTaskHistorysStandardPageInfo(ctid, csid, pageSize);
		List<TaskHistoryPageInfoVo> errNumPages = collectionMonitorDao.findTaskHistorysErrorNumPageInfo(ctid, csid,pageSize);
		int index = 0;
		for (TaskHistoryPageInfoVo pageInfoVo : pageInfoVos) {
			for (int i = index; i < errNumPages.size();) {
				TaskHistoryPageInfoVo errNumPage = errNumPages.get(i);
				if (pageInfoVo.getCurrentPage().equals(errNumPage.getCurrentPage())) {
					pageInfoVo.setErrPercent(errNumPage.getErrPercent() / pageInfoVo.getErrPercent() * 100);
					index += 1;
					break;
				}
				pageInfoVo.setErrPercent(0.0f);
				break;
			}
		}
		
		try {
			CommonPageListInfo<TaskHistoryPageInfoVo> commonPage = new CommonPageListInfo<>();
			return MessageUtils.returnSuccess(commonPage.page(pageInfoVos, currentPage, perPageSize));
		} catch (PageErrorException e) {
			e.printStackTrace();
			return MessageUtils.pageOutOfRange();
		}
	}
	
	//获取任务历史的日期信息
	public Object findApplicationTaskHistorysDayInfo(String ctid, String csid, long time, Integer totalPage) {
		time = time - (time+8*60*60*1000)%(24*60*60*1000) + 24*60*60*1000;
		List<Long> times = collectionMonitorDao.findTaskHistoryDayInfo(ctid, csid, time, totalPage);
		return MessageUtils.returnSuccess(times);
	}
	
	//根据日期获取任务历史数据
	public Object findApplicationTaskHistoryByDay(String ctid, String csid, String time) {
		String[] times = time.split("-");
		if(times.length < 3)
			MessageUtils.parameterNotStandardVauleError();
		
		int year = Integer.valueOf(times[0]);
		int month = Integer.valueOf(times[1]);
		int day = Integer.valueOf(times[2]);
		List<CollectionHistory> chs = collectionMonitorDao.findTaskHistoryByDay(
				ctid, csid, year, month, day);
		return MessageUtils.returnSuccess(chs);
	}

	// 获取按日，月，年返回任务状态
	public Object findTaskHistoryStatusPercentByTimeNodes(TimeType type, String label) {
		long milli = System.currentTimeMillis();
		List<StatusPercentVo> spvs = new LinkedList<StatusPercentVo>();
		List<OneNode> perTimeSumDatas = collectionMonitorDao.findTaskHistoryStatusPercentSumByTimeNodes(type, milli,
				label);
		List<OneNode> chs = collectionMonitorDao.findTaskHistoryStatusPercentByTimeNodes(type, milli, label);
		StatusPercentVo spv = null;
		String status = null;
		List<Integer> times = null;
		List<Float> percents = null;
		for (OneNode ch : chs) {
			String statusName = ch.getStatusName();
			if (!statusName.equals(status)) {
				spv = new StatusPercentVo();
				times = new LinkedList<Integer>();
				percents = new LinkedList<Float>();
				spv.setStatus(statusName);
				spv.setTimes(times);
				spv.setPercents(percents);
				status = statusName;
				spvs.add(spv);
			}
			times.add(ch.getTime());
			for (OneNode node : perTimeSumDatas) {
				if (node != null && node.getTime() != null && node.getTime().equals(ch.getTime())) {
					percents.add(ch.getPercent() / node.getPercent() * 100);
					break;
				}

			}

		}
		return MessageUtils.returnSuccess(spvs);
	}

	// 获取按日，月，年返回任务状态(个数跟大小)
	public Object findTaskHistoryResultNumAndSizeByHistoryTime(TimeType type, String label) throws ParseException {
		NodeResultVo nrv = null;
		switch (type) {
		case day:
			nrv = monitorTask.getTaskHistoryResultNumAndSizeByHistoryTime(type, label);
			break;
		case month:
			nrv = MonitorIndexCaches.monthNrvs.get(label);
			if(nrv == null){
				nrv = monitorTask.getTaskHistoryResultNumAndSizeByHistoryTime(type, label);
				MonitorIndexCaches.monthNrvs.put(label, nrv);
			}
			break;
		case year:
			nrv = MonitorIndexCaches.yearNrvs.get(label);
			if(nrv == null){
				nrv = monitorTask.getTaskHistoryResultNumAndSizeByHistoryTime(type, label);
				MonitorIndexCaches.yearNrvs.put(label, nrv);
			}
			break;
		case years:
			nrv = MonitorIndexCaches.yearsNrvs.get(label);
			if(nrv == null){
				nrv = monitorTask.getTaskHistoryResultNumAndSizeByHistoryTime(type, label);
				MonitorIndexCaches.yearsNrvs.put(label, nrv);
			}
			break;
			
		default:
			break;
		}
		
		return MessageUtils.returnSuccess(nrv);
	}

	// 按条件获取任务的结果数量与大小，用于实时的开始显示。
	public Object findTaskHistoryResultNumAndSize(int interval, int size, String label) {
		long milli = System.currentTimeMillis();
		long starttime = TimeUtils.divNotSure(milli, interval, size);
		long endtime = TimeUtils.divNotSure(milli, interval, 1);
		List<CollectionHistory> fullChs = new LinkedList<CollectionHistory>();
		List<CollectionHistory> chs = collectionMonitorDao.findTaskHistoryResultNumAndSize(interval, label, starttime,
				endtime);

		int tempJ = 0;
		for (long i = starttime; i <= endtime; i = i + 5 * 60 * 1000){
			CollectionHistory ch = null;
			for (int j = tempJ; j < chs.size();) {
				ch = chs.get(j);
				long itemTime = ch.getTime();
				if (itemTime == i) {
					fullChs.add(ch);
					tempJ = j + 1;
					break;
				} else {
					ch = null;
					if (itemTime < starttime || itemTime > endtime)
						tempJ = j = j + 1;
					else
						break;
				}
			}
			if(ch == null){
				ch = new CollectionHistory();
				ch.setTime(i);
				ch.setDataResultNum(0);
				ch.setDataResultSize(0L);
				fullChs.add(ch);
			}
		}

		NodeResultVo nrv = new NodeResultVo();
		nrv.setStarttime(starttime);
		nrv.setEndtime(endtime);
		nrv.setChs(fullChs);
		return MessageUtils.returnSuccess(nrv);
	}

	// 获取任务的进度信息
	public Object calcAllTaskSchedule(String label, int nodeSize) {
		List<TaskScheduleVo> taskScheduleVo = MonitorIndexCaches.taskSchedules.get(label);
		
		if (monitorTask.getNodeSize() != nodeSize
				|| (label != null && !label.equals(monitorTask.getLabel()))
				|| (label == null && monitorTask.getLabel() != null)) {
			monitorTask.setLabel(label);
			monitorTask.setNodeSize(nodeSize);
		}
		
		if(taskScheduleVo == null)
			taskScheduleVo = monitorTask.getTaskSchedules(label);
		return MessageUtils.returnSuccess(taskScheduleVo);
	}

	// 获取历史任务不同的服务,应用，任务,标签信息
	public Object findAllTaskHistoryClassifyInfos(Short type) {
		CollectionAllInfoVo civ = new CollectionAllInfoVo();
		List<CollectionServer> css = collectionMonitorDao.findCollectionServers(null, type);
		List<CollectionApplication> cas = collectionMonitorDao.findCompleteCollectionApplications(null, null, null);
		List<CollectionTask> cts = collectionMonitorDao.findCollectionTasks(null, null, null, null, null);
		List<String> labels = collectionMonitorDao.findAllDistinctTaskLabels(null);
		civ.setServers(css);
		civ.setDrivers(cas);
		civ.setTasks(cts);
		civ.setLabels(labels);
		return MessageUtils.returnSuccess(civ);
	}

	// 根据多条件, 分页获取历史任务信息
	public Object findApplicationHistoryPage(MultiConditionVo mcv) {
		if(mcv.getCurrentPage() == null || mcv.getPageSize() == null)
			return MessageUtils.parameterNullError();
		
		if (mcv.getStarttime() == null)
			mcv.setStarttime(0l);
		if (mcv.getEndtime() == null)
			mcv.setEndtime(System.currentTimeMillis());

		int currentPage = mcv.getCurrentPage();
		int pageSize = mcv.getPageSize();
		Page<CollectionHistory> chs = collectionMonitorDao.findTaskHistoryPageByMultiCondition(mcv);
		PageListInfo<CollectionHistory> pageInfo = new PageListInfo<CollectionHistory>();
		pageInfo.setCurrentPage(currentPage);
		pageInfo.setPerPageCount(pageSize);
		pageInfo.setTotalCount((int) chs.getTotal());
		pageInfo.setPageCount(chs.getPages());
		pageInfo.setObjs(chs);

		if (currentPage <= 0 || (currentPage > chs.getPages() && currentPage != 1))
			return MessageUtils.requestPageError();
		return MessageUtils.returnSuccess(pageInfo);
	}

	// 获取统计历史任务结果大小与数量（每天，每月，每年，今年）
	public Object calcTotalApplicationTasksHistoryResult(
			TimeType type, String label, String order,
			int size, Integer currentPage, Integer perPageSize)
			throws ParseException {
		List<CollectionHistory> cts = null;
		monitorTask.setTaskSize(size);
		String tempLabelName = StringUtils.join(label, "-", order, "-", size);
		switch (type) {
		case day:
			cts = monitorTask.getTotalTasksHistoryResult(type, label, order, size);
			break;
		case month:
			cts = MonitorIndexCaches.monthCts.get(tempLabelName);
			if(cts == null){
				cts = monitorTask.getTotalTasksHistoryResult(type, label, order, size);
				MonitorIndexCaches.monthCts.put(tempLabelName, cts);
			}
			break;
		case year:
			cts = MonitorIndexCaches.yearCts.get(tempLabelName);
			if(cts == null){
				cts = monitorTask.getTotalTasksHistoryResult(type, label, order, size);
				MonitorIndexCaches.yearCts.put(tempLabelName, cts);
			}
			break;
		case years:
			cts = MonitorIndexCaches.yearsCts.get(tempLabelName);
			if(cts == null){
				cts = monitorTask.getTotalTasksHistoryResult(type, label, order, size);
				MonitorIndexCaches.yearsCts.put(tempLabelName, cts);
			}
			break;
			
		default:
			break;
		}
		
		//分页
		try {
			CommonPageListInfo<CollectionHistory> commonPage = new CommonPageListInfo<>();
			return MessageUtils.returnSuccess(commonPage.page(cts, currentPage, perPageSize));
		} catch (PageErrorException e) {
			e.printStackTrace();
			return MessageUtils.pageOutOfRange();
		}
	}
	
	private void cudCollectionServer(List<CollectionServer> addCollectionServers, List<CollectionServer> updateCollectionServers, List<CollectionServer> removeCollectionServers){
		if (addCollectionServers != null && !addCollectionServers.isEmpty())
			collectionMonitorDao.addCollectionServerBatch(addCollectionServers);
		if (updateCollectionServers != null && !updateCollectionServers.isEmpty())
			collectionMonitorDao.updateCollectionServerBatch(updateCollectionServers);
		if (removeCollectionServers != null && !removeCollectionServers.isEmpty())
			collectionMonitorDao.removeCollectionServerBatch(removeCollectionServers);
	}
	
	private void cudCollectionServer(List<CollectionServer> collectionServers, Integer isUsedCs, Short type) {
		List<CollectionServer> removeCollectionServers = new LinkedList<CollectionServer>();
		List<CollectionServer> insertCollectionServers = new LinkedList<CollectionServer>();
		List<CollectionServer> updateCollectionServers = new LinkedList<CollectionServer>();
		List<CollectionServer> existCollectionServers = collectionMonitorDao.findCollectionServers(isUsedCs, type);
		removeCollectionServers.addAll(existCollectionServers);
		if (collectionServers == null || collectionServers.isEmpty())
			return;
		for (int i = 0; i < collectionServers.size(); i++) {
			CollectionServer collectionServer = collectionServers.get(i);
		
			if (existCollectionServers.isEmpty())
				insertCollectionServers.add(collectionServer);
			for (CollectionServer existCollectionServer : existCollectionServers) {
				if (existCollectionServer.getCsid().equals(collectionServer.getCsid())) {
					removeCollectionServers.remove(existCollectionServer);
					updateCollectionServers.add(collectionServer);
					break;
				}
				if (existCollectionServers.indexOf(existCollectionServer) == existCollectionServers.size() - 1)
					insertCollectionServers.add(collectionServer);
			}
		}
		if (!insertCollectionServers.isEmpty())
			collectionMonitorDao.addCollectionServerBatch(insertCollectionServers);
		if (!updateCollectionServers.isEmpty())
			collectionMonitorDao.updateCollectionServerBatch(updateCollectionServers);
		if (!removeCollectionServers.isEmpty())
			collectionMonitorDao.removeCollectionServerBatch(removeCollectionServers);
	}
	
	public void cudCollectionApplicationRunning(String csid, List<CollectionApplicationRunning> cars){
		List<CollectionApplicationRunning> insertCars = new LinkedList<CollectionApplicationRunning>();
		List<CollectionApplicationRunning> updateCars = new LinkedList<CollectionApplicationRunning>();
		List<CollectionApplicationRunning> existCars = collectionMonitorDao.selectCollectionApplicationRunnings(csid, null);
		if (cars == null || cars.isEmpty())
			return;
		for (int i = 0; i < cars.size(); i++) {
			CollectionApplicationRunning car = cars.get(i);
			if (existCars.isEmpty())
				insertCars.add(car);
			for (CollectionApplicationRunning existCar : existCars) {
				if (existCar.getCsid().equals(car.getCsid()) 
						&& existCar.getPid().equals(car.getPid())) {
					updateCars.add(car);
					break;
				}
				if (existCars.indexOf(existCar) == existCars.size() - 1)
					insertCars.add(car);
			}
		}
		if (!insertCars.isEmpty())
			collectionMonitorDao.addCollectionApplicationRunningBatch(insertCars);
		if (!updateCars.isEmpty())
			collectionMonitorDao.updateCollectionApplicationRunningBatch(updateCars);
	}

	public enum TimeType {
		day("day"), month("month"), year("year"), years("years");
		private TimeType(String type) {
			this.type = type;
		}

		private String type;

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

}
