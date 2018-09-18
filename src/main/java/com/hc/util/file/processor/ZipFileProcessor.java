package com.hc.util.file.processor;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipFileProcessor {

	@SuppressWarnings("resource")
	public static InputStream readZipFile(String path, String fileName) {
		ZipFile zf = null;
		ZipInputStream zin = null;
		try {
			zf = new ZipFile(path);
			zin = new ZipInputStream(new BufferedInputStream(new FileInputStream(path)));
			ZipEntry ze = null;
			while ((ze = zin.getNextEntry()) != null)
				if (!ze.isDirectory()){
					if(ze.getName().contains(fileName))
						return zf.getInputStream(ze);
				}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static void decompress(String path){
		decompress(new File(path));
	}

	public static void decompress(File path){
		BufferedInputStream bufferedInputStream = null;
		BufferedOutputStream bufferedOutputStream = null;
		ZipInputStream zipInputStream = null;
		ZipEntry zipEntry = null;
		try {
			// ZipInputStream读取压缩文件
			zipInputStream = new ZipInputStream(new FileInputStream(path), Charset.forName("GBK"));
			// 写入到缓冲流中
			bufferedInputStream = new BufferedInputStream(zipInputStream);
			File fileOut = null;
			// 读取压缩文件中的一个文件
			while ((zipEntry = zipInputStream.getNextEntry()) != null) {
				// 若当前zipEntry是一个文件夹
				if (zipEntry.isDirectory()) {
					fileOut = new File(path.getParentFile() + "//" + zipEntry.getName());
					// 在指定路径下创建文件夹
					if (!fileOut.exists()) {
						fileOut.mkdirs();
					}
					// 若是文件
				} else {
					// 原文件名与指定路径创建File对象(解压后文件的对象)
					fileOut = new File(path.getParentFile(), zipEntry.getName());
					try {
						bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(fileOut));
						// 将文件写入到指定file中
						int length = 0;
						byte[] bytes = new byte[2048];
						while ((length = bufferedInputStream.read(bytes)) != -1) {
							bufferedOutputStream.write(bytes, 0, length);
						}
						bufferedOutputStream.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally{
				try {
					if(bufferedOutputStream != null)
						bufferedOutputStream.close();
					if(bufferedInputStream != null)
						bufferedInputStream.close();
					if(zipInputStream != null)
						zipInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
}
