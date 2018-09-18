package com.hc.util.verification;

import com.hc.util.map.cache.TaskModifyFlagMap;
import com.hc.util.verification.base.BaseTaskModifyFalg;

public class TaskConfigModifyFlagController implements BaseTaskModifyFalg{
	
	@Override
	public synchronized boolean isWritabled(String csid, String ctid){
		TaskModifyFlagMap.Writable writable = TaskModifyFlagMap.getTaskConfig(csid, ctid);
		if(writable == null){
			writable = new TaskModifyFlagMap.Writable();
			TaskModifyFlagMap.addTaskConfig(csid, ctid, writable);
		}
		synchronized (writable) {
			if(writable.isWritabled){
				writable.isWritabled = false;
				writable.time = System.currentTimeMillis();
				return true;
			}else
				if(writable.isOvertime()){
					writable.time = System.currentTimeMillis();
					return true;
				}
		}
		return false;
	}
	
	@Override
	public void writeTrue(String csid, String ctid) {
		TaskModifyFlagMap.Writable writable = TaskModifyFlagMap.getTaskConfig(csid, ctid);
		if(writable == null){
			writable = new TaskModifyFlagMap.Writable();
			TaskModifyFlagMap.addTaskConfig(csid, ctid, writable);
		}else
			writable.isWritabled = true;
	}
}
