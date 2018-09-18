package com.hc.vo;

import java.util.List;

import com.hc.entity.collection.application.CollectionApplication;
import com.hc.entity.collection.task.CollectionTask;

public class MultiConditionVo {
	private List<String> csids;
	private List<CollectionApplication> cas;
	private List<CollectionTask> cts;
	private List<String> statuss;
	private List<String> labels;
	private Integer startDelay;
	private Integer endDelay;
	private Long starttime;
	private Long endtime;
	private Integer currentPage;
	private Integer pageSize;

	public List<String> getCsids() {
		return csids;
	}

	public void setCsids(List<String> csids) {
		this.csids = csids;
	}

	public List<CollectionApplication> getCas() {
		return cas;
	}

	public void setCas(List<CollectionApplication> cas) {
		this.cas = cas;
	}

	public List<CollectionTask> getCts() {
		return cts;
	}

	public void setCts(List<CollectionTask> cts) {
		this.cts = cts;
	}

	public List<String> getStatuss() {
		return statuss;
	}

	public void setStatuss(List<String> statuss) {
		this.statuss = statuss;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

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

	public Integer getStartDelay() {
		return startDelay;
	}

	public void setStartDelay(Integer startDelay) {
		this.startDelay = startDelay;
	}

	public Integer getEndDelay() {
		return endDelay;
	}

	public void setEndDelay(Integer endDelay) {
		this.endDelay = endDelay;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

}
