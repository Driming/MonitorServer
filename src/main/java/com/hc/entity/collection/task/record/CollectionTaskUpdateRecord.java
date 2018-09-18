package com.hc.entity.collection.task.record;

public class CollectionTaskUpdateRecord {
	private Integer id;

	private Long time;

	private Short type;

	private String user;

	private String requestmessage;

	private String responsemessage;

	public CollectionTaskUpdateRecord() {
	}

	public CollectionTaskUpdateRecord(Integer id, String responsemessage) {
		this.id = id;
		this.responsemessage = responsemessage;
	}

	public CollectionTaskUpdateRecord(Integer id, Integer type, String user, String requestmessage) {
		this.id = id;
		this.time = System.currentTimeMillis();
		this.type = type.shortValue();
		this.user = user;
		this.requestmessage = requestmessage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getRequestmessage() {
		return requestmessage;
	}

	public void setRequestmessage(String requestmessage) {
		this.requestmessage = requestmessage == null ? null : requestmessage.trim();
	}

	public String getResponsemessage() {
		return responsemessage;
	}

	public void setResponsemessage(String responsemessage) {
		this.responsemessage = responsemessage == null ? null : responsemessage.trim();
	}
}