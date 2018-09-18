package com.hc.util.properties.manager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class CommonPropertiesManager {
	public Properties prop;

	public CommonPropertiesManager(InputStream in) {
		try {
			prop = new Properties();
			prop.load(in);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (in != null)
					in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static CommonPropertiesManager init(InputStream in) {
		return new CommonPropertiesManager(in);
	}

}
