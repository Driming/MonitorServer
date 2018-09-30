package com.hc.controller;

import java.text.ParseException;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hc.dao.CollectionMonitorDao;
import com.hc.entity.collection.application.CollectionApplication;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.entity.collection.task.CollectionTask;
import com.hc.entity.control.user.User;
import com.hc.service.CollectionMonitorService;
import com.hc.service.CollectionMonitorService.TimeType;
import com.hc.task.MonitorTask;
import com.hc.util.MessageUtils;
import com.hc.util.StringUtils;
import com.hc.util.map.ServerMap;
import com.hc.vo.MultiConditionVo;

@Controller
@RequestMapping("/collect/monitor")
public class CollectionMonitorController {
	@Autowired
	private CollectionMonitorDao collectionMonitorDao;
	
	@Autowired
	private CollectionMonitorService collectionMonitorService;
	
	@Autowired
	private MonitorTask commTask;
	
	@PostConstruct
	public void initCommTask(){
		collectionMonitorService.setCommTask(commTask);
	}
	
	
	/************  服务器    *************/
	//获取所有采集服务信息
	@ResponseBody
	@RequestMapping("/all/infos")
	public Object getAllCollectionInfo(){
		int isUsedCs = 1;
		int isUsedCa = 1;
		int isUsedCt = 1;
		short type = ServerMap.COLLECT_SERVER;
		return collectionMonitorService.findAllCollectionInfo(isUsedCs, isUsedCa, isUsedCt, type);
	}
	
	//注册采集服务/应用/任务信息
	@ResponseBody
	@RequestMapping("/server/register")
	public Object registerCollectionServer(String message, HttpServletRequest request){
		String ip = StringUtils.getIp(request);
		int port = request.getRemotePort();
		return collectionMonitorService.addCollectionServer(message, ip, port);
	}
	
	//获取采集服务器信息
	@ResponseBody
	@RequestMapping("/server/infos")
	public Object getCollectionServers() {
		int isUsedCs = 1;
		short type = ServerMap.COLLECT_SERVER;
		return MessageUtils.returnSuccess(collectionMonitorDao.findCollectionServers(isUsedCs, type));
	}
	
	//更新服务器信息
	@ResponseBody
	@RequestMapping("/server/update")
	public Object updateCollectionServer(CollectionServer cs){
		return collectionMonitorService.updateCollectionServer(cs);
	}
	
	//删除服务器
	@ResponseBody
	@RequestMapping("/server/delete")
	public Object deleteCollectionServer(String csid){
		return collectionMonitorService.deleteCollectionServer(csid);
	}
	
	
	/************  应用   *************/
	//更改应用信息
	@ResponseBody
	@RequestMapping("/application/update")
	public Object updateCollectionApplication(CollectionApplication ca){
		return collectionMonitorService.updateCollectionApplication(ca);
	}
	
	//获取所有驱动信息
	@ResponseBody
	@RequestMapping("/application/infos")
	public Object getCollectionApplications(){
		int isUsedCa = 1;
		return MessageUtils.returnSuccess(collectionMonitorDao.findCollectionApplications(isUsedCa));
	}
	
	//获取所有有任务运行的应用信息
	@ResponseBody
	@RequestMapping("/application/run/infos")
	public Object getRunningCollectionApplications(){
		int isUsedCa = 1;
		int isUsedCt = 1;
		return MessageUtils.returnSuccess(collectionMonitorDao.findCompleteCollectionApplications(
				null, isUsedCa, isUsedCt));
	}
	
	//获取所有采集服务的任务信息
	@ResponseBody
	@RequestMapping("/application/tasks")
	public Object getApplicationTasks(String csid){
		int isUsedCa = 1;
		int isUsedCt = 1;
		return MessageUtils.returnSuccess(collectionMonitorDao.findApplicationTasks(
				csid, isUsedCa, isUsedCt));
	}
	
	
	//多条件获取任务信息
	@ResponseBody
	@RequestMapping("/application/multi/condition/tasks")
	public Object getApplicationTasks(@RequestBody MultiConditionVo mcv){
		int isUsedCar = 1;
		int isUsedCt = 1;
		return MessageUtils.returnSuccess(collectionMonitorService.findApplicationRunningsByMultiCondition(
				mcv, isUsedCar, isUsedCt));
	}
	
	/************ 系统 ***************/
	@ResponseBody
	@RequestMapping("/system/control")
	public Object controlCollectDispatchSystem(
			@RequestParam(required = false)String csid,
			@RequestParam(required = true)String command,
			HttpSession session){
		User user = (User) session.getAttribute("webUser");
		return collectionMonitorService.controlCollectDispatchSystem(csid, command, user);
	}
	
	
	/************  任务   *************/
	//根据标签或者应用获取任务信息
	@ResponseBody
	@RequestMapping("/application/label/tasks")
	public Object getCollectionTasks(String label, String driverName, String driverVersion){
		int isUsedCt = 1;
		return MessageUtils.returnSuccess(collectionMonitorDao.findCollectionTasks(null, label, driverName, driverVersion, isUsedCt));
	}
	
	//控制任务状态
	@ResponseBody
	@RequestMapping("/application/task/status/update")
	public Object controllTaskStatus(CollectionTask ct, HttpSession session){
		User user = (User) session.getAttribute("webUser");
		return collectionMonitorService.updateTaskStatus(ct, user);
	}
	
	//创建任务
	@ResponseBody
	@RequestMapping("/application/task/add")
	public Object addCollectionTask(CollectionTask ct, HttpSession session){
		User user = (User) session.getAttribute("webUser");
		return collectionMonitorService.addCollectionTask(ct, user);
	}
	
	//更改采集任务配置
	@ResponseBody
	@RequestMapping("/application/task/config/update")
	public Object updateTaskConfigure(@RequestBody CollectionTask ct, HttpSession session){
		User user = (User) session.getAttribute("webUser");
		return collectionMonitorService.updateTaskConfigure(ct, user);
	}
	
	//更改采集任务标签信息
	@ResponseBody
	@RequestMapping("/application/task/label/update")
	public Object updateTaskLabel(CollectionTask ct) {
		return collectionMonitorService.updateTaskLabel(ct);
	}
	
	//获取任务的所有不同标签
	@ResponseBody
	@RequestMapping("/application/task/labels")
	public Object getAllDistinctTaskLabels(){
		int isUsed = 1;
		return MessageUtils.returnSuccess(collectionMonitorDao.findAllDistinctTaskLabels(isUsed));
	}
	
	//获取所有未设标签值的任务
	@ResponseBody
	@RequestMapping("/application/notLabel/tasks")
	public Object getAllNotLabelTasks(){
		int isUsed = 1;
		return MessageUtils.returnSuccess(collectionMonitorDao.findAllNotLabelTasks(isUsed));
	}
	
	//获取所有任务进度信息
	@ResponseBody
	@RequestMapping("/application/tasks/schedule")
	public Object getAllTaskSchedule(String label, int nodeSize){
		return collectionMonitorService.calcAllTaskSchedule(label, nodeSize);
	}
	
	//查看任务操作返回情况（创建任务，更新任务配置，更新任务状态）
	@ResponseBody
	@RequestMapping("/application/task/update/record")
	public Object getCollectionTaskUpdateRecord(Integer id){
		return collectionMonitorService.findCollectionTaskUpdateRecord(id);
	}
	
	
	/************  任务历史   *************/
	@ResponseBody
	@RequestMapping("/task/history/all/classify/infos")
	public Object getAllTaskHistoryClassifyInfos(){
		short type = ServerMap.COLLECT_SERVER;
		return collectionMonitorService.findAllTaskHistoryClassifyInfos(type);
	}
	
	@ResponseBody
	@RequestMapping("/application/task/history/distinct/servers")
	public Object getTaskHistoryDistinctServers(){
		short type = ServerMap.COLLECT_SERVER;
		return MessageUtils.returnSuccess(collectionMonitorDao.findCollectionServers(null, type));
	}
	
	@ResponseBody
	@RequestMapping("/task/multi/condition/history/page")
	public Object getApplicationHistoryPage(@RequestBody MultiConditionVo mcv){
		return collectionMonitorService.findApplicationHistoryPage(mcv);
	}
	
	//获取任务的历史信息
	@ResponseBody
	@RequestMapping("/application/tasks/history")
	public Object getCollectionHistorys(Boolean hasError, String csid, String ctid){
		return MessageUtils.returnSuccess(collectionMonitorDao.findCollectionHistorys(ctid, csid, hasError));
	}
	
	//根据任务,分页获取任务历史数据
	@Deprecated
	@ResponseBody
	@RequestMapping("/application/tasks/history/page")
	public Object getAppliationTaskHistorysPage(String ctid, String csid, int currentPage,
			@RequestParam(defaultValue = "1000")Integer pageSize,
			@RequestParam(defaultValue = "10")Integer totalPage){
		return collectionMonitorService.findApplicationTaskHistorysPage(ctid, csid, currentPage, pageSize,
				totalPage);
	}
	
	//根据任务,获取分页的任务历史数据概要信息
	@Deprecated
	@ResponseBody
	@RequestMapping("/application/tasks/history/page/info")
	public Object getAppliationTaskHistorysPageInfo(
			String ctid, String csid,
			@RequestParam(defaultValue = "1000")Integer pageSize,
			@RequestParam(defaultValue = "10")Integer totalPage,
			@RequestParam(defaultValue = "1")Integer currentPage,
			@RequestParam(defaultValue = "10")Integer perPageSize){
		return collectionMonitorService.findApplicationTaskHistorysPageInfo(
				ctid, csid, pageSize, totalPage, currentPage, perPageSize);
	}
	
	@ResponseBody
	@RequestMapping("/application/tasks/history/day")
	public Object getApplicationTaskHistoryByDay(
			String ctid, String csid, String time){
		return collectionMonitorService.findApplicationTaskHistoryByDay(ctid, csid, time);
	}
	
	@ResponseBody
	@RequestMapping("/application/tasks/history/day/info")
	public Object getApplicationTaskHistorysDayInfo(
			String ctid, String csid, long time,
			@RequestParam(defaultValue = "10")Integer totalPage){
		return collectionMonitorService.findApplicationTaskHistorysDayInfo(
				ctid, csid, time, totalPage);
	}
	
	//获取24小时历史任务各状态百分比
	@ResponseBody
	@RequestMapping("/task/history/status/percent")
	public Object getTaskHistoryStatusPercent(String label){
		long milli = System.currentTimeMillis();
		return MessageUtils.returnSuccess(collectionMonitorDao.findTaskHistoryStatusPercent(milli, label));
	}
	
	//获取统计历史任务结果大小与数量（每天，每月，每年，今年）
	@ResponseBody
	@RequestMapping("/application/ntasks/history/total/result")
	public Object getTotalApplicationTasksHistoryResult(
			TimeType type, String label, String order, int size,
			@RequestParam(defaultValue = "1")Integer currentPage,
			@RequestParam(defaultValue = "10")Integer perPageSize) throws ParseException{
		return collectionMonitorService.calcTotalApplicationTasksHistoryResult(
				type, label, order, size, currentPage, perPageSize);
	}
	
	//获取一段时间的任务记录各状态百分比（间隔小时、日、月、年）
	@ResponseBody
	@RequestMapping("/application/tasks/history/time/nodes")
	public Object getTaskHistoryStatusPercentByTimeNodes(TimeType type, String label){
		return collectionMonitorService.findTaskHistoryStatusPercentByTimeNodes(type, label);
	}
	
	//获取一段时间的任务记录统计结果（间隔小时、日、月、年）
	@ResponseBody
	@RequestMapping("/application/tasks/history/history/result")
	public Object getTaskHistoryResultNumAndSizeByHistoryTime(TimeType type, String label) throws ParseException{
		return collectionMonitorService.findTaskHistoryResultNumAndSizeByHistoryTime(type, label);
	}
	
	//按条件获取任务的结果数量与大小，用于实时的开始显示。
	@ResponseBody
	@RequestMapping("/application/tasks/history/continue/start")
	public Object getTaskHistoryResultNumAndSize(int interval, int size, String label){
		commTask.setInterval(interval);
		commTask.setLabel(label);
		return collectionMonitorService.findTaskHistoryResultNumAndSize(interval, size, label);
	}
	
	@ResponseBody
	@RequestMapping("/test")
	public Object getTest(String ctid, String csid){
		return null;
	}

}
