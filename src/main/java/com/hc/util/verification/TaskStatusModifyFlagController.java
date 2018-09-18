package com.hc.util.verification;

import com.hc.util.map.cache.TaskModifyFlagMap;
import com.hc.util.verification.base.BaseTaskModifyFalg;

public class TaskStatusModifyFlagController implements BaseTaskModifyFalg{

	@Override
	public synchronized boolean isWritabled(String csid, String ctid){
		TaskModifyFlagMap.Writable writable = TaskModifyFlagMap.getTaskStatus(csid, ctid);
		if(writable == null){
			writable = new TaskModifyFlagMap.Writable();
			TaskModifyFlagMap.addTaskStatus(csid, ctid, writable);
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
		TaskModifyFlagMap.Writable writable = TaskModifyFlagMap.getTaskStatus(csid, ctid);
		if(writable == null){
			writable = new TaskModifyFlagMap.Writable();
			TaskModifyFlagMap.addTaskStatus(csid, ctid, writable);
		}else
			writable.isWritabled = true;
	}

}
