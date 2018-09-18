package com.hc.util.properties;

import com.hc.util.properties.entity.PropertiesEntity;
import com.hc.util.properties.parser.ParseProperties;

public class PropertiesBuilder {
	private static PropertiesEntity prop = null;
	
	public static PropertiesEntity buildProperties(){
		if(prop == null)
			prop = ParseProperties.parseSettingProperties();
		return prop;
	}
}
