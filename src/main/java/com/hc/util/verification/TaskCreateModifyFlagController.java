package com.hc.util.verification;

import com.hc.util.map.cache.TaskModifyFlagMap;
import com.hc.util.verification.base.BaseTaskModifyFalg;

public class TaskCreateModifyFlagController implements BaseTaskModifyFalg{
	
	public boolean isWritabled(String csid){
		return isWritabled(csid, null);
	}
	
	public void writeTrue(String csid){
		writeTrue(csid, null);
	}
	
	@Override
	public synchronized boolean isWritabled(String csid, String ctid){
		TaskModifyFlagMap.Writable writable = TaskModifyFlagMap.getTaskCreate(csid);
		if(writable == null){
			writable = new TaskModifyFlagMap.Writable();
			TaskModifyFlagMap.addTaskCreate(csid, writable);
		}
		synchronized (writable) {
			if(writable.isWritabled){
				writable.isWritabled = false;
				writable.time = System.currentTimeMillis();
				return true;
			}else if(writable.isOvertime()){
					writable.time = System.currentTimeMillis();
					return true;
				}
		}
		
		return false;
	}
	
	@Override
	public void writeTrue(String csid, String ctid) {
		TaskModifyFlagMap.Writable writable = TaskModifyFlagMap.getTaskCreate(csid);
		if(writable == null){
			writable = new TaskModifyFlagMap.Writable();
			TaskModifyFlagMap.addTaskCreate(csid, writable);
		}else
			writable.isWritabled = true;
	}
}
