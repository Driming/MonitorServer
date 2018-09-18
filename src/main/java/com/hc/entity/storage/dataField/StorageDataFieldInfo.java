package com.hc.entity.storage.dataField;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class StorageDataFieldInfo extends StorageDataFieldInfoKey {
	private String cname;

	private Double collectsize;

	private Double datacount;

	private Double datasize;

	private Double indexsize;

	private Double storagesize;

	private Double avgobjsize;

	private Long datetime;

	private Double expectday;

	private Double expectgrowth;

	private Double percentcount;

	private Double percentsize;

	private String datatype;

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

	public Double getDatasize() {
		return datasize;
	}

	public void setDatasize(Double datasize) {
		this.datasize = datasize;
	}

	public Double getIndexsize() {
		return indexsize;
	}

	public void setIndexsize(Double indexsize) {
		this.indexsize = indexsize;
	}

	public Double getStoragesize() {
		return storagesize;
	}

	public void setStoragesize(Double storagesize) {
		this.storagesize = storagesize;
	}

	public Double getAvgobjsize() {
		return avgobjsize;
	}

	public void setAvgobjsize(Double avgobjsize) {
		this.avgobjsize = avgobjsize;
	}

	public Long getDatetime() {
		return datetime;
	}

	public void setDatetime(Long datetime) {
		this.datetime = datetime;
	}

	public Double getExpectday() {
		return expectday;
	}

	public void setExpectday(Double expectday) {
		this.expectday = expectday;
	}

	public Double getExpectgrowth() {
		return expectgrowth;
	}

	public void setExpectgrowth(Double expectgrowth) {
		this.expectgrowth = expectgrowth;
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

	public String getDatatype() {
		return datatype;
	}

	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}

}