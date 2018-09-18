package com.hc.entity.control.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class User {
	private Integer uid;

	private String username;

	private String password;

	private String phone;

	private Short isPass;

	private Long time;

	private String verificationCode;

	private String captcha;

	private String captchaFlag;

	public Integer getUid() {
		return uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password == null ? null : password.trim();
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Short getIsPass() {
		return isPass;
	}

	public void setIsPass(Short isPass) {
		this.isPass = isPass;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getCaptchaFlag() {
		return captchaFlag;
	}

	public void setCaptchaFlag(String captchaFlag) {
		this.captchaFlag = captchaFlag;
	}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", password=" + password + ", phone=" + phone
				+ ", isPass=" + isPass + ", time=" + time + ", verificationCode=" + verificationCode + ", captcha="
				+ captcha + ", captchaFlag=" + captchaFlag + "]";
	}

}