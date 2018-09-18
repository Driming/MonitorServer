package com.hc.util;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.hc.vo.DriverDirectoryVo;

public class FileUtils {

	//删除文件夹，包含本身
	public static boolean removeIncludeFolder(File folder) {
		boolean flag = true;
		if (!folder.exists())
			return flag;
		if (folder.isDirectory())
			for (File file : folder.listFiles())
				if (file.isDirectory())
					if (!removeIncludeFolder(folder))
						flag = false;
					else if (!file.delete())
						flag = false;

		if (!folder.delete())
			flag = false;
		return flag;
	}

	public static boolean removeExcludeFolder(File folder) {
		boolean flag = true;
		if (!folder.exists())
			return flag;
		if (folder.isDirectory())
			for (File file : folder.listFiles())
				if (!file.delete())
					flag = false;
		return flag;
	}

	public static DriverDirectoryVo depthFirstFiles(DriverDirectoryVo ddv, File root) {
		if (root.isDirectory()) {
			ddv.setName(root.getName());
			ddv.setType("dir");
			List<DriverDirectoryVo> subDir = new LinkedList<DriverDirectoryVo>();
			File[] files = root.listFiles();
			for (File file : files) {
				DriverDirectoryVo subDDv = new DriverDirectoryVo();
				depthFirstFiles(subDDv, file);
				subDir.add(subDDv);
			}
			ddv.setSubDir(subDir);
		} else {
			ddv.setName(root.getName());
			ddv.setType("file");
		}

		return ddv;
	}

	public static List<DriverDirectoryVo> depthFirstFiles(List<DriverDirectoryVo> subDir, File[] roots) {
		for (File file : roots) {
			DriverDirectoryVo subDDv = new DriverDirectoryVo();
			depthFirstFiles(subDDv, file);
			subDir.add(subDDv);
		}
		return subDir;
	}
	
}
