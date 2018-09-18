package com.hc.util.map;

import java.util.HashMap;
import java.util.Map;

public class TaskCommandMap {
	public static final Map<String, String> commandMap = new HashMap<String, String>();
	
	static{
		commandMap.put("stop", "stopped");
		commandMap.put("start", "running");
		commandMap.put("pause", "stopped");
		commandMap.put("resume", "running");
	}
}
