package com.hc.vo;

public class RecordVo {
	private Long createdTime;
	private String record;

	public Long getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Long createdTime) {
		this.createdTime = createdTime;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

	public static class Record {
		private String id;
		private String result;
		private byte[] exception;

		public Long sourceSize;
		private Integer sourceSum;
		private Long outcomeSize;
		private Integer outcomeSum;

		private Long timeStart;
		private Long timeEnd;
		private Long timeSave;
		private Long timeDelay;
		private Integer retryCount;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getResult() {
			return result;
		}

		public void setResult(String result) {
			this.result = result;
		}

		public byte[] getException() {
			return exception;
		}

		public void setException(byte[] exception) {
			this.exception = exception;
		}

		public Integer getSourceSum() {
			return sourceSum;
		}

		public void setSourceSum(Integer sourceSum) {
			this.sourceSum = sourceSum;
		}

		public Long getSourceSize() {
			return sourceSize;
		}

		public void setSourceSize(Long sourceSize) {
			this.sourceSize = sourceSize;
		}

		public Long getOutcomeSize() {
			return outcomeSize;
		}

		public void setOutcomeSize(Long outcomeSize) {
			this.outcomeSize = outcomeSize;
		}

		public Integer getOutcomeSum() {
			return outcomeSum;
		}

		public void setOutcomeSum(Integer outcomeSum) {
			this.outcomeSum = outcomeSum;
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

		public Long getTimeSave() {
			return timeSave;
		}

		public void setTimeSave(Long timeSave) {
			this.timeSave = timeSave;
		}

		public Long getTimeDelay() {
			return timeDelay;
		}

		public void setTimeDelay(Long timeDelay) {
			this.timeDelay = timeDelay;
		}

		public Integer getRetryCount() {
			return retryCount;
		}

		public void setRetryCount(Integer retryCount) {
			this.retryCount = retryCount;
		}
	}

}
