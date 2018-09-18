package com.hc.vo;

import java.util.List;

public class IndexInfoVo {
	private List<ServerInfo> collectServers;
	private List<ServerInfo> storageServers;
	private List<ServerInfo> serviceServers;

	public List<ServerInfo> getCollectServers() {
		return collectServers;
	}

	public void setCollectServers(List<ServerInfo> collectServers) {
		this.collectServers = collectServers;
	}

	public List<ServerInfo> getStorageServers() {
		return storageServers;
	}

	public void setStorageServers(List<ServerInfo> storageServers) {
		this.storageServers = storageServers;
	}

	public List<ServerInfo> getServiceServers() {
		return serviceServers;
	}

	public void setServiceServers(List<ServerInfo> serviceServers) {
		this.serviceServers = serviceServers;
	}

	public static class ServerInfo {
		private String server;
		private String name;
		private boolean connect;

		public String getServer() {
			return server;
		}

		public void setServer(String server) {
			this.server = server;
		}

		public boolean isConnect() {
			return connect;
		}

		public void setConnect(boolean connect) {
			this.connect = connect;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
