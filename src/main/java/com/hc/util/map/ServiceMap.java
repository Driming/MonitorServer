package com.hc.util.map;

public class ServiceMap {
	public static final short ADD_RECORD = 1;
	public static final short UPDATE_RECORD = 2;
	public static final short REMOVE_RECORD = 3;
	
	private static String titleAdd = "{server}服务器新增{url}接口";
	private static String contentAdd = "新增接口";
	private static String titleRemove = "{server}服务器删除{url}接口";
	private static String contentRemove = "删除接口";
	private static String url = "http://{server}{url}";

	public static String titleAdd(String server, String url){
		return titleAdd.replace("{server}", server)
				.replace("{url}", url);
	}
	
	public static String contentAdd(){
		return contentAdd;
	}
	
	public static String titleRemove(String server, String url){
		return titleRemove.replace("{server}", server)
				.replace("{url}", url);
	}
	
	public static String contentRemove(){
		return contentRemove;
	}
	
	public static String getUrl(String server, String uri){
		return url.replace("{server}", server)
				.replace("{url}", uri);
	}
}
