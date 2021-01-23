package com.royal.app.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateMonthUtil {
	static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	public static String getMonthString(String month) {
		String monthValue=null;
		for(int i=1; i<=12; i++) {
			if(month.equalsIgnoreCase("january")||month.equalsIgnoreCase("jan")) {
				monthValue = "01";
			}
			if(month.equalsIgnoreCase("february")||month.equalsIgnoreCase("feb")) {
				monthValue = "02";
			}
			if(month.equalsIgnoreCase("march")||month.equalsIgnoreCase("mar")) {
				monthValue = "03";
			}
			if(month.equalsIgnoreCase("april")||month.equalsIgnoreCase("apr")) {
				monthValue = "04";
			}
			if(month.equalsIgnoreCase("may")||month.equalsIgnoreCase("may")) {
				monthValue = "05";
			}
			if(month.equalsIgnoreCase("june")||month.equalsIgnoreCase("jun")) {
				monthValue = "06";
			}
			if(month.equalsIgnoreCase("july")||month.equalsIgnoreCase("jul")) {
				monthValue = "07";
			}
			if(month.equalsIgnoreCase("august")||month.equalsIgnoreCase("aug")) {
				monthValue = "08";
			}
			if(month.equalsIgnoreCase("september")||month.equalsIgnoreCase("sept") || month.equalsIgnoreCase("sep")) {
				monthValue = "09";
			}
			if(month.equalsIgnoreCase("october")||month.equalsIgnoreCase("oct")) {
				monthValue = "10";
			}
			if(month.equalsIgnoreCase("november")||month.equalsIgnoreCase("nov")) {
				monthValue = "11";
			}
			if(month.equalsIgnoreCase("december")||month.equalsIgnoreCase("dec")) {
				monthValue = "12";
			}
		}
		return monthValue;

	}
	
	public static Date getParsedDate(String date) throws ParseException {
		Date dateObj=null;
		dateObj = simpleDateFormat.parse(date);
		return dateObj;
}

	public static LocalDate parseIntoLocalDate(String date1) {
		LocalDate localDate = LocalDate.parse(date1);
		return localDate;
	}

	public static long getNumberOfDaysBetweenDates(LocalDate firstDate, LocalDate localDate) {
		long noOfDaysBetween = ChronoUnit.DAYS.between(firstDate, localDate);
		return noOfDaysBetween;
	}

	public static Date convertIntoDateFormLocalDate(LocalDate date) {
		ZoneId defaultZoneId = ZoneId.systemDefault();
		Date dateObj = Date.from(date.atStartOfDay(defaultZoneId).toInstant());
		return dateObj;
	}

	public static String convertDateIntoString(Date date) {
		String dateString = simpleDateFormat.format(date);
		return dateString;
	}

	public static Date getNextDate(Date date) {
		  final Calendar calendar = Calendar.getInstance();
		  calendar.setTime(date);
		  calendar.add(Calendar.DAY_OF_YEAR, 1);
		  Date endDate =  calendar.getTime();
		  return endDate;
	}

	public static String getOnlyMonth() {
		Date date = new Date();
		SimpleDateFormat datef = new SimpleDateFormat("MM");
		String onlyMonth =	datef.format(date);
		return onlyMonth;
	}

	public static String getTodayDate() {
		Date date = new Date();
		SimpleDateFormat dateFormater = new SimpleDateFormat("dd");
		String todayDate = dateFormater.format(date);
		return todayDate;
	}
}
