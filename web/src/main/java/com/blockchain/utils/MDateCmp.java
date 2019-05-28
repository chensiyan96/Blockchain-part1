package com.blockchain.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MDateCmp
{
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public static Date timeAdd(int days, Date from)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(from);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}

	public static String timeFormat(Date d)
	{
		return sdf.format(d);
	}

	public static Date parse(String s) throws ParseException
	{
		return sdf.parse(s);
	}


}
