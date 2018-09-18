package com.hc.vo;

import java.util.List;

import com.hc.entity.control.middle.rus.RoleUserServerMiddleKey;
import com.hc.entity.control.user.User;

public class AddUserVo {
	private User user;
	private List<RoleUserServerMiddleKey> ruses;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<RoleUserServerMiddleKey> getRuses() {
		return ruses;
	}

	public void setRuses(List<RoleUserServerMiddleKey> ruses) {
		this.ruses = ruses;
	}

}
