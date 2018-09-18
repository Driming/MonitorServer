package com.hc.vo;

import java.util.List;

public class StorageDataIndexDetailInfoVo {

	private String id;

	private String collectname;

	private String field;

	private List<String> elements;

	private List<String> values;

	private Short fieldtype;

	private Double count;

	private Double percent;

	private List<String> fields;

	private Long time;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCollectname() {
		return collectname;
	}

	public void setCollectname(String collectname) {
		this.collectname = collectname;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public List<String> getElements() {
		return elements;
	}

	public void setElements(List<String> elements) {
		this.elements = elements;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public Short getFieldtype() {
		return fieldtype;
	}

	public void setFieldtype(Short fieldtype) {
		this.fieldtype = fieldtype;
	}

	public Double getCount() {
		return count;
	}

	public void setCount(Double count) {
		this.count = count;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	public List<String> getFields() {
		return fields;
	}

	public void setFields(List<String> fields) {
		this.fields = fields;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

}
