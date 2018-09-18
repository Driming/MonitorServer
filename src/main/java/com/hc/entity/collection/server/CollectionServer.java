package com.hc.entity.collection.server;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hc.entity.collection.application.CollectionApplication;
import com.hc.entity.collection.task.CollectionTask;

@JsonInclude(Include.NON_NULL)
public class CollectionServer {
	private String csid;

	private String server;

	private String name;

	private String description;

	private String flag;

	private Short type;

	private String version;

	private Short isUsed;

	private List<CollectionApplication> cas;

	private List<CollectionTask> cts;

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid == null ? null : csid.trim();
	}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Short getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Short isUsed) {
		this.isUsed = isUsed;
	}

	public List<CollectionApplication> getCas() {
		return cas;
	}

	public void setCas(List<CollectionApplication> cas) {
		this.cas = cas;
	}

	public List<CollectionTask> getCts() {
		return cts;
	}

	public void setCts(List<CollectionTask> cts) {
		this.cts = cts;
	}

	@Override
	public String toString() {
		return "CollectionServer [csid=" + csid + ", server=" + server + ", description=" + description + ", flag="
				+ flag + ", type=" + type + ", version=" + version + ", isUsed=" + isUsed + ", cas=" + cas + ", cts="
				+ cts + "]";
	}

}