package com.hc.vo;

import java.util.List;

public class RegisterServerVo {
	private String id;
	private List<Task> task;
	private List<Driver> driver;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Task> getTask() {
		return task;
	}

	public void setTask(List<Task> task) {
		this.task = task;
	}

	public List<Driver> getDriver() {
		return driver;
	}

	public void setDriver(List<Driver> driver) {
		this.driver = driver;
	}

	@Override
	public String toString() {
		return "RegisterServerVo [id=" + id + ", task=" + task + ", driver=" + driver + "]";
	}

	public static class Task {
		private String id;
		private String name;
		private String config;
		private String driverName;
		private String driverVersion;
		private String type;
		private Boolean utc;
		private String dispatchConfig;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getConfig() {
			return config;
		}

		public void setConfig(String config) {
			this.config = config;
		}

		public String getDriverName() {
			return driverName;
		}

		public void setDriverName(String driverName) {
			this.driverName = driverName;
		}

		public String getDriverVersion() {
			return driverVersion;
		}

		public void setDriverVersion(String driverVersion) {
			this.driverVersion = driverVersion;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public Boolean getUtc() {
			return utc;
		}

		public void setUtc(Boolean utc) {
			this.utc = utc;
		}

		public String getDispatchConfig() {
			return dispatchConfig;
		}

		public void setDispatchConfig(String dispatchConfig) {
			this.dispatchConfig = dispatchConfig;
		}

		@Override
		public String toString() {
			return "Task [id=" + id + ", name=" + name + ", config=" + config + ", driverName=" + driverName
					+ ", driverVersion=" + driverVersion + ", type=" + type + ", utc=" + utc + ", dispatchConfig="
					+ dispatchConfig + "]";
		}

	}

	public static class Driver {
		private String name;
		private String version;
		private String alias;
		private String config;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getAlias() {
			return alias;
		}

		public void setAlias(String alias) {
			this.alias = alias;
		}

		public String getConfig() {
			return config;
		}

		public void setConfig(String config) {
			this.config = config;
		}

		@Override
		public String toString() {
			return "Driver [name=" + name + ", version=" + version + ", alias=" + alias + ", config=" + config + "]";
		}

	}
}
