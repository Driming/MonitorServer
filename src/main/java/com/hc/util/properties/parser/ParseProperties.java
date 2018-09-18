package com.hc.util.properties.parser;

import java.util.Properties;

import com.hc.util.properties.entity.PropertiesEntity;
import com.hc.util.properties.manager.PropertiesManager;
import com.hc.vo.JarInfoVo;

public class ParseProperties {

	public static JarInfoVo parseJarInfoProperties(Properties properties) {
		JarInfoVo jarInfo = new JarInfoVo();
		jarInfo.setName(properties.getProperty("name"));
		jarInfo.setCname(properties.getProperty("cname"));
		jarInfo.setVersion(properties.getProperty("version"));
		jarInfo.setAuthor(properties.getProperty("author"));
		jarInfo.setDescription(properties.getProperty("description"));
		return jarInfo;
	}

	public static PropertiesEntity parseSettingProperties() {
		PropertiesEntity properties = new PropertiesEntity();
		Properties prop = PropertiesManager.getProp();
		properties.setDriver(prop.getProperty("Driver"));
		properties.setJarResourceName(prop.getProperty("jarResourceName"));
		properties.setTempDriver(prop.getProperty("TempDriver"));
		properties.setUriSendSms(prop.getProperty("UriSendSms"));
		properties.setApiKey(prop.getProperty("ApiKey"));
		properties.setCaptcha(prop.getProperty("Captcha"));
		properties.setCaptchaUrl(prop.getProperty("CaptchaUrl"));
		properties.setFlags(prop.getProperty("flag").split(";"));
		properties.setIp(prop.getProperty("ip"));
		properties.setPort(Integer.parseInt(prop.getProperty("port")));
		return properties;
	}
}
