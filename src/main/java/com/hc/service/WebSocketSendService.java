package com.hc.service;

import java.util.List;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.hc.entity.collection.application.running.CollectionApplicationRunning;
import com.hc.entity.collection.history.CollectionHistory;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.entity.collection.task.CollectionTask;
import com.hc.entity.control.user.User;
import com.hc.entity.index.error.ErrorInfoRecord;
import com.hc.util.StringUtils;
import com.hc.util.websocket.feedback.WebSocketFeedbackFlag.TaskModify;
import com.hc.vo.IndexInfoVo;
import com.hc.vo.JarInfoVo;
import com.hc.vo.TaskScheduleVo;
import com.hc.vo.WsFeedbackVo;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class WebSocketSendService {

	private SimpMessagingTemplate template;

	public void setTemplate(SimpMessagingTemplate template) {
		this.template = template;
	}

	/********* 发送给采集监控客户端 **********/
	// 发送任务状态更改
	public void updateTaskStatus(Integer eid, CollectionTask ct) {
		updateTaskStatus(ct.getCsid(), ct.getCtid(), eid, ct.getStatus());
	}

	public void updateTaskStatus(String csid, String ctid, Integer eid, String status) {
		JSONObject commandJson = new JSONObject();
		commandJson.put("id", ctid);
		commandJson.put("eid", eid);
		commandJson.put("command", status);
		template.convertAndSend("/user/" + csid + "/control_task", commandJson.toString());
	}

	// 发送任务配置更改
	public void updateTaskConfigure(Integer eid, CollectionTask ct) {
		updateTaskConfigure(ct.getCsid(), ct.getCtid(), eid, ct.getConfig());
	}

	public void updateTaskConfigure(String csid, String ctid, Integer eid, String config) {
		JSONObject scheduleJson = new JSONObject();
		scheduleJson.put("id", ctid);
		scheduleJson.put("eid", eid);
		scheduleJson.put("config", config);
		template.convertAndSend("/user/" + csid + "/set_task", scheduleJson.toString());
	}

	// 发送驱动更改信息
	public void updateDriver(JarInfoVo jarInfo, CollectionServer server) {
		updateDriver(server.getCsid(), jarInfo.getName(), jarInfo.getVersion());
	}

	public void updateDriver(String csid, String name, String version) {
		JSONObject driverJson = new JSONObject();
		driverJson.put("name", name);
		driverJson.put("version", version);
		template.convertAndSend("/user/" + csid + "/update_drive", driverJson.toString());
	}
	
	//发送同步历史任务时间
	public void synchronizeTaskHistory(String csid, Long time){
		JSONObject synchronizeJson = new JSONObject();
		synchronizeJson.put("time", time);
		template.convertAndSend("/user/" + csid + "/query_record", synchronizeJson.toString());
	}
	
	//发送控制系统信息
	public void controlCollectDispatchSystem(String csid, String command) {
		JSONObject controlSystemJson = new JSONObject();
		controlSystemJson.put("command", command);
		template.convertAndSend("/user/" + csid + "/control_system", controlSystemJson.toString());
	}

	/********** 发送给网页前端 ***************/
	// 发送任务运行状态
	public void taskProcess(List<CollectionApplicationRunning> cars) {
		JSONArray processJson = JSONArray.fromObject(cars, StringUtils.jsonIgnoreNull());
		taskProcessArray(processJson);
	}

	public void taskProcess(String process) {
		template.convertAndSend("/client/process", process);
	}

	public void taskProcessArray(JSONArray processJson) {
		template.convertAndSend("/client/process", processJson);
	}

	// 发送任务结果汇总
	public void taskRecord(CollectionTask ct) {
		JSONObject recordJson = JSONObject.fromObject(ct, StringUtils.jsonIgnoreNull());
		taskRecord(recordJson);
	}

	public void taskRecord(String record) {
		template.convertAndSend("/client/record", record);
	}

	public void taskRecord(JSONObject recordJson) {
		template.convertAndSend("/client/record", recordJson);
	}

	// 发送错误任务结果
	public void taskErrorHistory(CollectionHistory ch) {
		JSONObject errorJson = JSONObject.fromObject(ch, StringUtils.jsonIgnoreNull());
		taskErrorHistory(errorJson);
	}

	public void taskErrorHistory(String error) {
		template.convertAndSend("/client/error", error);
	}

	public void taskErrorHistory(JSONObject errorJson) {
		template.convertAndSend("/client/error", errorJson);
	}

	// 发送任务状态更改
	public void taskStatus(String status) {
		template.convertAndSend("/client/status", status);
	}

	public void taskStatus(JSONObject statusJson) {
		template.convertAndSend("/client/status", statusJson);
	}

	// 发送实时任务结果
	public void taskRecordResult(CollectionHistory ch) {
		JSONObject resultJson = JSONObject.fromObject(ch, StringUtils.jsonIgnoreNull());
		taskRecordResult(resultJson);
	}

	public void taskRecordResult(String result) {
		template.convertAndSend("/client/record/task", result);
	}

	public void taskRecordResult(JSONObject resultJson) {
		template.convertAndSend("/client/record/task", resultJson);
	}

	// 发送任务进度信息
	public void taskSchedule(List<TaskScheduleVo> tsvs) {
		JSONArray scheduleJson = JSONArray.fromObject(tsvs);
		taskScheduleArray(scheduleJson);
	}

	public void taskSchedule(String schedule) {
		template.convertAndSend("/client/task/schedule", schedule);
	}

	public void taskScheduleArray(JSONArray scheduleJson) {
		template.convertAndSend("/client/task/schedule", scheduleJson);
	}
	
	//发送操作任务结果
	public void taskHandleReply(TaskModify tm, WsFeedbackVo wf, User user){
		taskHandleReply(wf.getId(), tm.getType(), tm.getCt().getCtid(),
				tm.getCt().getCsid(), wf.getCode(), wf.getMsg(),
				user.getUid());
	}
	
	public void taskHandleReply(Integer id,
			Integer type, String ctid, String csid,
			Integer code, String message, Integer uid){
		JSONObject replyJson = new JSONObject();
		replyJson.put("id", id);
		replyJson.put("type", type);
		replyJson.put("ctid", ctid);
		replyJson.put("csid", csid);
		replyJson.put("statusCode", code);
		replyJson.put("message", message);
		template.convertAndSend("/user/"+ uid +"/task/reply", replyJson);
	}
	
	//发送首页服务器信息
	public void indexServerInfo(String serverInfo){
		template.convertAndSend("/client/index/server/infos", serverInfo);
	}
	
	public void indexServerInfo(IndexInfoVo indexInfoVo){
		JSONObject serverInfoJson= JSONObject.fromObject(indexInfoVo, StringUtils.jsonIgnoreNull());
		template.convertAndSend("/client/index/server/infos", serverInfoJson);
	}
	
	//发送错误信息
	public void indexErrorInfo(String errorInfo){
		template.convertAndSend("/client/index/error/infos", errorInfo);
	}
	
	public void indexErrorInfo(List<ErrorInfoRecord> records){
		JSONArray serverInfoJson= JSONArray.fromObject(records,  StringUtils.jsonIgnoreNull());
		template.convertAndSend("/client/index/error/infos", serverInfoJson);
	}

}
