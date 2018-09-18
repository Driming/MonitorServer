package com.hc.entity.collection.history.extra;

public class StatusPercent {
	public static class OneNode {
		private String statusName;
		private Float percent;
		private Integer time;

		public Integer getTime() {
			return time;
		}

		public void setTime(Integer time) {
			this.time = time;
		}

		public String getStatusName() {
			return statusName;
		}

		public void setStatusName(String statusName) {
			this.statusName = statusName;
		}

		public Float getPercent() {
			return percent;
		}

		public void setPercent(Float percent) {
			this.percent = percent;
		}
	}
}
