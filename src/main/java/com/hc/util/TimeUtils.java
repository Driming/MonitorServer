package com.hc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
	
	public static long divNotSure(long time, int interval, Integer sub){
		time = time - (time+8*60*60*1000)%(interval*60*1000);
		if(sub != null)
			time = time - (long)(sub*interval *60*1000);
		return time;
	}
	
	public static long todayZeroHour(){
		long current = System.currentTimeMillis();
		return current - (current+8*60*60*1000)%(24*60*60*1000);
	}
	
	public static long tomorrowZeroHour(){
		long current = System.currentTimeMillis();
		return current - (current+8*60*60*1000)%(24*60*60*1000)
				+ 24*60*60*1000;
	}
	
	public static long getStandardMilli(long time, int modValueMinute){
		return time - (time+8*60*60*1000)%(modValueMinute*60*1000);
	}

	public static long getStartMonth(long milli) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milli);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int newMonth = month - 11;
		if(newMonth <= 0){
			newMonth += 12;
			year -= 1;
		}
			
		Date parse = format.parse(StringUtils.join(String.format("%04d", year), String.format("%02d", newMonth)));
		return parse.getTime();
	}
	
	public static long getMonthMilli(long milli) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		Date parse = format.parse(format.format(new Date(milli)));
		return parse.getTime();
	}
	
	public static long addOneMonth(long milli) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milli);
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int newMonth = month + 1;
		if(newMonth > 12){
			newMonth -= 12;
			year += 1;
		}
			
		Date parse = format.parse(StringUtils.join(String.format("%04d", year), String.format("%02d", newMonth)));
		return parse.getTime();
	}
	
	public static long getYearMilli(long milli) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milli);
		int year = calendar.get(Calendar.YEAR);
			
		Date parse = format.parse(StringUtils.join(String.format("%04d", year)));
		return parse.getTime();
	}
	
	public static long addOneYear(long milli) throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(milli);
		int year = calendar.get(Calendar.YEAR) + 1;
			
		Date parse = format.parse(StringUtils.join(String.format("%04d", year)));
		return parse.getTime();
	}
	
	
}
