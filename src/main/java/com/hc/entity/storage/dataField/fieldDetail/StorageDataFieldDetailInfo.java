package com.hc.entity.storage.dataField.fieldDetail;

public class StorageDataFieldDetailInfo extends StorageDataFieldDetailInfoKey {
	private Double count;

	private Double percent;

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

	public StorageDataFieldDetailInfo clone() {
		StorageDataFieldDetailInfo info = new StorageDataFieldDetailInfo();
		info.setId(super.getId());
		info.setCollectname(super.getCollectname());
		info.setType(super.getType());
		info.setFieldtype(super.getFieldtype());
		info.setField(super.getField());
		info.setCount(count);
		info.setPercent(percent);
		return info;
	}
}