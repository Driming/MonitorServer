package com.hc.util.map;

public class TaskMap {
	public static final int CREATE_TASK = 1;
	public static final int UPDATE_TASK_CONGIF = 2;
	public static final int CONTINUE_PAUSE_TASK = 4;
	public static final int RESTART_STOP_TASK = 8;
	public static final int CONTROL_SYSTEM = 16;
	
	public static final String STATUS_PAUSE = "pause";
	public static final String STATUS_CONTINUE = "continue";
	public static final String STATUS_RESTART = "restart";
	public static final String STATUS_STOP = "stop";
	
	public static final int CREATE_TASK_RECORD = 1;
	public static final int UPDATE_TASK_STATUS_RECORD = 2;
	public static final int UPDATE_TASK_CONFIG_RECORD = 3;
}
