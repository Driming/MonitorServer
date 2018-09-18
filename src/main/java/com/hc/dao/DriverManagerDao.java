package com.hc.dao;

import java.io.File;

import org.springframework.stereotype.Repository;

import com.hc.util.FileUtils;
import com.hc.util.StringUtils;
import com.hc.util.properties.PropertiesBuilder;

@Repository
public class DriverManagerDao {
	private String path = StringUtils.addSeparator(PropertiesBuilder.buildProperties().getDriver());
	private String tempPath = StringUtils.addSeparator(PropertiesBuilder.buildProperties().getTempDriver());
	
	public String getRoot(){
		return path;
	}
	
	public String getTempRoot(){
		return tempPath;
	}

	public File[] findDriverFiles() {
		File file = new File(path);
		return file.listFiles();
	}

	public boolean createFolder(String root) {
		root = StringUtils.join(path, root);
		File file = new File(root);
		if(!file.exists())
			return file.mkdirs();
		return true;
	}

	public boolean deleteFolder(String root) {
		root = StringUtils.join(path, root);
		File file = new File(root);
		return FileUtils.removeIncludeFolder(file);
	}

	public boolean updateFolder(String oldFolder, String newFolder) {
		oldFolder = StringUtils.join(path, oldFolder);
		newFolder = StringUtils.join(path, newFolder);
		File oldFile = new File(oldFolder);
		File newFile = new File(newFolder);
		return oldFile.renameTo(newFile);
	}

	public File downloadJarFile(String driverName, String driverVersion) {
		String jarPath = StringUtils.join(path, driverName, "/", driverVersion);
		File file = new File(jarPath);
		if(file.exists() && file.listFiles().length > 0)
			return file.listFiles()[0];
		return null;
	}

}
