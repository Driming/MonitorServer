package com.hc.entity.control.middle.ra;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.hc.entity.control.access.AccessControl;

@JsonInclude(Include.NON_NULL)
public class RoleAccessMiddleKey {
	private Integer rid;

	private String name;

	private Integer acid;

	private List<Integer> acids;

	private AccessControl access;

	private List<AccessControl> accesses;

	public Integer getRid() {
		return rid;
	}

	public void setRid(Integer rid) {
		this.rid = rid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getAcid() {
		return acid;
	}

	public void setAcid(Integer acid) {
		this.acid = acid;
	}

	public List<Integer> getAcids() {
		return acids;
	}

	public void setAcids(List<Integer> acids) {
		this.acids = acids;
	}

	public AccessControl getAccess() {
		return access;
	}

	public void setAccess(AccessControl access) {
		this.access = access;
	}

	public List<AccessControl> getAccesses() {
		return accesses;
	}

	public void setAccesses(List<AccessControl> accesses) {
		this.accesses = accesses;
	}

	@Override
	public String toString() {
		return "RoleAccessMiddleKey [rid=" + rid + ", name=" + name + ", acid=" + acid + ", acids=" + acids
				+ ", access=" + access + ", accesses=" + accesses + "]";
	}

}