package com.hc.util.properties.entity;

import java.util.Arrays;

public class PropertiesEntity {
	private String driver;
	private String jarResourceName;
	private String tempDriver;

	private String uriSendSms;
	private String apiKey;

	private String captcha;
	private String CaptchaUrl;

	private String[] flags;
	private String ip;
	private int port;

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getJarResourceName() {
		return jarResourceName;
	}

	public void setJarResourceName(String jarResourceName) {
		this.jarResourceName = jarResourceName;
	}

	public String getTempDriver() {
		return tempDriver;
	}

	public void setTempDriver(String tempDriver) {
		this.tempDriver = tempDriver;
	}

	public String getUriSendSms() {
		return uriSendSms;
	}

	public void setUriSendSms(String uriSendSms) {
		this.uriSendSms = uriSendSms;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}

	public String getCaptchaUrl() {
		return CaptchaUrl;
	}

	public void setCaptchaUrl(String captchaUrl) {
		CaptchaUrl = captchaUrl;
	}

	public String[] getFlags() {
		return flags;
	}

	public void setFlags(String[] flags) {
		this.flags = flags;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public String toString() {
		return "PropertiesEntity [driver=" + driver + ", jarResourceName=" + jarResourceName + ", tempDriver="
				+ tempDriver + ", uriSendSms=" + uriSendSms + ", apiKey=" + apiKey + ", captcha=" + captcha
				+ ", CaptchaUrl=" + CaptchaUrl + ", flags=" + Arrays.toString(flags) + ", ip=" + ip + ", port=" + port
				+ "]";
	}

}
