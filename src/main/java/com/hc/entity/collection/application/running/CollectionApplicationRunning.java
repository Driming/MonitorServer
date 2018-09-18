package com.hc.entity.collection.application.running;

import java.util.List;

import com.hc.entity.collection.application.CollectionApplication;
import com.hc.entity.collection.task.CollectionTask;

public class CollectionApplicationRunning extends CollectionApplicationRunningKey {
	private String caName;

	private String caVersion;

	private Float usedCpu;

	private Float usedMemory;

	private Float upload;

	private Float download;

	private Float readSpeed;

	private Float writeSpeed;

	private Short isUsed;

	private CollectionApplication ca;

	private List<CollectionTask> cts;

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

	public Float getUsedCpu() {
		return usedCpu;
	}

	public void setUsedCpu(Float usedCpu) {
		this.usedCpu = usedCpu;
	}

	public Float getUsedMemory() {
		return usedMemory;
	}

	public void setUsedMemory(Float usedMemory) {
		this.usedMemory = usedMemory;
	}

	public Float getUpload() {
		return upload;
	}

	public void setUpload(Float upload) {
		this.upload = upload;
	}

	public Float getDownload() {
		return download;
	}

	public void setDownload(Float download) {
		this.download = download;
	}

	public Float getReadSpeed() {
		return readSpeed;
	}

	public void setReadSpeed(Float readSpeed) {
		this.readSpeed = readSpeed;
	}

	public Float getWriteSpeed() {
		return writeSpeed;
	}

	public void setWriteSpeed(Float writeSpeed) {
		this.writeSpeed = writeSpeed;
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

	public CollectionApplication getCa() {
		return ca;
	}

	public void setCa(CollectionApplication ca) {
		this.ca = ca;
	}

}