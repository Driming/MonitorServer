package com.hc.entity.sms;

public class SmsStatus {
	private String code;
	private Long time;

	public SmsStatus(String code, Long time) {
		super();
		this.code = code;
		this.time = time;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

}
