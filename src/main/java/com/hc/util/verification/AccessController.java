package com.hc.util.verification;

import java.util.List;

import com.hc.dao.AccessControlDao;
import com.hc.util.StringUtils;
import com.hc.util.context.ContextCore;
import com.hc.util.map.TaskMap;

public class AccessController {
	private static AccessControlDao accessControlDao;

	static {
		accessControlDao = (AccessControlDao) ContextCore.getBean(AccessControlDao.class);
	}

	public static boolean createTask(String csid, Integer uid) {
		List<Integer> accesses = accessControlDao.findUserAccesses(csid, uid);
		if (StringUtils.isContainValue(TaskMap.CREATE_TASK, accesses)){
			return true;
		}else{
			return false;
		}
	}

	public static boolean controlTask(String csid, Integer uid, String status) {
		List<Integer> accesses = accessControlDao.findUserAccesses(csid, uid);
		if (((status.equals(TaskMap.STATUS_STOP) || status.equals(TaskMap.STATUS_RESTART))
				&& !StringUtils.isContainValue(TaskMap.RESTART_STOP_TASK, accesses))
				|| ((status.equals(TaskMap.STATUS_CONTINUE) || status.equals(TaskMap.STATUS_PAUSE))
						&& !StringUtils.isContainValue(TaskMap.CONTINUE_PAUSE_TASK, accesses))){
			return false;
		}else{
			return true;
		}
	}

	public static boolean updateConfig(String csid, Integer uid) {
		List<Integer> accesses = accessControlDao.findUserAccesses(csid, uid);
		if (StringUtils.isContainValue(TaskMap.UPDATE_TASK_CONGIF, accesses)){
			return true;
		}else{
			return false;
		}
	}
	
	public static boolean controlSystem(String csid, Integer uid){
		List<Integer> accesses = accessControlDao.findUserAccesses(csid, uid);
		if(StringUtils.isContainValue(TaskMap.CONTROL_SYSTEM, accesses)){
			return true;
		}else{
			return false;
		}
	}
}
