package com.hc.entity.collection.task;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hc.entity.collection.application.CollectionApplication;
import com.hc.entity.collection.server.CollectionServer;

@JsonInclude(Include.NON_NULL)
public class CollectionTask {
	private String ctid;
	private String csid;
	private String name;
	private String status;
	private String statusReason;
	private Long lastExecuteTime;

	private Integer totalDataSourceNum;
	private Long totalDataSourceSize;
	private Integer totalDataResultNum;
	private Long totalDataResultSize;

	private String type;
	private Short utc;
	private String config;
	private String collectConfig;
	private String label;

	private String caName;
	private String caVersion;
	private Short isUsed;

	private CollectionServer cs;
	private CollectionApplication ca;

	public String getCtid() {
		return ctid;
	}

	public void setCtid(String ctid) {
		this.ctid = ctid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusReason() {
		return statusReason;
	}

	public void setStatusReason(String statusReason) {
		this.statusReason = statusReason;
	}

	public Long getLastExecuteTime() {
		return lastExecuteTime;
	}

	public void setLastExecuteTime(Long lastExecuteTime) {
		this.lastExecuteTime = lastExecuteTime;
	}

	public Integer getTotalDataSourceNum() {
		return totalDataSourceNum;
	}

	public void setTotalDataSourceNum(Integer totalDataSourceNum) {
		this.totalDataSourceNum = totalDataSourceNum;
	}

	public Long getTotalDataResultSize() {
		return totalDataResultSize;
	}

	public void setTotalDataResultSize(Long totalDataResultSize) {
		this.totalDataResultSize = totalDataResultSize;
	}

	public Integer getTotalDataResultNum() {
		return totalDataResultNum;
	}

	public void setTotalDataResultNum(Integer totalDataResultNum) {
		this.totalDataResultNum = totalDataResultNum;
	}

	public Long getTotalDataSourceSize() {
		return totalDataSourceSize;
	}

	public void setTotalDataSourceSize(Long totalDataSourceSize) {
		this.totalDataSourceSize = totalDataSourceSize;
	}

	public String getConfig() {
		return config;
	}

	public void setConfig(String config) {
		this.config = config;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCsid() {
		return csid;
	}

	public void setCsid(String csid) {
		this.csid = csid;
	}

	public String getCaName() {
		return caName;
	}

	public void setCaName(String caName) {
		this.caName = caName;
	}

	public String getCaVersion() {
		return caVersion;
	}

	public void setCaVersion(String caVersion) {
		this.caVersion = caVersion;
	}

	public Short getIsUsed() {
		return isUsed;
	}

	public void setIsUsed(Short isUsed) {
		this.isUsed = isUsed;
	}

	public CollectionServer getCs() {
		return cs;
	}

	public void setCs(CollectionServer cs) {
		this.cs = cs;
	}

	public CollectionApplication getCa() {
		return ca;
	}

	public void setCa(CollectionApplication ca) {
		this.ca = ca;
	}

	public String getCollectConfig() {
		return collectConfig;
	}

	public void setCollectConfig(String collectConfig) {
		this.collectConfig = collectConfig;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Short getUtc() {
		return utc;
	}

	public void setUtc(Short utc) {
		this.utc = utc;
	}

}
