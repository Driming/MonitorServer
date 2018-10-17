package com.hc.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.hc.dao.CollectionMonitorDao;
import com.hc.entity.collection.history.CollectionHistory;
import com.hc.entity.collection.history.extra.TaskSchedule;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.entity.collection.task.CollectionTask;
import com.hc.service.CollectionMonitorService.TimeType;
import com.hc.service.WebSocketSendService;
import com.hc.util.StringUtils;
import com.hc.util.TimeUtils;
import com.hc.util.map.ServerMap;
import com.hc.util.map.cache.MonitorIndexCaches;
import com.hc.vo.NodeResultVo;
import com.hc.vo.TaskScheduleVo;

import net.sf.json.JSONObject;

@Component
public class MonitorTask {

	@Autowired
	private WebSocketSendService webSocketSendService;

	@Autowired
	private CollectionMonitorDao collectionMonitorDao;

	private String label;
	private int interval = 5;
	private int nodeSize = 300;
	private int taskSize = 50;
	private String[] orders = {"size", "num"};
	private TimeType[] timeTypes = { TimeType.month, TimeType.year, TimeType.years };

	public void setLabel(String label) {
		this.label = label;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public void setNodeSize(int nodeSize) {
		this.nodeSize = nodeSize;
	}

	public void setTaskSize(int taskSize) {
		this.taskSize = taskSize;
	}

	public String getLabel() {
		return label;
	}

	public int getNodeSize() {
		return nodeSize;
	}

	// 间隔5分钟执行
	@Scheduled(cron = "0 */5 * * * ? ")
	public void sendTaskHistoryResultNumAndSize() {
		long milli = System.currentTimeMillis();
		long time = TimeUtils.divNotSure(milli, 5, 1);
		CollectionHistory ch = null;
		List<CollectionHistory> chs = collectionMonitorDao.findTaskHistoryResultNumAndSize(interval, label, time, time);
		if (!chs.isEmpty())
			ch = chs.get(0);
		else {
			ch = new CollectionHistory();
			ch.setTime(time);
			ch.setDataResultNum(0);
			ch.setDataResultSize(0L);
		}
		webSocketSendService.taskRecordResult(ch);
	}

	// 间隔30分钟执行
	@Scheduled(cron = "0 */30 * * * ? ")
	public void sendTaskSchedule() {
		int isUsedCt = 1;
		List<String> labels = collectionMonitorDao.findAllDistinctTaskLabels(isUsedCt);
		labels.add(null);
		for (String labelName : labels)
			getTaskSchedules(labelName);

		webSocketSendService.taskSchedule(MonitorIndexCaches.taskSchedules.get(label));
	}

	// 间隔6小时缓存统计数据
	@Scheduled(cron = "0 0 */6 * * ? ")
	public void cacheTaskHistoryResultNumAndSizeByHistoryTime() {
		int isUsedCt = 1;
		List<String> labels = collectionMonitorDao.findAllDistinctTaskLabels(isUsedCt);
		labels.add(null);
		NodeResultVo nrv = null;
		for (String labelName : labels) {
			for (TimeType timeType : timeTypes)
				try {
					nrv = getTaskHistoryResultNumAndSizeByHistoryTime(timeType, labelName);
					switch (timeType) {
					case month:
						MonitorIndexCaches.monthNrvs.put(labelName, nrv);
						break;

					case year:
						MonitorIndexCaches.yearNrvs.put(labelName, nrv);
						break;

					case years:
						MonitorIndexCaches.yearsNrvs.put(labelName, nrv);
						break;

					default:
						break;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
		}
	}
	
	// 间隔6小时缓存统计数据
	@Scheduled(cron = "0 0 */6 * * ? ")
	public void cacheTotalTasksHistoryResult() {
		int isUsedCt = 1;
		List<String> labels = collectionMonitorDao.findAllDistinctTaskLabels(isUsedCt);
		labels.add(null);
		String tempLabelName = null;
		List<CollectionHistory> cts = null;
		for (String labelName : labels) {
			for (TimeType timeType : timeTypes)
				for(String order : orders)
					try {
						tempLabelName = StringUtils.join(labelName, "-", order, "-", taskSize);
						cts = getTotalTasksHistoryResult(timeType, labelName, order, taskSize);
						
						switch (timeType) {
						case month:
							MonitorIndexCaches.monthCts.put(tempLabelName, cts);
							break;
	
						case year:
							MonitorIndexCaches.yearCts.put(tempLabelName, cts);
							break;
	
						case years:
							MonitorIndexCaches.yearsCts.put(tempLabelName, cts);
							break;
	
						default:
							break;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
		}
	}

	// 同步任务历史数据
	@Scheduled(cron = "*/5 * * * * ? ")
	public void synchronizeTaskHistory() {
		int isUsedCs = 1;
		long starttime = 0;
		short type = ServerMap.COLLECT_SERVER;
		Map<String, Long> serverMaxTimes = collectionMonitorDao.selectCollectionHistoryServerMaxTime();
		List<CollectionServer> collectionServers = collectionMonitorDao.findCollectionServers(isUsedCs, type);
		for (CollectionServer collectionServer : collectionServers) {
			String csid = collectionServer.getCsid();
			Object time = serverMaxTimes.get(csid);
			if (time == null)
				webSocketSendService.synchronizeTaskHistory(csid, starttime);
			else
				webSocketSendService.synchronizeTaskHistory(csid, Long.parseLong(time.toString()));

		}
	}

	public List<TaskScheduleVo> getTaskSchedules(String label) {
		List<TaskScheduleVo> tsvs = MonitorIndexCaches.taskSchedules.get(label);
		if (tsvs == null) {
			tsvs = new ArrayList<TaskScheduleVo>();
			MonitorIndexCaches.taskSchedules.put(label, tsvs);
		} else
			tsvs.clear();

		int isUsedCt = 1;
		List<CollectionTask> cts = collectionMonitorDao.findCollectionTasks(null, label, null, null, isUsedCt);
		for (CollectionTask ct : cts) {
			String config = ct.getConfig();
			TaskScheduleVo tsv = new TaskScheduleVo();
			JSONObject configJson = JSONObject.fromObject(config);
			if (!configJson.containsKey("end")) {
				tsv.setStart(TimeUtils.todayZeroHour());
				tsv.setEnd(TimeUtils.tomorrowZeroHour());
			} else {
				tsv.setStart(configJson.getLong("start"));
				tsv.setEnd(configJson.getLong("end"));
			}
			tsv.setLastExecuteTime(ct.getLastExecuteTime());
			tsv.setCtid(ct.getCtid());
			tsv.setCsid(ct.getCsid());
			tsv.setName(ct.getName());
			tsv.setStatus(ct.getStatus());

			List<TaskSchedule> ts = collectionMonitorDao.findTaskHistoryInTaskSchedule(label, null, tsv, nodeSize);
			if (ts.isEmpty())
				continue;

			List<Integer> nodeResult2 = null;
			List<List<Integer>> nodeResult1 = new LinkedList<List<Integer>>();
			int index = 0;
			int preResult = -1;
			if (!ts.isEmpty())
				for (int i = 0; i < ts.get(ts.size() - 1).getNode(); i++)
					for (int j = index; j < ts.size();) {
						if (ts.get(j).getNode() - 1 == i) {
							if (preResult != ts.get(j).getNodeResult()) {
								if (nodeResult2 != null)
									nodeResult1.add(nodeResult2);
								nodeResult2 = new LinkedList<Integer>();
								preResult = ts.get(j).getNodeResult();
							}
							nodeResult2.add(ts.get(j).getNodeResult());
							index += 1;
							break;
						}
						if (preResult != 0) {
							if (nodeResult2 != null)
								nodeResult1.add(nodeResult2);
							nodeResult2 = new LinkedList<Integer>();
							preResult = 0;
						}
						nodeResult2.add(0);
						break;
					}
			if (nodeResult2 != null && !nodeResult2.isEmpty())
				nodeResult1.add(nodeResult2);
			tsv.setNodeResults(nodeResult1);
			tsvs.add(tsv);
		}

		Collections.sort(tsvs, new TaskScheduleComparator());

		return tsvs;
	}

	class TaskScheduleComparator implements Comparator<TaskScheduleVo> {
		@Override
		public int compare(TaskScheduleVo tsvo1, TaskScheduleVo tsvo2) {
			int size1 = 0;
			List<List<Integer>> nodeResults1 = tsvo1.getNodeResults();
			for (List<Integer> list : nodeResults1)
				size1 += list.size();

			int size2 = 0;
			List<List<Integer>> nodeResults2 = tsvo2.getNodeResults();
			for (List<Integer> list : nodeResults2)
				size2 += list.size();

			if (size1 > size2)
				return -1;
			else if (size1 < size2)
				return 1;
			else
				return 0;
		}
	}

	public NodeResultVo getTaskHistoryResultNumAndSizeByHistoryTime(TimeType type, String label) throws ParseException {
		long milli = System.currentTimeMillis();
		List<CollectionHistory> fullChs = new LinkedList<CollectionHistory>();
		List<CollectionHistory> chs = collectionMonitorDao.findTaskHistoryResultNumAndSizeByHistoryTime(type, milli,
				label);

		long endtime = 0;
		long starttime = 0;
		int intervalMilli = 0;
		switch (type) {
		case day:
			intervalMilli = 60 * 60 * 1000;
			starttime = TimeUtils.getStandardMilli(milli - (long) 23 * 60 * 60 * 1000, 60);
			endtime = TimeUtils.getStandardMilli(milli, 60);
			break;
		case month:
			intervalMilli = 24 * 60 * 60 * 1000;
			starttime = TimeUtils.getStandardMilli(milli - (long) 29 * 24 * 60 * 60 * 1000, 24 * 60);
			endtime = TimeUtils.getStandardMilli(milli, 24 * 60);
			break;
		case year:
			starttime = TimeUtils.getStartMonth(milli);
			endtime = TimeUtils.getMonthMilli(milli);
			break;
		case years:
			if (!chs.isEmpty()) {
				starttime = TimeUtils.getYearMilli(chs.get(0).getTime());
				endtime = TimeUtils.getYearMilli(chs.get(chs.size() - 1).getTime());
			}
		}

		int tempJ = 0;
		for (long i = starttime; i <= endtime; i = type.equals(TimeType.day) || type.equals(TimeType.month)
				? i + intervalMilli : type.equals(TimeType.year) ? TimeUtils.addOneMonth(i) : TimeUtils.addOneYear(i)) {
			CollectionHistory ch = null;
			for (int j = tempJ; j < chs.size();) {
				ch = chs.get(j);
				long itemTime = type.equals(TimeType.day) || type.equals(TimeType.month)
						? TimeUtils.getStandardMilli(ch.getTime(), intervalMilli / (60 * 1000))
						: type.equals(TimeType.year) ? TimeUtils.getMonthMilli(ch.getTime())
								: TimeUtils.getYearMilli(ch.getTime());
				if (itemTime == i) {
					ch.setTime(itemTime);
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
			if (ch == null) {
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
		return nrv;
	}

	public List<CollectionHistory> getTotalTasksHistoryResult(TimeType type, String label, String order, int size)
			throws ParseException {
		if (order == null)
			order = "size";

		long milli = System.currentTimeMillis();
		long starttime = 0;
		switch (type) {
		case day:
			starttime = TimeUtils.todayZeroHour();
			break;
		case month:
			starttime = TimeUtils.getMonthMilli(milli);
			break;
		case year:
			starttime = TimeUtils.getYearMilli(milli);
			break;
		case years:
		}
		List<CollectionHistory> cts = collectionMonitorDao.calcTotalApplicationTasksHistoryResult(type, label,
				starttime, order, size);
		return cts;
	}
}
