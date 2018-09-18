package com.hc.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hc.service.DriverManagerService;
import com.hc.vo.JarInfoVo;

@Controller
@RequestMapping("/driver/manager")
public class DriverManagerController {

	@Autowired
	private DriverManagerService driverManagerService;
	
	@ResponseBody
	@RequestMapping("/files")
	public Object getAllDriverFiles(){
		return driverManagerService.findAllDriverFiles();
	}
	
	@ResponseBody
	@RequestMapping("/folder/create")
	public Object createFolder(String root){
		return driverManagerService.createFolder(root);
	}
	
	@ResponseBody
	@RequestMapping("/folder/delete")
	public Object deleteFolder(String root){
		return driverManagerService.deleteFolder(root);
	}
	
	@ResponseBody
	@RequestMapping("/folder/update")
	public Object updateFolder(String oldFolder, String newFolder){
		return driverManagerService.updateFolder(oldFolder, newFolder);
	}
	
	@ResponseBody
	@RequestMapping("/jar/upload")
	public Object uploadJarFile(HttpServletRequest request, HttpServletResponse response){
		return driverManagerService.uploadJarFile(request, response);
	}
	
	@RequestMapping("/jar/download")
	public void downloadJarFile(String driverName, String driverVersion, HttpServletResponse response){
		driverManagerService.downloadJarFile(driverName, driverVersion, response);
	}
	
	@ResponseBody
	@RequestMapping("/jar/config/update")
	public Object updateJarConfig(JarInfoVo jarInfo){
		return driverManagerService.updateJarConfig(jarInfo);
	}
	
	@ResponseBody
	@RequestMapping("/jar/latest")
	public Object getDriver(String name, String version){
		return driverManagerService.findDriver(name, version);
	}
}
