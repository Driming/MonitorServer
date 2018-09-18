package com.hc.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hc.entity.control.user.User;
import com.hc.entity.service.record.ServiceUrlUpdateRecord;
import com.hc.service.CollectionServiceService;
import com.hc.util.map.ServerMap;

@Controller
@RequestMapping("/collect/service")
public class CollectionServiceController {

	@Autowired
	private CollectionServiceService collectionServiceService;
	
	@ResponseBody
	@RequestMapping("/urls")
	public Object getServiceUrls(String server){
		server = "10.148.83.228:8921";
		return collectionServiceService.findServiceUrls(server);
	}
	
	@ResponseBody
	@RequestMapping("/servers/urls")
	public Object getServiceUrls(){
		int isUsedCs = 1;
		short type = ServerMap.SERVICE_SERVER;
		return collectionServiceService.findServiceUrls(isUsedCs, type);
	}
	
	@ResponseBody
	@RequestMapping("/url/modify/record/add")
	public Object addServiceUrlUpdateRecord(ServiceUrlUpdateRecord record, HttpSession session){
		User user = (User) session.getAttribute("webUser");
		return collectionServiceService.addServiceUrlUpdateRecord(record, user);
	}
	
	@ResponseBody
	@RequestMapping("/url/modify/record/update")
	public Object updateServiceUrlUpdateRecord(ServiceUrlUpdateRecord record, HttpSession session){
		User user = (User) session.getAttribute("webUser");
		return collectionServiceService.updateServiceUrlUpdateRecord(record, user);
	}
	
	@ResponseBody
	@RequestMapping("/url/modify/record/list")
	public Object listServiceUrlUpdateRecord(
			String searchTime, ServiceUrlUpdateRecord record,
			@RequestParam(defaultValue = "10")int pageSize){
		return collectionServiceService.listServiceUrlUpdateRecord(searchTime, record, pageSize);
	}
	
}
