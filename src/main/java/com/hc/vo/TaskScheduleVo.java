package com.hc.vo;

import java.util.List;

public class TaskScheduleVo {
	private String ctid;
	private String csid;
	private String name;
	private String status;
	private List<List<Integer>> nodeResults;
	private Long lastExecuteTime;
	private Long start;
	private Long end;

	public String getCtid() {
		return ctid;
	}

	public void setCtid(String ctid) {
		this.ctid = ctid;
	}

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<List<Integer>> getNodeResults() {
		return nodeResults;
	}

	public void setNodeResults(List<List<Integer>> nodeResults) {
		this.nodeResults = nodeResults;
	}

	public Long getLastExecuteTime() {
		return lastExecuteTime;
	}

	public void setLastExecuteTime(Long lastExecuteTime) {
		this.lastExecuteTime = lastExecuteTime;
	}

	public Long getStart() {
		return start;
	}

	public void setStart(Long start) {
		this.start = start;
	}

	public Long getEnd() {
		return end;
	}

	public void setEnd(Long end) {
		this.end = end;
	}

	@Override
	public String toString() {
		return "TaskScheduleVo [ctid=" + ctid + ", csid=" + csid + ", name=" + name + ", status=" + status
				+ ", nodeResults=" + nodeResults + ", lastExecuteTime=" + lastExecuteTime + ", start=" + start
				+ ", end=" + end + "]";
	}

}
