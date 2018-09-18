package com.hc.util.count;

import java.util.ArrayList;
import java.util.List;

public class CountWebsocketConnected {
	public static int connectedNum = 0;

	public static List<DetailConnected> details = new ArrayList<>();

	public static class DetailConnected {
		private String time;
		private String csid;
		private Integer index;
		private String status;

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public String getCsid() {
			return csid;
		}

		public void setCsid(String csid) {
			this.csid = csid;
		}

		public Integer getIndex() {
			return index;
		}

		public void setIndex(Integer index) {
			this.index = index;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

	}
}
