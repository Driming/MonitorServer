package com.hc.entity.service.urlInfo;

import com.hc.entity.service.urlDir.ServiceUrlDir;

public class ServiceUrlInfo extends ServiceUrlDir{

	private String description;

	private String returntype;

	private String args;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description == null ? null : description.trim();
	}

	public String getReturntype() {
		return returntype;
	}

	public void setReturntype(String returntype) {
		this.returntype = returntype == null ? null : returntype.trim();
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args == null ? null : args.trim();
	}
}