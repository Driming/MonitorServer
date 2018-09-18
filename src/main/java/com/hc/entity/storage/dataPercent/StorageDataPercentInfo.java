package com.hc.entity.storage.dataPercent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StorageDataPercentInfo extends StorageDataPercentInfoKey {
	private Short type;

	private String cname;

	private String typename;

	private Double collectsize;

	private Double datacount;

	private Double percentcount;

	private Double percentsize;

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname;
	}

	public Double getCollectsize() {
		return collectsize;
	}

	public void setCollectsize(Double collectsize) {
		this.collectsize = collectsize;
	}

	public Double getDatacount() {
		return datacount;
	}

	public void setDatacount(Double datacount) {
		this.datacount = datacount;
	}

	public Double getPercentcount() {
		return percentcount;
	}

	public void setPercentcount(Double percentcount) {
		this.percentcount = percentcount;
	}

	public Double getPercentsize() {
		return percentsize;
	}

	public void setPercentsize(Double percentsize) {
		this.percentsize = percentsize;
	}

	public String getTypename() {
		return typename;
	}

	public void setTypename(String typename) {
		this.typename = typename;
	}

}