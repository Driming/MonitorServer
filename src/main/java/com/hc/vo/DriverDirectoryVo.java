package com.hc.vo;

import java.util.List;

public class DriverDirectoryVo {
	private String name;
	private String type;
	private List<DriverDirectoryVo> subDir;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<DriverDirectoryVo> getSubDir() {
		return subDir;
	}

	public void setSubDir(List<DriverDirectoryVo> subDir) {
		this.subDir = subDir;
	}

	
}
