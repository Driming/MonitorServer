package com.hc.vo;

import java.util.List;

public class StatusPercentVo {
	private String status;
	private List<Integer> times;
	private List<Float> percents;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Integer> getTimes() {
		return times;
	}

	public void setTimes(List<Integer> times) {
		this.times = times;
	}

	public List<Float> getPercents() {
		return percents;
	}

	public void setPercents(List<Float> percents) {
		this.percents = percents;
	}

}
