package com.hc.entity.collection.task.record;

public class CollectionTaskUpdateRecord {
	private Integer id;

	private Long time;

	private Short type;

	private String user;

	private String requestMessage;

	private String responseMessage;

	public CollectionTaskUpdateRecord() {
	}

	public CollectionTaskUpdateRecord(Integer id, String responseMessage) {
		this.id = id;
		this.responseMessage = responseMessage;
	}

	public CollectionTaskUpdateRecord(Integer id, Integer type, String user, String requestMessage) {
		this.id = id;
		this.time = System.currentTimeMillis();	//加入生成系统当前时间参数
		this.type = type.shortValue();
		this.user = user;
		this.requestMessage = requestMessage;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getTime() { return time; }

	public void setTime(Long time) { this.time = time; }

	public Short getType() { return type; }

	public void setType(Short type) { this.type = type; }

	public String getUser() { return user; }

	public void setUser(String user) { this.user = user; }

	public String getRequestMessage() { return requestMessage; }

	public void setRequestMessage(String requestMessage) { this.requestMessage = requestMessage; }

	public String getResponseMessage() { return responseMessage; }

	public void setResponseMessage(String responseMessage) { this.responseMessage = responseMessage; }
}