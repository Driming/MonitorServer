package com.hc.util.page.template;

public class PageObjInfo<E> {
	private int currentPage;
	private int pageCount;
	private int totalCount;
	private int perPageCount;
	private E obj;

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

	public E getObj() {
		return obj;
	}

	public void setObj(E obj) {
		this.obj = obj;
	}

}
