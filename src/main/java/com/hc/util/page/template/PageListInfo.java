package com.hc.util.page.template;

import java.util.ArrayList;
import java.util.List;

public class PageListInfo<E>{
	private int currentPage;
	private int pageCount;
	private int totalCount;
	private int perPageCount;
	private List<E> objs=new ArrayList<>();
	
	public int getCurrentPage() {  
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageCount() {
		return pageCount;
	}
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getPerPageCount() {
		return perPageCount;
	}
	public void setPerPageCount(int perPageCount) {
		this.perPageCount = perPageCount;
	}
	public List<E> getObjs() {
		return objs;
	}
	public void setObjs(List<E> objs) {
		this.objs = objs;
	}
}
