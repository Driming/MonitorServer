package com.hc.util.map.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hc.util.StringUtils;

public class TaskModifyFlagMap {
	private static final long keepLived = 5*60*1000;
	private static final Map<String, Writable> taskConfigs = new ConcurrentHashMap<String, Writable>();
	private static final Map<String, Writable> taskStatuses = new ConcurrentHashMap<String, Writable>();
	private static final Map<String, Writable> taskCreates = new ConcurrentHashMap<String, Writable>();
	
	public static Map<String, Writable> addTaskConfig(String csid, String ctid, Writable value){
		String key = getKey(csid, ctid);
		return addTaskConfig(key, value);
	}
	
	public static Map<String, Writable> addTaskConfig(String key, Writable value){
		taskConfigs.put(key, value);
		return taskConfigs;
	}
	
	public static Writable getTaskConfig(String csid, String ctid){
		String key = getKey(csid, ctid);
		return taskConfigs.get(key);
	}
	
	public static Map<String, Writable> addTaskStatus(String csid, String ctid, Writable value){
		String key = getKey(csid, ctid);
		return addTaskStatus(key, value);
	}
	
	public static Map<String, Writable> addTaskStatus(String key, Writable value){
		taskStatuses.put(key, value);
		return taskStatuses;
	}
	
	public static Writable getTaskStatus(String csid, String ctid){
		String key = getKey(csid, ctid);
		return taskStatuses.get(key);
	}
	
	public static Map<String, Writable> addTaskCreate(String csid, Writable value){
		taskCreates.put(csid, value);
		return taskCreates;
	}
	
	public static Writable getTaskCreate(String csid){
		return taskCreates.get(csid);
	}
	
	public static String getKey(String csid, String ctid){
		return StringUtils.join(csid, "-", ctid);
	}
	
	public static class Writable{
		public boolean isWritabled = true;
		public long time = System.currentTimeMillis();
		
		public boolean isOvertime(){
			long current = System.currentTimeMillis();
			if(current - time > keepLived)
				return true;
			return false;
		}
	}
	
}
