package com.hc.entity.service.urlDir;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ServiceUrlDir {
	private String server;

	private String name;

	private String url;

	private String dirtype;

	private String cname;

	private String dirurl;

	private String dirserver;

	private Short isroot;

	private Integer sequence;

	private List<ServiceUrlDir> subs;

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDirtype() {
		return dirtype;
	}

	public void setDirtype(String dirtype) {
		this.dirtype = dirtype == null ? null : dirtype.trim();
	}

	public String getCname() {
		return cname;
	}

	public void setCname(String cname) {
		this.cname = cname == null ? null : cname.trim();
	}

	public String getDirurl() {
		return dirurl;
	}

	public void setDirurl(String dirurl) {
		this.dirurl = dirurl;
	}

	public String getDirserver() {
		return dirserver;
	}

	public void setDirserver(String dirserver) {
		this.dirserver = dirserver;
	}

	public Short getIsroot() {
		return isroot;
	}

	public void setIsroot(Short isroot) {
		this.isroot = isroot;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public List<ServiceUrlDir> getSubs() {
		return subs;
	}

	public void setSubs(List<ServiceUrlDir> subs) {
		this.subs = subs;
	}

	public ServiceUrlDir clone() {
		ServiceUrlDir urlDir = new ServiceUrlDir();
		urlDir.setServer(server);
		urlDir.setName(name);
		urlDir.setUrl(url);
		urlDir.setDirtype(dirtype);
		urlDir.setIsroot(isroot);
		urlDir.setSubs(subs);
		urlDir.setCname(cname);
		urlDir.setDirserver(dirserver);
		urlDir.setDirurl(dirurl);
		urlDir.setSequence(sequence);
		return urlDir;
	}

}