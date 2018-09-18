package com.hc.entity.index.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ErrorInfoRecord {
	private Integer id;

	private short type;

	private String status;

	private Long time;

	private String server;

	private String name;

	private String reason;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server == null ? null : server.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason == null ? null : reason.trim();
	}
}