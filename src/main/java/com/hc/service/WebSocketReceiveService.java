package com.hc.service;

import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hc.dao.CollectionMonitorDao;
import com.hc.entity.collection.application.running.CollectionApplicationRunning;
import com.hc.entity.collection.history.CollectionHistory;
import com.hc.entity.collection.history.CollectionHistory.Error;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.entity.collection.task.CollectionTask;
import com.hc.entity.collection.task.record.CollectionTaskUpdateRecord;
import com.hc.entity.collection.task.status.CollectionTaskTimePointStatus;
import com.hc.entity.control.user.User;
import com.hc.util.StringUtils;
import com.hc.util.map.TaskCommandMap;
import com.hc.util.map.TaskMap;
import com.hc.util.verification.builder.TaskModifyFlagControllerBuilder;
import com.hc.util.websocket.feedback.WebSocketFeedbackFlag;
import com.hc.util.websocket.feedback.WebSocketFeedbackFlag.TaskModify;
import com.hc.vo.ProcessVo;
import com.hc.vo.RecordVo;
import com.hc.vo.RecordVo.Record;
import com.hc.vo.WsFeedbackVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

@Service
public class WebSocketReceiveService {

	@Autowired
	private CollectionMonitorDao collectionMonitorDao;

	@Autowired
	private WebSocketSendService webSocketSendService;

	@Autowired
	private CollectionMonitorService collectionMonitorService;

	public void receiveProcess(String message, Principal principal) {
		try {
			@SuppressWarnings("unchecked")
			List<ProcessVo> pvs = JSONArray.toList(JSONArray.fromObject(message), new ProcessVo(), new JsonConfig());
			List<CollectionApplicationRunning> cars = new LinkedList<CollectionApplicationRunning>();
			for (ProcessVo pv : pvs) {
				CollectionApplicationRunning car = new CollectionApplicationRunning();
				car.setCaName(pv.getDriverName());
				car.setCaVersion(pv.getDriverVersion());
				car.setCsid(principal.getName());
				car.setPid(pv.getPid());
				car.setUsedCpu(pv.getCpu());
				car.setUsedMemory(pv.getMemory());
				if (pv.getNetwork() != null) {
					car.setUpload((float) pv.getNetwork().getUp());
					car.setDownload((float) pv.getNetwork().getDown());
				}
				if (pv.getIo() != null) {
					car.setReadSpeed((float) pv.getIo().getRead());
					car.setWriteSpeed((float) pv.getIo().getWrite());
				}
				cars.add(car);
			}
			// 存入数据库
			collectionMonitorService.cudCollectionApplicationRunning(principal.getName(), cars);
			// 发送给前端
			webSocketSendService.taskProcess(cars);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public synchronized void receiveRecords(String message, Principal principal) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		List<RecordVo> rvs = mapper.readValue(message, new TypeReference<List<RecordVo>>() {});
		if (!rvs.isEmpty()) {
			for (int i = 0; i < rvs.size(); i++) {
				receiveRecord(rvs.get(i), principal);
			}
			// 继续请求获取记录
			Map<String, Long> serverMaxTimes = collectionMonitorDao.selectCollectionHistoryServerMaxTime();
			webSocketSendService.synchronizeTaskHistory(principal.getName(),
					Long.parseLong(serverMaxTimes.get(principal.getName()).toString()));
		}
	}

	private void receiveRecord(RecordVo recordVo, Principal principal) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			CollectionHistory ch = new CollectionHistory();
			ch.setCtime(recordVo.getCreatedTime());
			Record record = mapper.readValue(recordVo.getRecord(), Record.class);
			CollectionServer cs = collectionMonitorDao.findCollectionServerByCtid(record.getId(), principal.getName());
			if (cs != null) {
				ch.setCsid(cs.getCsid());
				ch.setCtid(cs.getCts().get(0).getCtid());
				ch.setCt(cs.getCts().get(0));
			}else
				return;
			ch.setStatus(record.getResult());
			ch.setTime(record.getTimeSave());
			ch.setTimeStart(record.getTimeStart());
			ch.setTimeEnd(record.getTimeEnd());
			ch.setDelay(record.getTimeDelay());
			ch.setRetry(record.getRetryCount());
			ch.setDataSourceNum(record.getSourceSum());
			ch.setDataSourceSize(record.getSourceSize());
			ch.setDataResultNum(record.getOutcomeSum());
			ch.setDataResultSize(record.getOutcomeSize());
			List<Error> errors = new LinkedList<Error>();
			Error error = new Error();
			byte[] exception = record.getException(); 
			if(exception != null)
				error.setException(Arrays.toString(exception));
			errors.add(error);
			ch.setErrors(errors);
	
			// 插入数据库
			collectionMonitorDao.upsertCollectionHistory(ch);

			// 更新Task数据表
			collectionMonitorDao.updateCollectionTaskByHistory(ch);
			// 向前端发送更改
			CollectionTask ct = collectionMonitorDao.findCollectionTaskByHistoryUpdate(ch.getCtid(), ch.getCsid());
			webSocketSendService.taskRecord(ct);
			/*// 向前端发送含有错误信息的任务
			webSocketSendService.taskErrorHistory(ch);*/
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receiveTasks(String message, Principal principal) {
		JSONArray statusArray = JSONArray.fromObject(message);
		for (int i = 0; i < statusArray.size(); i++)
			receiveTask(statusArray.getJSONObject(i), principal);
	}

	private void receiveTask(JSONObject statusJson, Principal principal) {
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("ctid", statusJson.get("id"));
			map.put("csid", principal.getName());
			map.put("status", statusJson.get("status"));
			map.put("statusReason", statusJson.get("reason"));
			statusJson.clear();
			statusJson = JSONObject.fromObject(map);
			// 存入数据库
			CollectionTask ct = new CollectionTask();
			ct.setCtid(statusJson.getString("ctid"));
			ct.setCsid(principal.getName());
			ct.setStatus(statusJson.getString("status"));
			ct.setStatusReason(statusJson.getString("statusReason"));
			collectionMonitorDao.updateCollectionTaskSelective(ct);
			// 发送给前端
			webSocketSendService.taskStatus(statusJson);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void receiveStatuses(String message, Principal principal) {
		JSONArray statusArray = JSONArray.fromObject(message);
		for (int i = 0; i < statusArray.size(); i++)
			receiveStatus(statusArray.getJSONObject(i), principal);
	}

	private void receiveStatus(JSONObject statusJson, Principal principal) {
		try{
			CollectionTaskTimePointStatus taskStatus = new CollectionTaskTimePointStatus();
			taskStatus.setCtid(statusJson.getString("id"));
			taskStatus.setStatus(statusJson.getString("status"));
			taskStatus.setTimesave(statusJson.getLong("timeSave"));
			taskStatus.setTimedelay(statusJson.getLong("timeDelay"));
			taskStatus.setTimecurrent(statusJson.getLong("timeCurrent"));
			collectionMonitorDao.upsertCollectionTaskTimePointStatus(taskStatus);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void receiveFeedback(String message) {
		WsFeedbackVo feedbackVo = (WsFeedbackVo) JSONObject.toBean(JSONObject.fromObject(message), WsFeedbackVo.class);
		Integer id = feedbackVo.getId();
		TaskModify tm = WebSocketFeedbackFlag.get(id);
		if (id == null || id < 0 || tm == null)
			return;
		else
			WebSocketFeedbackFlag.remove(id);

		CollectionTask ct = tm.getCt();
		User user = tm.getUser();
		switch (tm.getType()) {
		case TaskMap.CREATE_TASK_RECORD:
			ct.setCtid(feedbackVo.getCtid());
			webSocketSendService.taskHandleReply(tm, feedbackVo, user);
			TaskModifyFlagControllerBuilder.buildTaskCreateController().writeTrue(ct.getCsid());
			break;
		case TaskMap.UPDATE_TASK_STATUS_RECORD:
			ct.setStatus(TaskCommandMap.commandMap.get(ct.getStatus()));
			webSocketSendService.taskHandleReply(tm, feedbackVo, user);
			TaskModifyFlagControllerBuilder.buildTaskStatusController().writeTrue(ct.getCsid(), ct.getCtid());
			break;
		case TaskMap.UPDATE_TASK_CONFIG_RECORD:
			webSocketSendService.taskHandleReply(tm, feedbackVo, user);
			TaskModifyFlagControllerBuilder.buildTaskConfigController().writeTrue(ct.getCsid(), ct.getCtid());
			break;
		default:
			return;
		}

		Integer result = null;
		int loop = 3;
		do {
			if (feedbackVo.getCode() != 0)
				break;
			// 更新数据库
			if (tm.getType() == TaskMap.CREATE_TASK_RECORD)
				result = collectionMonitorDao.addCollectionTaskSelective(ct);
			else
				result = collectionMonitorDao.updateCollectionTaskSelective(ct);
			loop--;
		} while (result <= 0 && loop >= 0);

		loop = 3;
		do {
			CollectionTaskUpdateRecord record = new CollectionTaskUpdateRecord(id,
					JSONObject.fromObject(feedbackVo, StringUtils.jsonIgnoreNull()).toString());
			result = collectionMonitorDao.updateCollectionTaskUpdateRecordSelective(record);
			loop--;
		} while (result <= 0 && loop >= 0);
	}

}
