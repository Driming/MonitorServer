package com.hc.entity.storage.dataField.indexDetail;

public class StorageDataIndexDetailInfo extends StorageDataIndexDetailInfoKey {
	private Double count;

	private Double percent;

	private String fields;

	private Long time;

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

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields == null ? null : fields.trim();
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public StorageDataIndexDetailInfo clone() {
		StorageDataIndexDetailInfo info = new StorageDataIndexDetailInfo();
		info.setId(super.getId());
		info.setCollectname(super.getCollectname());
		info.setField(super.getField());
		info.setFieldtype(super.getFieldtype());
		info.setElements(super.getElements());
		info.setValues(super.getValues());
		info.setCount(count);
		info.setPercent(percent);
		info.setTime(time);
		info.setFields(fields);
		return info;
	}

	@Override
	public String toString() {
		return "StorageDataIndexDetailInfo [count=" + count + ", percent=" + percent + ", fields=" + fields + ", time="
				+ time + ", getId()=" + getId() + ", getCollectname()=" + getCollectname() + ", getField()="
				+ getField() + ", getElements()=" + getElements() + ", getValues()=" + getValues() + ", getFieldtype()="
				+ getFieldtype() + "]";
	}

}