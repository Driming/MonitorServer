package com.hc.util.verification.builder;

import com.hc.util.verification.TaskConfigModifyFlagController;
import com.hc.util.verification.TaskCreateModifyFlagController;
import com.hc.util.verification.TaskStatusModifyFlagController;

public class TaskModifyFlagControllerBuilder {
	private static TaskConfigModifyFlagController taskConfigModifyFlagController= null;
	private static TaskStatusModifyFlagController taskStatusModifyFlagController = null;
	private static TaskCreateModifyFlagController taskCreateModifyFlagController = null;
	
	public static synchronized TaskConfigModifyFlagController buildTaskConfigController(){
		if(taskConfigModifyFlagController == null)
			taskConfigModifyFlagController = new TaskConfigModifyFlagController();
		return taskConfigModifyFlagController;
	}
	
	public static synchronized TaskStatusModifyFlagController buildTaskStatusController(){
		if(taskStatusModifyFlagController == null)
			taskStatusModifyFlagController = new TaskStatusModifyFlagController();
		return taskStatusModifyFlagController;
	}
	
	public static synchronized TaskCreateModifyFlagController buildTaskCreateController(){
		if(taskCreateModifyFlagController == null)
			taskCreateModifyFlagController = new TaskCreateModifyFlagController();
		return taskCreateModifyFlagController;
	}
}
