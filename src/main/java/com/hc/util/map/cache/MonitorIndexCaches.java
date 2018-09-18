package com.hc.util.map.cache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hc.entity.collection.history.CollectionHistory;
import com.hc.vo.NodeResultVo;
import com.hc.vo.TaskScheduleVo;

public class MonitorIndexCaches {
	
	public static Map<String, List<TaskScheduleVo>> taskSchedules = new HashMap<String, List<TaskScheduleVo>>();

	public static Map<String, NodeResultVo> monthNrvs = new HashMap<String, NodeResultVo>();
	public static Map<String, NodeResultVo> yearNrvs = new HashMap<String, NodeResultVo>();
	public static Map<String, NodeResultVo> yearsNrvs = new HashMap<String, NodeResultVo>();

	public static Map<String, List<CollectionHistory>> monthCts = new HashMap<String, List<CollectionHistory>>();
	public static Map<String, List<CollectionHistory>> yearCts = new HashMap<String, List<CollectionHistory>>();
	public static Map<String, List<CollectionHistory>> yearsCts = new HashMap<String, List<CollectionHistory>>();

}
