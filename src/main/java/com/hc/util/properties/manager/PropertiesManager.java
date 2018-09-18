package com.hc.util.properties.manager;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Properties;

import com.hc.entity.config.RequestConfigCache;

public class PropertiesManager {
	private static Properties prop;

	static {
		try {
			URL url = PropertiesManager.class.getClassLoader().getResource("setting.properties");
			prop = new Properties();
			prop.load(new FileInputStream(url.getPath()));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean updateProperties(String key, String value) {
		FileOutputStream fileOut = null;
		try {
			URL url = PropertiesManager.class.getClassLoader().getResource("setting.properties");
			fileOut = new FileOutputStream(url.getPath());
			prop.setProperty(key, value);
			prop.store(fileOut, null);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean updateProperties(RequestConfigCache config) {
		FileOutputStream fileOut = null;
		try {
			URL url = PropertiesManager.class.getClassLoader().getResource("setting.properties");
			fileOut = new FileOutputStream(url.getPath());
			Field[] field = config.getClass().getDeclaredFields();
			for (int i = 0; i < field.length; i++) {
				try {
					field[i].setAccessible(true);
					String key = field[i].getName();
					String value = (String) field[i].get(config);
					prop.setProperty(key, value);
				} catch (IllegalArgumentException | IllegalAccessException e) {
					e.printStackTrace();
				}
			}
			prop.store(fileOut, null);
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static Properties getProp() {
		return prop;
	}

}
