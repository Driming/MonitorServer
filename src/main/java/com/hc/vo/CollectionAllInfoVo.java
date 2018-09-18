package com.hc.vo;

import java.util.List;

import com.hc.entity.collection.application.CollectionApplication;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.entity.collection.task.CollectionTask;

public class CollectionAllInfoVo {
	private List<CollectionServer> servers;
	private List<CollectionApplication> drivers;
	private List<CollectionTask> tasks;
	private List<String> labels;

	public List<CollectionServer> getServers() {
		return servers;
	}

	public void setServers(List<CollectionServer> servers) {
		this.servers = servers;
	}

	public List<CollectionApplication> getDrivers() {
		return drivers;
	}

	public void setDrivers(List<CollectionApplication> drivers) {
		this.drivers = drivers;
	}

	public List<CollectionTask> getTasks() {
		return tasks;
	}

	public void setTasks(List<CollectionTask> tasks) {
		this.tasks = tasks;
	}

	public List<String> getLabels() {
		return labels;
	}

	public void setLabels(List<String> labels) {
		this.labels = labels;
	}

}
