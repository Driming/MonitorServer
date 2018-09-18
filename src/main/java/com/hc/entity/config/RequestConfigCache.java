package com.hc.entity.config;

public class RequestConfigCache {
	private String csid;

	private String ip;

	private String port;

	private String root;

	private String control;

	private String schedule;

	private String process;

	private String heartbeat;

	private String record;

	private String status;

	private String feedback;

	private String driver;

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port == null ? null : port.trim();
	}

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root == null ? null : root.trim();
	}

	public String getControl() {
		return control;
	}

	public void setControl(String control) {
		this.control = control == null ? null : control.trim();
	}

	public String getSchedule() {
		return schedule;
	}

	public void setSchedule(String schedule) {
		this.schedule = schedule == null ? null : schedule.trim();
	}

	public String getProcess() {
		return process;
	}

	public void setProcess(String process) {
		this.process = process == null ? null : process.trim();
	}

	public String getHeartbeat() {
		return heartbeat;
	}

	public void setHeartbeat(String heartbeat) {
		this.heartbeat = heartbeat == null ? null : heartbeat.trim();
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record == null ? null : record.trim();
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status == null ? null : status.trim();
	}

	public String getFeedback() {
		return feedback;
	}

	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	@Override
	public String toString() {
		return "RequestConfigCache [csid=" + csid + ", ip=" + ip + ", port=" + port + ", root=" + root + ", control="
				+ control + ", schedule=" + schedule + ", process=" + process + ", heartbeat=" + heartbeat + ", record="
				+ record + ", status=" + status + ", feedback=" + feedback + ", driver=" + driver + "]";
	}

}