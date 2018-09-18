package com.hc.util.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.hc.util.StringUtils;

public class UploadFileUtils {

	public synchronized static List<FileItem> getFileItem(HttpServletRequest request, HttpServletResponse response) {
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> fileitems = upload.parseRequest(request);
			return fileitems;
		} catch (FileUploadException e) {
			e.printStackTrace();
		}
		return null;
	}

	public synchronized static Map<String, String> uploadText(List<FileItem> fileItems, String encode) {
		// 修改保存路径
		Map<String, String> maps = new HashMap<>();
		try {
			for (FileItem item : fileItems)
				if (item.isFormField())
					// 普通文本
					maps.put(item.getFieldName(), new String(item.getString().getBytes(encode), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return maps;
	}

	public synchronized static List<String> getFileNames(List<FileItem> fileItems) {
		List<String> fileNames = new LinkedList<String>();
		if (fileItems == null)
			return fileNames;
		for (FileItem item : fileItems) {
			if (!item.isFormField()) {
				// 文件
				String fullFileName = item.getName();
				int index = fullFileName.lastIndexOf("/") + 1;
				String filename = null;
				if (index != -1) {
					filename = fullFileName.substring(index, fullFileName.length());
				} else {
					filename = fullFileName;
				}
				if (filename.toUpperCase().indexOf(".jpg".toUpperCase()) == -1
						&& filename.toUpperCase().indexOf(".jpeg".toUpperCase()) == -1
						&& filename.toUpperCase().indexOf(".png".toUpperCase()) == -1
						&& filename.toUpperCase().indexOf(".gif".toUpperCase()) == -1)
					fileNames.add(filename);
			}
		}
		return fileNames;
	}

	public synchronized static Map<String, String> uploadFile(List<FileItem> fileItems, String imagePath,
			String filePath, String encode, String preName) throws UnsupportedEncodingException {
		// 修改保存路径
		System.out.println("image:" + imagePath);
		Map<String, String> maps = new HashMap<>();
		if (fileItems == null)
			return maps;

		try {
			for (FileItem item : fileItems) {
				if (item.isFormField()) {
					// 普通文本
					maps.put(item.getFieldName(), new String(item.getString().getBytes(encode), "UTF-8"));
				} else {
					// 文件
					String fullFileName = item.getName();
					int index = fullFileName.lastIndexOf("/") + 1;
					String filename = null;
					if (index != -1) {
						filename = fullFileName.substring(index, fullFileName.length());
					} else {
						filename = fullFileName;
					}
					System.out.println("filename.get:" + filename);
					if (filename.toUpperCase().indexOf(".jpg".toUpperCase()) != -1
							|| filename.toUpperCase().indexOf(".jpeg".toUpperCase()) != -1
							|| filename.toUpperCase().indexOf(".png".toUpperCase()) != -1
							|| filename.toUpperCase().indexOf(".gif".toUpperCase()) != -1) {
						// 图片
						System.out.println("iamgefieldname:" + item.getFieldName());
						InputStream in = item.getInputStream();
						maps.put(item.getFieldName(), uploadImage(imagePath, filename, in, preName));
					} else {
						// 其他文件
						InputStream in = item.getInputStream();
						maps.put(item.getFieldName(), uploadFile(filePath, filename, in, preName));
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return maps;
	}

	public synchronized static Map<String, String> uploadFile(HttpServletRequest request, HttpServletResponse response,

			String imagePath, String filePath, String encode, String preName) throws UnsupportedEncodingException {

		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html;charset=UTF-8");
		// 修改保存路径
		System.out.println("图片路径目录:" + imagePath);
		Map<String, String> maps = new HashMap<>();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		try {
			List<FileItem> fileitems = upload.parseRequest(request);
			for (FileItem item : fileitems) {
				if (item.isFormField()) {
					// 普通文本
					maps.put(item.getFieldName(), new String(item.getString().getBytes(encode), "UTF-8"));
				} else {
					// 文件
					String fullFileName = item.getName();
					int index = fullFileName.lastIndexOf("/") + 1;
					String filename = null;
					if (index != -1) {
						filename = fullFileName.substring(index, fullFileName.length());
					} else {
						filename = fullFileName;
					}
					System.out.println("图片名字1" + filename);
					if (filename.toUpperCase().indexOf(".jpg".toUpperCase()) != -1
							|| filename.toUpperCase().indexOf(".jpeg".toUpperCase()) != -1
							|| filename.toUpperCase().indexOf(".png".toUpperCase()) != -1
							|| filename.toUpperCase().indexOf(".gif".toUpperCase()) != -1) {
						// 图片
						System.out.println("图片名字2:" + item.getFieldName());
						InputStream in = item.getInputStream();
						maps.put(item.getFieldName(), uploadImage(imagePath, filename, in, preName));
					} else {
						// 其他文件
						InputStream in = item.getInputStream();
						maps.put(item.getFieldName(), uploadFile(imagePath, filename, in, preName));
					}

				}
			}
		} catch (FileUploadException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return maps;
	}

	private static String uploadImage(String imagePath, String filename, InputStream in, String preName) {

		File file = new File(imagePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		byte[] bytes = new byte[1024];
		int len = 0;
		if (preName != null)
			filename = StringUtils.join(preName, filename);
		String savePath = imagePath + "/" + filename;
		System.out.println("保存的全路径（加文件名）:" + savePath);
		String returnPath = "/images/" + filename;
		OutputStream out = null;
		try {
			out = new FileOutputStream(savePath);
			while ((len = in.read(bytes)) != -1) {
				out.write(bytes, 0, len);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnPath;
	}

	private static String uploadFile(String filePath, String filename, InputStream in, String preName) {
		File file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
		byte[] bytes = new byte[1024];
		int len = 0;
		if (preName != null)
			filename = StringUtils.join(preName, filename);
		String savePath = filePath + "/" + filename;
		System.out.println("savefile--2:" + savePath);
		String returnPath = "/images/" + filename;
		OutputStream out = null;
		try {
			out = new FileOutputStream(savePath);
			while ((len = in.read(bytes)) != -1) {
				out.write(bytes, 0, len);
			}
			in.close();
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnPath;
	}

	public ResponseEntity<byte[]> download(String fileName, File file) throws IOException {

		String dfileName = new String(fileName.getBytes("gb2312"), "iso8859-1");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

		headers.setContentDispositionFormData("attachment", dfileName);

		return new ResponseEntity<byte[]>(org.apache.commons.io.FileUtils.readFileToByteArray(file), headers,
				HttpStatus.CREATED);
	}

}
