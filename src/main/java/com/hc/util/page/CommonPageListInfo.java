package com.hc.util.page;

import java.util.ArrayList;
import java.util.List;

import com.hc.util.page.template.PageListInfo;

public class CommonPageListInfo<E>{

	public PageListInfo<E> page(List<E> infos, int currentPage, int pageSize) throws PageErrorException{
		boolean outOfMinSize = currentPage <= 0;
		boolean outOfMaxSize = infos.size() != 0 && infos.size() <= (currentPage -1) * pageSize;
		if(outOfMinSize || outOfMaxSize)
			throw new PageErrorException();
		
		PageListInfo<E> pages = new PageListInfo<>();
		pages.setCurrentPage(currentPage);
		pages.setPerPageCount(pageSize);
		pages.setTotalCount(infos.size());
		int totalPage = 0;
		if(infos.size() % pageSize == 0)
			totalPage = infos.size() / pageSize;
		else
			totalPage = infos.size() / pageSize + 1;
		pages.setPageCount(totalPage);
		pages.setObjs(copySubList(infos, currentPage, pageSize));
		return pages;
	}

	private synchronized List<E> copySubList(List<E> infos, int currentPage, int pageSize) {
		List<E> newInfos = new ArrayList<>();
		for(int i=(currentPage-1)*pageSize; i<infos.size() && i<currentPage*pageSize; i++)
			newInfos.add(infos.get(i));
		
		return newInfos;
	}
}
