package com.hc.vo;

import java.util.List;

import com.hc.entity.collection.history.CollectionHistory;

public class NodeResultVo {
	private Long starttime;
	private Long endtime;
	private List<CollectionHistory> chs;

	public Long getStarttime() {
		return starttime;
	}

	public void setStarttime(Long starttime) {
		this.starttime = starttime;
	}

	public Long getEndtime() {
		return endtime;
	}

	public void setEndtime(Long endtime) {
		this.endtime = endtime;
	}

	public List<CollectionHistory> getChs() {
		return chs;
	}

	public void setChs(List<CollectionHistory> chs) {
		this.chs = chs;
	}

}
