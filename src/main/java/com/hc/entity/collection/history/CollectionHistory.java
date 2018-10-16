package com.hc.entity.collection.history;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hc.entity.collection.task.CollectionTask;

@JsonInclude(Include.NON_NULL)
public class CollectionHistory {

	public static class Error {
		private String fileName;
		private String Exception;

		public String getException() {
			return Exception;
		}

		public void setException(String Exception) {
			this.Exception = Exception;
		}

		public String getFileName() {
			return fileName;
		}

		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
	}

	private String ctid;
	private String csid;
	private Long ctime;
	private Long time;
	private Long timeStart;
	private Long timeEnd;
	private String status;
	private Integer dataSourceNum;
	private Long dataSourceSize;
	private Integer dataResultNum;
	private Long dataResultSize;
	private List<Error> errors;
	private Long delay;
	private Integer retry;
	private CollectionTask ct;

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getCtid() {
		return ctid;
	}

	public void setCtid(String ctid) {
		this.ctid = ctid;
	}

	public Long getCtime() {
		return ctime;
	}

	public void setCtime(Long ctime) {
		this.ctime = ctime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getDataSourceNum() {
		return dataSourceNum;
	}

	public void setDataSourceNum(Integer dataSourceNum) {
		this.dataSourceNum = dataSourceNum;
	}

	public Integer getDataResultNum() {
		return dataResultNum;
	}

	public void setDataResultNum(Integer dataResultNum) {
		this.dataResultNum = dataResultNum;
	}

	public Long getDataSourceSize() {
		return dataSourceSize;
	}

	public void setDataSourceSize(Long dataSourceSize) {
		this.dataSourceSize = dataSourceSize;
	}

	public Long getDataResultSize() {
		return dataResultSize;
	}

	public void setDataResultSize(Long dataResultSize) {
		this.dataResultSize = dataResultSize;
	}

	public List<Error> getErrors() {
		return errors;
	}

	public void setErrors(List<Error> errors) {
		this.errors = errors;
	}

	public Integer getRetry() {
		return retry;
	}

	public void setRetry(Integer retry) {
		this.retry = retry;
	}

	public CollectionTask getCt() {
		return ct;
	}

	public void setCt(CollectionTask ct) {
		this.ct = ct;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Long getDelay() {
		return delay;
	}

	public void setDelay(Long delay) {
		this.delay = delay;
	}

	public Long getTimeStart() {
		return timeStart;
	}

	public void setTimeStart(Long timeStart) {
		this.timeStart = timeStart;
	}

	public Long getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd(Long timeEnd) {
		this.timeEnd = timeEnd;
	}

	@Override
	public String toString() {
		return "CollectionHistory [ctid=" + ctid + ", csid=" + csid + ", ctime=" + ctime + ", time=" + time
				+ ", timeStart=" + timeStart + ", timeEnd=" + timeEnd + ", status=" + status + ", dataSourceNum="
				+ dataSourceNum + ", dataSourceSize=" + dataSourceSize + ", dataResultNum=" + dataResultNum
				+ ", dataResultSize=" + dataResultSize + ", errors=" + errors + ", delay=" + delay + ", retry=" + retry
				+ ", ct=" + ct + "]";
	}

}
