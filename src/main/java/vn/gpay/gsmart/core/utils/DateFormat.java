package vn.gpay.gsmart.core.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateFormat {

	public static String DateToString(Date date) {
		
        return DateToString(date,"yyyy-MM-dd");
	}
	public static Date toDateWithoutTime(Date date){
		if(date!=null) {
			return StringToDate(DateToString(date),"yyyy-MM-dd");
		}
		else
			return null;
	}	
	public static Date StringToDate(String sourceDate, String format){
		try {
			SimpleDateFormat  sourceFormat = new SimpleDateFormat(format);
			Date date = sourceFormat.parse(sourceDate);
			return date;
		}catch(Exception ex){
			return null;
		}
	}	
	public static String DateToString(Date date,String format) {
		String strDate=null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(format);  
	        strDate = dateFormat.format(date);  
		}catch(Exception ex){
			
		}
        return strDate;
	}
	
	public static Date atStartOfDay(Date date) {
		if(date!=null) {
		    LocalDateTime localDateTime = dateToLocalDateTime(date);
		    LocalDateTime startOfDay = localDateTime.with(LocalTime.MIN);
		    return localDateTimeToDate(startOfDay);
		}else {
			return null;
		}
	}

	public static Date atEndOfDay(Date date) {
		if(date!=null) {
		    LocalDateTime localDateTime = dateToLocalDateTime(date);
		    LocalDateTime endOfDay = localDateTime.with(LocalTime.MAX);
		    return localDateTimeToDate(endOfDay);
		}else {
			return null;
		}
	}
	
	private static LocalDateTime dateToLocalDateTime(Date date) {
	    return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
	}

	private static Date localDateTimeToDate(LocalDateTime localDateTime) {
	    return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}
}
