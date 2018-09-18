package com.hc.entity.collection.application;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hc.entity.collection.task.CollectionTask;

@JsonInclude(Include.NON_NULL)
public class CollectionApplication {
	private String egName;

	private String version;

	private String chName;

	private String author;

	private String description;

	private Integer minVersion;

	private Short isUsed;

	private List<CollectionTask> cts;

	private String csid;

	private String config;

	public CollectionApplication() {
		// TODO Auto-generated constructor stub
	}

	public CollectionApplication(String egName, String version, String chName, String author, String description) {
		super();
		this.egName = egName;
		this.version = version;
		this.chName = chName;
		this.author = author;
		this.description = description;
	}

	public String getEgName() {
		return egName;
	}

	public void setEgName(String egName) {
		this.egName = egName;
	}

	public String getChName() {
		return chName;
	}

	public void setChName(String chName) {
		this.chName = chName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public Integer getMinVersion() {
		return minVersion;
	}

	public void setMinVersion(Integer minVersion) {
		this.minVersion = minVersion;
	}

	public Short getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Short isUsed) {
		this.isUsed = isUsed;
	}

	public List<CollectionTask> getCts() {
		return cts;
	}

	public void setCts(List<CollectionTask> cts) {
		this.cts = cts;
	}

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	@Override
	public String toString() {
		return "CollectionApplication [egName=" + egName + ", version=" + version + ", chName=" + chName + ", author="
				+ author + ", description=" + description + ", minVersion=" + minVersion + ", isUsed=" + isUsed
				+ ", cts=" + cts + ", csid=" + csid + ", config=" + config + "]";
	}

}