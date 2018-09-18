package com.hc.vo;

public class TaskHistoryPageInfoVo {
	private Long starttime;
	private Long endtime;
	private Float errPercent;
	private Integer currentPage;

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

	public Float getErrPercent() {
		return errPercent;
	}

	public void setErrPercent(Float errPercent) {
		this.errPercent = errPercent;
	}

	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

}
