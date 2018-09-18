package com.hc.entity.storage.space;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StorageSpaceInfo {
	private String id;

	private String name;

	private Double datacount;

	private Double dbspace;

	private Double freespace;

	private Double otherspace;

	private Double percentdbspace;

	private Double percentfreespace;

	private Double percentotherspace;

	private Double totalspace;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id == null ? null : id.trim();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getDatacount() {
		return datacount;
	}

	public void setDatacount(Double datacount) {
		this.datacount = datacount;
	}

	public Double getDbspace() {
		return dbspace;
	}

	public void setDbspace(Double dbspace) {
		this.dbspace = dbspace;
	}

	public Double getFreespace() {
		return freespace;
	}

	public void setFreespace(Double freespace) {
		this.freespace = freespace;
	}

	public Double getOtherspace() {
		return otherspace;
	}

	public void setOtherspace(Double otherspace) {
		this.otherspace = otherspace;
	}

	public Double getPercentdbspace() {
		return percentdbspace;
	}

	public void setPercentdbspace(Double percentdbspace) {
		this.percentdbspace = percentdbspace;
	}

	public Double getPercentfreespace() {
		return percentfreespace;
	}

	public void setPercentfreespace(Double percentfreespace) {
		this.percentfreespace = percentfreespace;
	}

	public Double getPercentotherspace() {
		return percentotherspace;
	}

	public void setPercentotherspace(Double percentotherspace) {
		this.percentotherspace = percentotherspace;
	}

	public Double getTotalspace() {
		return totalspace;
	}

	public void setTotalspace(Double totalspace) {
		this.totalspace = totalspace;
	}
}