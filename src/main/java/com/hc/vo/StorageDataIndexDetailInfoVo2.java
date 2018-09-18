package com.hc.vo;

import java.util.List;

public class StorageDataIndexDetailInfoVo2 {
	public List<String> elements;
	private List<List<String>> values;
	private List<List<String>> fields;
	private List<Long> times;

	public List<String> getElements() {
		return elements;
	}

	public void setElements(List<String> elements) {
		this.elements = elements;
	}

	public List<List<String>> getValues() {
		return values;
	}

	public void setValues(List<List<String>> values) {
		this.values = values;
	}

	public List<List<String>> getFields() {
		return fields;
	}

	public void setFields(List<List<String>> fields) {
		this.fields = fields;
	}

	public List<Long> getTimes() {
		return times;
	}

	public void setTimes(List<Long> times) {
		this.times = times;
	}

}
