package com.hc.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hc.dao.CollectionMonitorDao;
import com.hc.dao.DriverManagerDao;
import com.hc.entity.collection.application.CollectionApplication;
import com.hc.entity.collection.server.CollectionServer;
import com.hc.util.FileUtils;
import com.hc.util.MessageUtils;
import com.hc.util.StringUtils;
import com.hc.util.file.UploadFileUtils;
import com.hc.util.file.processor.ZipFileProcessor;
import com.hc.util.map.ServerMap;
import com.hc.util.properties.PropertiesBuilder;
import com.hc.util.properties.manager.CommonPropertiesManager;
import com.hc.util.properties.parser.ParseProperties;
import com.hc.vo.DriverDirectoryVo;
import com.hc.vo.JarInfoVo;

@Service
public class DriverManagerService {

	@Autowired
	private DriverManagerDao driverManagerDao;
	
	@Autowired
	private CollectionMonitorDao collectionStatusDao;
	
	@Autowired
	private WebSocketSendService webSocketSendService;

	public Object findAllDriverFiles() {
		File[] file = driverManagerDao.findDriverFiles();
		List<DriverDirectoryVo> ddvs = new LinkedList<DriverDirectoryVo>();
		return MessageUtils.returnSuccess(FileUtils.depthFirstFiles(ddvs, file));
	}

	public Object createFolder(String root) {
		boolean result = driverManagerDao.createFolder(root);
		if (!result)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public Object deleteFolder(String root) {
		boolean result = driverManagerDao.deleteFolder(root);
		if (!result)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public Object updateFolder(String oldFolder, String newFolder) {
		boolean result = driverManagerDao.updateFolder(oldFolder, newFolder);
		if (!result)
			return MessageUtils.operationFailedError();
		return MessageUtils.operationSuccess();
	}

	public synchronized Object uploadJarFile(HttpServletRequest request, HttpServletResponse response) {
		List<FileItem> fileItem = UploadFileUtils.getFileItem(request, response);
		List<String> fileNames = UploadFileUtils.getFileNames(fileItem);

		String path = driverManagerDao.getTempRoot();
		if (fileNames.size() != 1)
			return MessageUtils.parameterNotStandardValueError();

		String milliDir = String.valueOf(System.currentTimeMillis());
		path = StringUtils.join(path, milliDir);
		try {
			UploadFileUtils.uploadFile(fileItem, null, path, "ISO-8859-1", null);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return MessageUtils.operationFailedError();
		}

		path = StringUtils.join(path, "/", fileNames.get(0));
		String fileName = PropertiesBuilder.buildProperties().getJarResourceName();
		InputStream in = ZipFileProcessor.readZipFile(path, fileName);
		if(in == null)
			return MessageUtils.jarFileNotFoundError();
		
		Properties jarProp = CommonPropertiesManager.init(in).prop;
		JarInfoVo jarInfo = ParseProperties.parseJarInfoProperties(jarProp);
		jarInfo.setFileDir(StringUtils.join(milliDir, "/", fileNames.get(0)));
		return MessageUtils.returnSuccess(jarInfo);
	}

	public void downloadJarFile(String driverName, String driverVersion, HttpServletResponse response) {
		if (driverName == null || driverVersion == null)
			return;

		File file = driverManagerDao.downloadJarFile(driverName, driverVersion);
		if (file == null)
			return;

		OutputStream os = null;
		try {
			os = response.getOutputStream();
			String fileName = URLEncoder.encode(file.getName(), "UTF-8");
			response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
			byte[] fileByte = org.apache.commons.io.FileUtils.readFileToByteArray(file);
			os.write(fileByte);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	public synchronized Object updateJarConfig(JarInfoVo jarInfo) {
		if (jarInfo.getName() == null 
				|| jarInfo.getVersion() == null
				|| jarInfo.getFileDir() == null)
			return MessageUtils.parameterNullError();

		String tempRoot = driverManagerDao.getTempRoot();
		String tempPath = StringUtils.join(tempRoot, jarInfo.getFileDir());

		String fileDir = jarInfo.getFileDir();
		String root = driverManagerDao.getRoot();
		root = StringUtils.join(root, jarInfo.getName(), "/", jarInfo.getVersion());
		String path = StringUtils.join(root, fileDir.substring(fileDir.lastIndexOf("/"), fileDir.length()));
	
		File rootFile = new File(root);
		rootFile.mkdirs();
		FileUtils.removeExcludeFolder(rootFile);
		
		File tempFile = new File(tempPath);
		if(!tempFile.exists())
			return MessageUtils.fileNotFountError();
		tempFile.renameTo(new File(path));
		
		// 更新最新驱动副版本
		CollectionApplication ca = new CollectionApplication(
				jarInfo.getName(), jarInfo.getVersion(), jarInfo.getCname(),
				jarInfo.getAuthor(), jarInfo.getDescription());
		List<CollectionApplication> existCa = collectionStatusDao.findCollectionApplication(
				jarInfo.getName(), jarInfo.getVersion());
		if(!existCa.isEmpty()){
			ca.setMinVersion(existCa.get(0).getMinVersion()+1);
			collectionStatusDao.updateCollectionApplication(ca);
		}else
			collectionStatusDao.addCollectionApplication(ca);
		
		// 发送websock消息
		int isUsedCs = 1;
		short type = ServerMap.COLLECT_SERVER;
		List<CollectionServer> servers = collectionStatusDao.findCollectionServers(isUsedCs, type);
		for (CollectionServer server : servers) {
			if(server == null)
				continue;
			webSocketSendService.updateDriver(jarInfo, server);
		}
		return MessageUtils.operationSuccess();
	}

	public Object findDriver(String name, String version) {
		if(name == null || version == null)
			return MessageUtils.parameterNullError();
		
		List<CollectionApplication> cas= collectionStatusDao.findCollectionApplication(name, version);
		if(!cas.isEmpty())
			return cas.get(0);
		return null;
	}
}
