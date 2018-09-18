package com.hc.vo;

import java.util.List;

import com.hc.entity.service.urlDir.ServiceUrlDir;

public class ServiceUrlVo {
	private String server;
	private String name;
	private String dirtype;
	private List<ServiceUrlDir> subs;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDirtype() {
		return dirtype;
	}

	public void setDirtype(String dirtype) {
		this.dirtype = dirtype;
	}

	public List<ServiceUrlDir> getSubs() {
		return subs;
	}

	public void setSubs(List<ServiceUrlDir> subs) {
		this.subs = subs;
	}

}
