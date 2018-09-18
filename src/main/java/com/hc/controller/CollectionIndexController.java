package com.hc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hc.service.CollectionIndexService;

@Controller
@RequestMapping("/index")
public class CollectionIndexController {
	
	@Autowired
	private CollectionIndexService collectionIndexService;

	@ResponseBody
	@RequestMapping("/server/infos")
	public Object getServerInfos(){
		int isUsedCs = 1;
		return collectionIndexService.findServerInfos(isUsedCs);
	}
	
	@ResponseBody
	@RequestMapping("/error/info/record/page")
	public Object getErrorInfosByPage(int currentPage, int pageSize){
		return collectionIndexService.findErrorInfosByPage(currentPage, pageSize);
	}
}
