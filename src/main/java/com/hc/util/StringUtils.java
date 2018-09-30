package com.hc.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;
import sun.nio.cs.ext.MacThai;

public class StringUtils {
	public static String join(Object... strs) {
		StringBuffer buff = new StringBuffer();
		for (Object str : strs)
			buff.append(str);
		return buff.toString();
	}

	public static String addSeparator(String path) {
		if (!path.endsWith("\\") && !path.endsWith("/"))
			return StringUtils.join(path, "/");
		return path;
	}
	
	public static boolean isStandardValue(String val, String[] vals) {
		for (String str : vals)
			if (val.equalsIgnoreCase(str))
				return true;
		return false;
	}
	
	public static boolean isContainValue(Integer val, List<Integer> vals) {
		for (Integer valEle : vals)
			if (val != null && val.equals(valEle))
				return true;
		return false;
	}
	
	public static List<Integer> strToNum(String[] strs) {
		List<Integer> ins = new ArrayList<>();
		for(int i=0; i<strs.length; i++){
			try{
				ins.add(Integer.valueOf(strs[i]));
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		return ins;
	}
	
	public static Integer createId(int size) {
		StringBuffer strBuff = new StringBuffer();
		int a=0;
		while(a==0){
			a=(int) Math.floor(Math.random() * 10);	//避免首位数字为零从而令int变量变成八进制
		}
		strBuff.append(a);
		for (int i = 1; i < size; i++){
			strBuff.append((int) Math.floor(Math.random() * 10));
		}
		/*for (int i = 0; i < size; i++)
			strBuff.append((int) Math.floor(Math.random() * 10));*/
		return Integer.valueOf(strBuff.toString());
	}
	
	public static String getIp(HttpServletRequest request) {   
       String ip = request.getHeader("x-forwarded-for");   
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
           ip = request.getHeader("Proxy-Client-IP");   
       }   
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
           ip = request.getHeader("WL-Proxy-Client-IP");   
       }   
       if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {   
           ip = request.getRemoteAddr();   
           if(ip.equals("127.0.0.1")){     
               //根据网卡取本机配置的IP     
               InetAddress inet=null;     
               try {     
                   inet = InetAddress.getLocalHost();     
               } catch (UnknownHostException e) {     
                   e.printStackTrace();     
               }
			   try {
				   ip= inet.getHostAddress();
			   } catch (Exception e) {
				   e.printStackTrace();
			   }
		   }
       }   
       // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
       if(ip != null && ip.length() > 15){
           if(ip.indexOf(",")>0){
               ip = ip.substring(0,ip.indexOf(","));     
           }
       }
       return ip;
	}  
	
	public static JsonConfig jsonIgnoreNull(){
   	 JsonConfig config =new JsonConfig();
        config.setJsonPropertyFilter(new PropertyFilter(){
     	   @Override
     	   public boolean apply(Object source,String name,Object value){
     		   	return value==null;
     		   }
     	   });
        return config;
   }
	
}
